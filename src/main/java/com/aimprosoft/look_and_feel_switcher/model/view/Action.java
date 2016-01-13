package com.aimprosoft.look_and_feel_switcher.model.view;

import com.liferay.portal.model.ResourceAction;

/**
 * crated by m.tkachenko on 11.01.16 14:01
 */
public class Action {

    private Long id;
    private String name;
    private boolean permitted;

    public Action() {
    }

    public Action(ResourceAction resourceAction) {
        this.id = resourceAction.getResourceActionId();
        this.name = resourceAction.getActionId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPermitted() {
        return permitted;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;

        if (getPermitted() != action.getPermitted()) return false;
        return !(getName() != null ? !getName().equals(action.getName()) : action.getName() != null);

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
