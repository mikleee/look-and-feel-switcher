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
        return concatSrcPath(imagesPath, defineImageToShow(dirFsPath));
    }

    public static String getScreenShotPath(ColorScheme colorScheme, Theme theme) {
        String dirFsPath = getImagesDirFsPath(colorScheme, theme);
        String imagesPath = theme.getContextPath() + colorScheme.getColorSchemeThumbnailPath();
        return concatSrcPath(imagesPath, defineImageToShow(dirFsPath));
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
            return root.replaceAll(concatFsPath("", "ROOT", ""), concatFsPath(theme.getContextPath(), imagesPath));
        } else {
            return root + imagesPath;
        }
    }

    private static String defineImageToShow(String root) {
        String screenShot = concatFsPath(root, THUMBNAIL_NAME);
        if (new File(screenShot).exists()) {
            return THUMBNAIL_NAME;
        } else {
            LOGGER.trace("No thumbnail found by path " + screenShot + ". returning the screenshot " + concatFsPath(root, SCREENSHOT_NAME));
            return SCREENSHOT_NAME;
        }
    }

    private static String concatFsPath(String... parts) {
        return concatPath(getFileSeparator(), parts);
    }

    private static String concatSrcPath(String... parts) {
        return concatPath("/", parts);
    }

    private static String concatPath(String delimiter, String... parts) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            result.append(parts[i]);
            if (i != parts.length - 1) {
                result.append(delimiter);
            }
        }
        return result.toString();
    }

}