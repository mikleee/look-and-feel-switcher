package com.aimprosoft.look_and_feel_switcher.model.view;


import java.util.ArrayList;
import java.util.List;

/**
 * crated by m.tkachenko on 08.01.16 15:14
 */
public class RolePermission {

    private Role role;
    private List<Action> actions = new ArrayList<Action>();

    public RolePermission(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void put(Action action) {
        actions.add(action);
    }

}
