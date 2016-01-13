package com.aimprosoft.look_and_feel_switcher.model.view;

import java.io.Serializable;

/**
 * crated by m.tkachenko on 02.10.15 20:51
 */
public class OptionViewModel implements Comparable<OptionViewModel> {

    private Serializable id;
    private String name;
    private Boolean selected = false;


    public OptionViewModel(Serializable id, String name) {
        this.id = id;
        this.name = name;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(OptionViewModel optionViewModel) {
        return name.compareTo(optionViewModel.getName());
    }
}