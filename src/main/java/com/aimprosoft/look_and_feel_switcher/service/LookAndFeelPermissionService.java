package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.view.Action;
import com.aimprosoft.look_and_feel_switcher.model.view.ResourcePermissions;
import com.aimprosoft.look_and_feel_switcher.model.view.Role;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;

import java.util.List;

/**
 * service for {@link com.liferay.portal.model.Role} and {@link ResourcePermissions} xorrespon
 *
 * @author Mikhail Tkachenko
 */
public interface LookAndFeelPermissionService {

    String RESOURCE_NAME = LookAndFeel.class.getName();
    int SCOPE = ResourceConstants.SCOPE_COMPANY;


    ResourcePermissions getPermissions(long companyId, Integer lookAndFeelId) throws ApplicationException;

    void applyPermissions(ResourcePermissions resourcePermissions, long companyId) throws ApplicationException;

    List<Action> getAllowedActions(LookAndFeel lookAndFeel, User user) throws ApplicationException;

    void addDefaultPermissions(LookAndFeel lookAndFeel) throws ApplicationException;

    List<Action> getLookAndFeelActions() throws ApplicationException;

    List<Role> getCompanyRoles(long companyId) throws ApplicationException;

    void updatePermissions(long companyId, Role role, List<Action> actions, String lookAndFeelId) throws ApplicationException;

    void deletePermissions(long companyId, String lookAndFeelId) throws ApplicationException;
}