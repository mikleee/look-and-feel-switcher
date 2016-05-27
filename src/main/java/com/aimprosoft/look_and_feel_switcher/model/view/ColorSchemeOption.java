package com.aimprosoft.look_and_feel_switcher.model.view;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelType;

import java.io.Serializable;

/**
 * The data transfer object for the {@link LookAndFeel} with {@link LookAndFeelType#COLOR_SCHEME} type
 *
 * @author Mikhail Tkachenko
 */
public class ColorSchemeOption extends LookAndFeelOption {

    public ColorSchemeOption(Serializable id, String name) {
        super(id, name);
    }

}