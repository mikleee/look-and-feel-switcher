package com.aimprosoft.look_and_feel_switcher.model.view;

import java.util.HashMap;
import java.util.Map;

/**
 * crated by m.tkachenko on 08.01.16 15:14
 */
public class RolePermission {

    private String role;
    private Map<String, Boolean> actions = new HashMap<String, Boolean>();

    public RolePermission() {
    }

    public RolePermission(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, Boolean> getActions() {
        return actions;
    }

    public void setActions(Map<String, Boolean> actions) {
        this.actions = actions;
    }

    public void put(String action, Boolean hasPermission) {
        actions.put(action, hasPermission);
    }

}
