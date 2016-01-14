package com.aimprosoft.look_and_feel_switcher.model.view;

import java.util.ArrayList;
import java.util.List;

/**
 * crated by m.tkachenko on 08.01.16 21:10
 */
public class ResourcePermissions {

    private String id;
    private List<RolePermission> permissions = new ArrayList<RolePermission>();
    private List<Action> allowedActions = new ArrayList<Action>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
