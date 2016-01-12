package com.aimprosoft.look_and_feel_switcher.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * crated by m.tkachenko on 09.10.15 10:41
 */
public class LookAndFeelOption extends OptionViewModel {

    private Boolean bind = false;
    private Boolean portalDefault = false;
    private String screenShotPath;
    private List<Action> allowedActions = new ArrayList<Action>();

    public LookAndFeelOption(Serializable id, String name) {
        super(id, name);
    }

    public Boolean getBind() {
        return bind;
    }

    public void setBind(Boolean bind) {
        this.bind = bind;
    }

    public Boolean getPortalDefault() {
        return portalDefault;
    }

    public void setPortalDefault(Boolean portalDefault) {
        this.portalDefault = portalDefault;
    }

    public String getScreenShotPath() {
        return screenShotPath;
    }

    public void setScreenShotPath(String screenShotPath) {
        this.screenShotPath = screenShotPath;
    }

    public List<Action> getAllowedActions() {
        return allowedActions;
    }

    public void setAllowedActions(List<Action> allowedActions) {
        this.allowedActions = allowedActions;
    }

}