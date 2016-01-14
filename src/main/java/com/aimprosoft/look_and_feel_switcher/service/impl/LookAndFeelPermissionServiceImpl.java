package com.aimprosoft.look_and_feel_switcher.service.impl;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.view.Action;
import com.aimprosoft.look_and_feel_switcher.model.view.ResourcePermissions;
import com.aimprosoft.look_and_feel_switcher.model.view.Role;
import com.aimprosoft.look_and_feel_switcher.model.view.RolePermission;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelPermissionService;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ResourceAction;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * crated by m.tkachenko on 04.01.16 11:48
 */
@Service
public class LookAndFeelPermissionServiceImpl implements LookAndFeelPermissionService {


    @Override
    public ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId) throws ApplicationException {
        ResourcePermissions permissions = new ResourcePermissions();
        List<Action> actions = getLookAndFeelActions();
        permissions.setAllowedActions(actions);
        for (Role role : getCompanyRoles(companyId)) {
            RolePermission rolePermission = new RolePermission(role);
            for (Action action : actions) {
                boolean permitted = hasPermission(companyId, role, lookAndFeelId.toString(), action);
                action.setPermitted(permitted);
                rolePermission.put(action);
            }
            permissions.getPermissions().add(rolePermission);
        }

        permissions.setId(lookAndFeelId.toString());
        return permissions;
    }

    @Override
    public void applyPermissions(ResourcePermissions resourcePermissions, long companyId) throws ApplicationException {
        for (RolePermission rolePermission : resourcePermissions.getPermissions()) {
            updatePermissions(companyId, rolePermission.getRole(), rolePermission.getActions(), resourcePermissions.getId());
        }
    }

    @Override
    public List<Action> getAllowedActions(LookAndFeel lookAndFeel, User user) throws ApplicationException {
        List<Action> allActions = getLookAndFeelActions();
        List<Action> result = new ArrayList<Action>();

        for (Role role : getUserRoles(user)) {
            for (Action action : allActions) {
                if (result.size() == allActions.size()) {
                    return result;
                } else if (!result.contains(action)) {
                    action.setPermitted(hasPermission(lookAndFeel.getCompanyId(), role, lookAndFeel.getId().toString(), action));
                    result.add(action);
                }
            }
        }

        return result;
    }

    @Override
    public void addDefaultPermissions(LookAndFeel lookAndFeel) throws ApplicationException {
        List<Action> allActions = getLookAndFeelActions();
        for (Action action : allActions) {
            action.setPermitted(true);
        }
        for (Role role : getCompanyRoles(lookAndFeel.getCompanyId())) {
            updatePermissions(lookAndFeel.getCompanyId(), role, allActions, lookAndFeel.getId().toString());
        }
    }

    @Override
    public List<Action> getLookAndFeelActions() throws ApplicationException {
        try {
            ArrayList<Action> actions = new ArrayList<Action>();
            for (ResourceAction resourceAction : ResourceActionLocalServiceUtil.getResourceActions(RESOURCE_NAME)) {
                if (!ActionKeys.PERMISSIONS.equals(resourceAction.getActionId())) {
                    actions.add(new Action(resourceAction));
                }
            }
            return actions;
        } catch (SystemException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public List<Role> getCompanyRoles(long companyId) throws ApplicationException {
        try {
            List<Role> result = new ArrayList<Role>();
            for (com.liferay.portal.model.Role role : RoleLocalServiceUtil.getRoles(companyId)) {
                result.add(new Role(role));
            }
            return result;
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }

    @Override
    public void updatePermissions(long companyId, Role role, List<Action> actions, String lookAndFeelId) throws ApplicationException {
        try {
            ResourcePermission resourcePermission = ResourcePermissionLocalServiceUtil.fetchResourcePermission(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId());
            if (resourcePermission == null) {
                for (Action action : actions) {
                    ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId(), action.getName());
                }
            } else {
                ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId(), actionIds(actions));
            }
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    @Override
    public void deletePermissions(long companyId, String lookAndFeelId) throws ApplicationException {
        try {
            ResourcePermissionLocalServiceUtil.deleteResourcePermissions(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId);
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }


    private List<Role> getUserRoles(User user) throws ApplicationException {
        try {
            List<Role> result = new ArrayList<Role>();
            for (com.liferay.portal.model.Role role : user.getRoles()) {
                result.add(new Role(role));
            }
            return result;
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }

    public boolean hasPermission(long companyId, Role role, String resourceId, Action action) throws ApplicationException {
        try {
            ResourcePermission resourcePermission = ResourcePermissionLocalServiceUtil.fetchResourcePermission(companyId, RESOURCE_NAME, SCOPE, resourceId, role.getId());
            return resourcePermission != null && resourcePermission.hasActionId(action.getName());
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    private String[] actionIds(List<Action> actions) {
        List<String> allowed = new ArrayList<String>();
        for (Action action : actions) {
            if (action.getPermitted()) {
                allowed.add(action.getName());
            }
        }

        return allowed.toArray(new String[]{});
    }


}