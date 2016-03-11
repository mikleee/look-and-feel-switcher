package com.aimprosoft.look_and_feel_switcher.service.impl;

import com.aimprosoft.look_and_feel_switcher.dao.LookAndFeelDao;
import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.ColorSchemeOption;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelPermissionService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.service.SynchronizationService;
import com.aimprosoft.look_and_feel_switcher.utils.LookAndFeelUtils;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Theme;
import com.liferay.portal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Tkachenko
 */
@Component
public class LookAndFeelServiceImpl implements LookAndFeelService {

    @Autowired
    private LookAndFeelDao lookAndFeelDao;
    @Autowired
    private LookAndFeelPermissionService permissionService;
    @Autowired
    private SynchronizationService synchronizationService;


    @Override
    public LookAndFeel find(Integer id) throws ApplicationException {
        LookAndFeel result = lookAndFeelDao.findById(id);
        if (result == null) {
            throw new ApplicationException("ts-requested-look-and-feel-not-available");
        }
        return result;
    }

    @Override
    public List<ThemeOption> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault, User user) throws ApplicationException {
        List<ThemeOption> lookAndFeels = new ArrayList<ThemeOption>();

        long companyId = fromView.getLookAndFeel().getCompanyId();
        for (LookAndFeel themeLookAndFeel : fetchThemes(companyId)) {
            ThemeOption themeOption = createThemeOption(themeLookAndFeel, persisted, portalDefault.getThemeId(), user);
            if (themeOption.isViewActionPermitted()) {

                List<LookAndFeel> colorSchemes = lookAndFeelDao.findColorSchemes(themeLookAndFeel.getThemeId(), companyId);
                for (LookAndFeel csLookAndFeel : colorSchemes) {
                    ColorSchemeOption colorSchemeOption = createColorSchemeOption(csLookAndFeel, persisted, portalDefault.getColorSchemeId(), user);
                    if (colorSchemeOption.isViewActionPermitted()) {
                        themeOption.addColorScheme(colorSchemeOption);
                    }
                    Collections.sort(themeOption.getColorSchemes());
                }

                if (!themeOption.getColorSchemes().isEmpty() || colorSchemes.isEmpty()) {
                    lookAndFeels.add(themeOption);
                }
            }
        }
        Collections.sort(lookAndFeels);
        return lookAndFeels;
    }

    @Override
    public List<ThemeOption> getAllLookAndFeels(Long companyId) throws ApplicationException {
        List<ThemeOption> lookAndFeels = new ArrayList<ThemeOption>();

        for (LookAndFeel themeLookAndFeel : fetchThemes(companyId)) {
            ThemeOption themeOption = createThemeOption(themeLookAndFeel);
            for (LookAndFeel csLookAndFeel : lookAndFeelDao.findColorSchemes(themeLookAndFeel.getThemeId(), companyId)) {
                ColorSchemeOption colorSchemeOption = createColorSchemeOption(csLookAndFeel);
                themeOption.addColorScheme(colorSchemeOption);
                Collections.sort(themeOption.getColorSchemes());
            }
            lookAndFeels.add(themeOption);
        }
        Collections.sort(lookAndFeels);
        return lookAndFeels;
    }

    private List<LookAndFeel> fetchThemes(long companyId) throws ApplicationException {
        synchronizationService.synchronize(companyId);
        List<LookAndFeel> result = lookAndFeelDao.getThemes(companyId);

        if (result.isEmpty()) {
            throw new ApplicationException("no-available-themes-found");
        }

        return result;
    }

    private ThemeOption createThemeOption(LookAndFeel themeLookAndFeel, LookAndFeelBinding persisted, String defaultThemeId, User user) throws ApplicationException {
        ThemeOption themeOption = createThemeOption(themeLookAndFeel);
        themeOption.setBind(themeLookAndFeel.getThemeId().equals(persisted.getLookAndFeel().getThemeId()));
        themeOption.setPortalDefault(themeLookAndFeel.getThemeId().equals(defaultThemeId));
        themeOption.setActions(permissionService.getAllowedActions(themeLookAndFeel, user));
        return themeOption;
    }

    private ThemeOption createThemeOption(LookAndFeel themeLookAndFeel) {
        Theme theme = themeLookAndFeel.getTheme();
        ThemeOption themeOption = new ThemeOption(themeLookAndFeel.getId(), theme.getName());
        themeOption.setScreenShotPath(LookAndFeelUtils.getScreenShotPath(theme));
        return themeOption;
    }

    private ColorSchemeOption createColorSchemeOption(LookAndFeel csLookAndFeel, LookAndFeelBinding persisted, String defaultColorSchemeId, User user) throws ApplicationException {
        ColorSchemeOption colorSchemeOption = createColorSchemeOption(csLookAndFeel);
        colorSchemeOption.setBind(csLookAndFeel.getColorSchemeId().equals(persisted.getLookAndFeel().getColorSchemeId()));
        colorSchemeOption.setPortalDefault(csLookAndFeel.getColorSchemeId().equals(defaultColorSchemeId));
        colorSchemeOption.setActions(permissionService.getAllowedActions(csLookAndFeel, user));
        return colorSchemeOption;
    }

    private ColorSchemeOption createColorSchemeOption(LookAndFeel csLookAndFeel) {
        ColorScheme colorScheme = csLookAndFeel.getColorScheme();
        ColorSchemeOption colorSchemeOption = new ColorSchemeOption(csLookAndFeel.getId(), colorScheme.getName());
        colorSchemeOption.setScreenShotPath(LookAndFeelUtils.getScreenShotPath(colorScheme, csLookAndFeel.getTheme()));
        return colorSchemeOption;
    }

}