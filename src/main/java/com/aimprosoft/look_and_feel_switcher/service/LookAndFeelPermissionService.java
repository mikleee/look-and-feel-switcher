package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.view.Role;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ResourceAction;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * crated by m.tkachenko on 04.01.16 11:48
 */
@Service
public class LookAndFeelPermissionService implements InitializingBean {

    private final static String RESOURCE_NAME = LookAndFeel.class.getName();

    private static List<String> lookAndFeelActions;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (lookAndFeelActions == null) {
            lookAndFeelActions = new ArrayList<String>();
            for (ResourceAction resourceAction : fetchLookAndFeelActions(RESOURCE_NAME)) {
                if (!ActionKeys.PERMISSIONS.equals(resourceAction.getActionId())) {
                    lookAndFeelActions.add(resourceAction.getActionId());
                }
            }
        }
    }

    public boolean hasPermission(ThemeDisplay themeDisplay, String id, String action) {
        PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();
        return permissionChecker.hasPermission(themeDisplay.getScopeGroupId(), RESOURCE_NAME, id, action);
    }

    private List<ResourceAction> fetchLookAndFeelActions(String name) throws ApplicationException {
        try {
            return ResourceActionLocalServiceUtil.getResourceActions(name);
        } catch (SystemException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public List<String> getLookAndFeelActions() throws ApplicationException {
        return lookAndFeelActions;
    }

    public List<Role> getRoles(ThemeDisplay themeDisplay) throws ApplicationException {
        List<Role> result = new ArrayList<Role>();
        for (com.liferay.portal.model.Role role : fetchRoles(themeDisplay.getCompanyId())) {
            result.add(new Role(role));
        }
        return result;
    }

    private List<com.liferay.portal.model.Role> fetchRoles(long companyId) throws ApplicationException {
        try {
            return RoleLocalServiceUtil.getRoles(companyId);
        } catch (SystemException e) {
            throw new ApplicationException(e.getMessage());
        }
    }


}