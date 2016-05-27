package com.aimprosoft.look_and_feel_switcher.model.view;

import com.liferay.portal.model.ResourceAction;

import java.util.ArrayList;
import java.util.List;

/**
 * The data transfer which represents the mapping for for the {@link ResourceAction}
 *
 * @author Mikhail Tkachenko
 */
public class ResourcePermissions {
    private Integer id;
    private List<RolePermission> permissions = new ArrayList<RolePermission>();
    private List<Action> allowedActions = new ArrayList<Action>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public List<Action> getAllowedActions() {
        return allowedActions;
    }

    public void setAllowedActions(List<Action> allowedActions) {
        this.allowedActions = allowedActions;
    }
}
