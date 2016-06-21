package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.view.Action;
import com.aimprosoft.lfs.model.view.ResourcePermissions;
import com.aimprosoft.lfs.model.view.Role;
import com.aimprosoft.lfs.model.view.RolePermission;
import com.aimprosoft.lfs.service.LookAndFeelPermissionService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
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
 * @author Mikhail Tkachenko
 */
@Service
public class LookAndFeelPermissionServiceImpl implements LookAndFeelPermissionService {

    @Override
    public long count(long companyId) throws ApplicationException {
        try {
            DynamicQuery query = companyRolesQueryTemplate(companyId);
            return RoleLocalServiceUtil.dynamicQueryCount(query);
        } catch (SystemException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId) throws ApplicationException {
        List<Role> roles = getCompanyRoles(companyId);
        return getPermissions(companyId, lookAndFeelId, roles);
    }

    @Override
    public ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId, int pageNo, int pageSize) throws ApplicationException {
        List<Role> roles = getCompanyRoles(companyId, pageNo, pageSize);
        return getPermissions(companyId, lookAndFeelId, roles);
    }

    @Override
    public void applyPermissions(ResourcePermissions resourcePermissions, long companyId) throws ApplicationException {
        for (RolePermission rolePermission : resourcePermissions.getPermissions()) {
            updatePermissions(companyId, rolePermission.getRole(), rolePermission.getActions(), String.valueOf(resourcePermissions.getId()));
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
    public void updatePermissions(long companyId, Role role, List<Action> actions, String lookAndFeelId) throws ApplicationException {
        ResourcePermission resourcePermission = fetchResourcePermission(companyId, role, lookAndFeelId);
        try {
            if (resourcePermission == null) {
                for (Action action : actions) {
                    ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId(), action.getName());
                }
            } else {
                ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, RESOURCE_NAME, SCOPE, lookAndFeelId, role.getId(), permittedActionIds(actions));
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

    private ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId, List<Role> roles) throws ApplicationException {
        ResourcePermissions permissions = new ResourcePermissions();
        List<Action> actions = getLookAndFeelActions();
        permissions.setAllowedActions(actions);
        for (Role role : roles) {
            RolePermission rolePermission = new RolePermission(role);
            for (Action item : getLookAndFeelActions()) {
                Action action = item.clone();
                boolean permitted = hasPermission(companyId, role, String.valueOf(lookAndFeelId), action);
                action.setPermitted(permitted);
                rolePermission.put(action);
            }
            permissions.getPermissions().add(rolePermission);
        }

        permissions.setId(lookAndFeelId);
        return permissions;
    }

    private List<Action> getLookAndFeelActions() throws ApplicationException {
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

    private List<Role> getCompanyRoles(long companyId) throws ApplicationException {
        try {
            List<com.liferay.portal.model.Role> roles = RoleLocalServiceUtil.getRoles(companyId);
            return toViewRoles(roles);
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }


    private List<Role> getCompanyRoles(long companyId, int pageNo, int pageSize) throws ApplicationException {
        final int start = (pageNo - 1) * pageSize;
        final int end = start + pageSize;

        DynamicQuery query = companyRolesQueryTemplate(companyId).addOrder(OrderFactoryUtil.asc("name"));

        try {
            //noinspection unchecked
            List<com.liferay.portal.model.Role> roles = RoleLocalServiceUtil.dynamicQuery(query, start, end);
            return toViewRoles(roles);
        } catch (SystemException e) {
            throw new ApplicationException();
        }
    }

    private DynamicQuery companyRolesQueryTemplate(long companyId) {
        return DynamicQueryFactoryUtil.forClass(com.liferay.portal.model.Role.class)
                .add(PropertyFactoryUtil.forName("companyId").eq(companyId));
    }


    private List<Role> toViewRoles(List<com.liferay.portal.model.Role> roles) throws ApplicationException {
        List<Role> result = new ArrayList<Role>();
        for (com.liferay.portal.model.Role role : roles) {
            result.add(new Role(role));
        }
        return result;
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

    private boolean hasPermission(long companyId, Role role, String resourceId, Action action) throws ApplicationException {
        ResourcePermission resourcePermission = fetchResourcePermission(companyId, role, resourceId);
        return resourcePermission != null && resourcePermission.hasActionId(action.getName());
    }

    private ResourcePermission fetchResourcePermission(long companyId, Role role, String resourceId) throws ApplicationException {
        DynamicQuery query = DynamicQueryFactoryUtil.forClass(ResourcePermission.class)
                .add(PropertyFactoryUtil.forName("companyId").eq(companyId))
                .add(PropertyFactoryUtil.forName("name").eq(RESOURCE_NAME))
                .add(PropertyFactoryUtil.forName("scope").eq(SCOPE))
                .add(PropertyFactoryUtil.forName("primKey").eq(resourceId))
                .add(PropertyFactoryUtil.forName("roleId").eq(role.getId()));
        try {
            //noinspection unchecked
            List<ResourcePermission> list = (List<ResourcePermission>) ResourcePermissionLocalServiceUtil.dynamicQuery(query);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            throw new ApplicationException();
        }
    }

    private String[] permittedActionIds(List<Action> actions) {
        List<String> result = new ArrayList<String>();
        for (Action action : actions) {
            if (action.getPermitted()) {
                result.add(action.getName());
            }
        }

        return result.toArray(new String[]{});
    }


}