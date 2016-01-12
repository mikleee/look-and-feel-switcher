package com.aimprosoft.look_and_feel_switcher.model.view;

import com.liferay.portal.model.RoleConstants;

/**
 * crated by m.tkachenko on 08.01.16 19:23
 */
public class Role {

    private long id;
    private String name;
    private String type;

    public Role() {
    }

    public Role(com.liferay.portal.model.Role role) {
        id = role.getRoleId();
        name = role.getName();
        type = RoleConstants.GUEST.equals(name) ? "guest" : role.getTypeLabel();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}