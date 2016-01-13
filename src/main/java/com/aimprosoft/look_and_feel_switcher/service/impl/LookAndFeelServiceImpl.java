package com.aimprosoft.look_and_feel_switcher.service.impl;

import com.aimprosoft.look_and_feel_switcher.dao.LookAndFeelDao;
import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.ColorSchemeOption;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelPermissionService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.utils.LookAndFeelUtils;
import com.aimprosoft.look_and_feel_switcher.utils.Timer;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Theme;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mikhail Tkachenko
 */
@Component
public class LookAndFeelServiceImpl implements LookAndFeelService, InitializingBean {

    private final static Logger LOGGER = Logger.getLogger(LookAndFeelServiceImpl.class);

    private static final Set<String> SKIPPED_THEMES = new HashSet<String>() {{
        add("controlpanel");
        add("mobile");
    }};

    @Autowired
    private LookAndFeelDao lookAndFeelDao;
    @Autowired
    private LookAndFeelPermissionService permissionService;


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            for (Company company : CompanyLocalServiceUtil.getCompanies()) {
                synchronize(company.getCompanyId());
            }
        } catch (SystemException e) {
            throw new ApplicationException("Synchronize failed");
        }
    }

    private void synchronize(long companyId) throws ApplicationException {
        Timer timer = new Timer();
        List<Integer> ids = lookAndFeelDao.getIds(companyId);

        for (Theme theme : ThemeLocalServiceUtil.getThemes(companyId)) {
            if (SKIPPED_THEMES.contains(theme.getThemeId())) {
                continue;
            }

            LookAndFeel lookAndFeelTheme = lookAndFeelDao.findTheme(theme.getThemeId(), companyId);

            if (lookAndFeelTheme == null) {
                lookAndFeelTheme = new LookAndFeel(theme, companyId);
                lookAndFeelDao.save(lookAndFeelTheme);
                permissionService.addDefaultPermissions(lookAndFeelTheme);
                LOGGER.info("Look and feels " + lookAndFeelTheme + " has been registered");
            } else {
                ids.remove(lookAndFeelTheme.getId());
            }

            for (ColorScheme colorScheme : theme.getColorSchemes()) {
                LookAndFeel lookAndFeelColorScheme = lookAndFeelDao.findColorScheme(theme.getThemeId(), colorScheme.getColorSchemeId(), companyId);
                if (lookAndFeelColorScheme == null) {
                    lookAndFeelColorScheme = new LookAndFeel(theme, colorScheme, companyId);
                    lookAndFeelDao.save(lookAndFeelColorScheme);
                    permissionService.addDefaultPermissions(lookAndFeelColorScheme);
                    LOGGER.info("Look and feels " + lookAndFeelColorScheme + " has been registered");
                } else {
                    ids.remove(lookAndFeelColorScheme.getId());
                }
            }
        }

        if (!ids.isEmpty()) { //todo remove permissions
            for (Integer id : ids) {
                lookAndFeelDao.delete(id);
                permissionService.deletePermissions(companyId, id.toString());
            }
            LOGGER.info("Look and feels " + ids + " have been removed because they had not been found in the portal registry");
        }
        LOGGER.debug("Look and feel synchronizing for " + companyId + " finished in " + timer.getSeconds() + " sec.");
    }

    public List<LookAndFeel> findAllThemes(long companyId) throws ApplicationException {
        synchronize(companyId);
        List<LookAndFeel> result = new ArrayList<LookAndFeel>();

        for (LookAndFeel displayedTheme : lookAndFeelDao.findAllThemes(companyId)) {
            result.add(displayedTheme);
        }

        if (result.isEmpty()) {
            throw new ApplicationException("no-available-themes-found");
        }

        return result;
    }

    @Override
    public LookAndFeel find(LookAndFeel lookAndFeel) throws ApplicationException {
        LookAndFeel result = lookAndFeelDao.findById(lookAndFeel.getId());
        if (result == null) {
            throw new ApplicationException("requested-look-and-feel-not-available");
        }
        return result;
    }

    @Override
    public List<ThemeOption> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault, User user) throws ApplicationException {
        List<ThemeOption> lookAndFeels = new ArrayList<ThemeOption>();

        long companyId = fromView.getLookAndFeel().getCompanyId();
        for (LookAndFeel themeLookAndFeel : findAllThemes(companyId)) {
            ThemeOption themeOption = createThemeOption(themeLookAndFeel, persisted, portalDefault.getThemeId(), user);
            if (themeOption.isViewActionPermitted()) {
                lookAndFeels.add(themeOption);

                for (LookAndFeel csLookAndFeel : lookAndFeelDao.findColorSchemes(themeLookAndFeel.getThemeId(), companyId)) {
                    ColorSchemeOption colorSchemeOption = createColorSchemeOption(csLookAndFeel, persisted, portalDefault.getColorSchemeId(), user);
                    if (colorSchemeOption.isViewActionPermitted()) {
                        themeOption.addColorScheme(colorSchemeOption);
                    }
                }
            }
        }
        return lookAndFeels;
    }

    @Override
    public List<ThemeOption> getAllLookAndFeels(Long companyId) throws ApplicationException {
        synchronize(companyId);
        List<ThemeOption> lookAndFeels = new ArrayList<ThemeOption>();

        for (LookAndFeel themeLookAndFeel : lookAndFeelDao.findThemes(companyId)) {
            ThemeOption themeOption = createThemeOption(themeLookAndFeel);
            for (LookAndFeel csLookAndFeel : lookAndFeelDao.findColorSchemes(themeLookAndFeel.getThemeId(), companyId)) {
                ColorSchemeOption colorSchemeOption = createColorSchemeOption(csLookAndFeel);
                themeOption.addColorScheme(colorSchemeOption);
            }
            lookAndFeels.add(themeOption);
        }
        return lookAndFeels;
    }

    private ThemeOption createThemeOption(LookAndFeel themeLookAndFeel, LookAndFeelBinding persisted, String defaultThemeId, User user) throws ApplicationException {
        ThemeOption themeOption = createThemeOption(themeLookAndFeel);
        themeOption.setBind(themeOption.getId().equals(persisted.getLookAndFeel().getThemeId()));
        themeOption.setPortalDefault(themeOption.getId().equals(defaultThemeId));
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
        colorSchemeOption.setBind(colorSchemeOption.getId().equals(persisted.getLookAndFeel().getColorSchemeId()));
        colorSchemeOption.setPortalDefault(colorSchemeOption.getId().equals(defaultColorSchemeId));
        colorSchemeOption.setActions(permissionService.getAllowedActions(csLookAndFeel, user));
        return colorSchemeOption;
    }

    private ColorSchemeOption createColorSchemeOption(LookAndFeel csLookAndFeel) {
        ColorScheme colorScheme = csLookAndFeel.getColorScheme();
        ColorSchemeOption colorSchemeOption = new ColorSchemeOption(csLookAndFeel.getId(), colorScheme.getName());
        colorSchemeOption.setScreenShotPath(LookAndFeelUtils.getScreenShotPath(colorScheme));
        return colorSchemeOption;
    }

}