package com.aimprosoft.look_and_feel_switcher.model.view;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The data transfer object for the {@link LookAndFeel} with {@link LookAndFeelType#THEME} type
 *
 * @author Mikhail Tkachenko
 */
public class ThemeOption extends LookAndFeelOption {

    private List<ColorSchemeOption> colorSchemes = new ArrayList<ColorSchemeOption>();

    public ThemeOption(Serializable id, String name) {
        super(id, name);
    }

    public List<ColorSchemeOption> getColorSchemes() {
        return colorSchemes;
    }

    public void addColorScheme(ColorSchemeOption colorScheme) {
        colorSchemes.add(colorScheme);
    }

}