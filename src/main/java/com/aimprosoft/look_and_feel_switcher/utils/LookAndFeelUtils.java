package com.aimprosoft.look_and_feel_switcher.utils;

import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;

import static com.aimprosoft.look_and_feel_switcher.utils.Utils.getFileSeparator;

/**
 * @author Mikhail Tkachenko
 */
public class LookAndFeelUtils {

    public final static String SCREEN_SHOT_NAME = "screenshot.png";

    public static String getScreenShotPath(Theme theme) {
        return theme.getContextPath() + theme.getImagesPath() + getFileSeparator() + SCREEN_SHOT_NAME;
    }

    public static String getScreenShotPath(ColorScheme colorScheme) {
        return colorScheme.getColorSchemeThumbnailPath() + getFileSeparator() + SCREEN_SHOT_NAME;
    }

}