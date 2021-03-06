package com.aimprosoft.lfs.utils;

import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.util.PortalUtil;
import org.apache.log4j.Logger;

import java.io.File;

import static com.aimprosoft.lfs.utils.Utils.getFileSeparator;

/**
 * utils for {@link LookAndFeel}
 *
 * @author Mikhail Tkachenko
 */
public class LookAndFeelUtils {

    private final static Logger LOGGER = Logger.getLogger(LookAndFeelUtils.class);
    private final static String SCREENSHOT_NAME = "screenshot.png";
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
        String screenShot = concatPath(root, SCREENSHOT_NAME);
        if (new File(screenShot).exists()) {
            return SCREENSHOT_NAME;
        } else {
            LOGGER.trace("No screenshot found by path " + screenShot + ". returning the thumbnail " + concatPath(root, THUMBNAIL_NAME));
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