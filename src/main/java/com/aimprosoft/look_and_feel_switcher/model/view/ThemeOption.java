package com.aimprosoft.look_and_feel_switcher.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * crated by m.tkachenko on 09.10.15 10:45
 */
public class ThemeOption extends LookAndFeelOption {

    private List<LookAndFeelOption> colorSchemes;

    public ThemeOption(Serializable id, String name) {
        super(id, name);
    }

    public List<LookAndFeelOption> getColorSchemes() {
        return colorSchemes;
    }

    public void addColorScheme(LookAndFeelOption colorScheme) {
        if (colorSchemes == null) {
            colorSchemes = new ArrayList<LookAndFeelOption>();
        }
        colorSchemes.add(colorScheme);
    }

}