package com.aimprosoft.look_and_feel_switcher.utils;

import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.util.PortalUtil;
import org.apache.log4j.Logger;

import java.io.File;

import static com.aimprosoft.look_and_feel_switcher.utils.Utils.getFileSeparator;

/**
 * @author Mikhail Tkachenko
 */
public class LookAndFeelUtils {

    private final static Logger LOGGER = Logger.getLogger(LookAndFeelUtils.class);
    private final static String SCREEN_SHOT_NAME = "screenshot.png";
    private final static String THUMBNAIL_NAME = "thumbnail.png";

    public static String getScreenShotPath(Theme theme) {
        String dirFsPath = getImagesDirFsPath(theme);
        String imagesPath = theme.getContextPath() + theme.getImagesPath();
        return concatPath(imagesPath, defineImageToShow(dirFsPath));
    }

    public static String getScreenShotPath(ColorScheme colorScheme, Theme theme) {
        String dirFsPath = getImagesDirFsPath(colorScheme, theme);
        String imagesPath = theme.getContextPath() + colorScheme.getColorSchemeThumbnailPath();
        return concatPath(imagesPath, defineImageToShow(dirFsPath));
    }

    private static String getImagesDirFsPath(Theme theme) {
        String imagesPath = theme.getImagesPath().substring(1);
        return getImagesDirFsPath(theme, imagesPath);
    }

    private static String getImagesDirFsPath(ColorScheme colorScheme, Theme theme) {
        String imagesPath = colorScheme.getColorSchemeImagesPath().substring(1);
        return getImagesDirFsPath(theme, imagesPath);
    }

    private static String getImagesDirFsPath(Theme theme, String imagesPath) {
        String root = PortalUtil.getPortalWebDir();
        if (theme.isWARFile()) {
            return root.replaceAll(concatPath("", "ROOT", ""), concatPath(theme.getContextPath(), imagesPath));
        } else {
            return root + imagesPath;
        }
    }

    private static String defineImageToShow(String root) {
        String screenShot = concatPath(root, SCREEN_SHOT_NAME);
        if (new File(screenShot).exists()) {
            return SCREEN_SHOT_NAME;
        } else {
            LOGGER.warn("No screenshot found by path " + screenShot + ". returning the thumbnail " + concatPath(root, THUMBNAIL_NAME));
            return THUMBNAIL_NAME;
        }
    }

    private static String concatPath(String... parts) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            result.append(parts[i]);
            if (i != parts.length - 1) {
                result.append(getFileSeparator());
            }
        }
        return result.toString();
    }

}