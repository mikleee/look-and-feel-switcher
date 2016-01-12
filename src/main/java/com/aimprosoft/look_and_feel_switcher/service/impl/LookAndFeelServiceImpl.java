package com.aimprosoft.look_and_feel_switcher.service.impl;

import com.aimprosoft.look_and_feel_switcher.dao.LookAndFeelDao;
import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.Action;
import com.aimprosoft.look_and_feel_switcher.model.view.LookAndFeelOption;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelPermissionService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.utils.LookAndFeelUtils;
import com.aimprosoft.look_and_feel_switcher.utils.Utils;
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
            throw new ApplicationException();
        }
    }

    private void synchronize(long companyId) throws ApplicationException {
        long start = System.currentTimeMillis();
        List<Integer> ids = lookAndFeelDao.getIds(companyId);

        for (Theme theme : ThemeLocalServiceUtil.getThemes(companyId)) {
            if (SKIPPED_THEMES.contains(theme.getThemeId())) {
                continue;
            }

            LookAndFeel lookAndFeelTheme = lookAndFeelDao.findTheme(theme.getThemeId(), companyId);

            if (lookAndFeelTheme == null) {
                lookAndFeelTheme = new LookAndFeel(theme, companyId);
                lookAndFeelDao.save(lookAndFeelTheme);
                permissionService.addAllPermissions(lookAndFeelTheme);
                LOGGER.info("Look and feels " + lookAndFeelTheme + " has been registered");
            } else {
                ids.remove(lookAndFeelTheme.getId());
            }

            for (ColorScheme colorScheme : theme.getColorSchemes()) {
                LookAndFeel lookAndFeelColorScheme = lookAndFeelDao.findColorScheme(theme.getThemeId(), colorScheme.getColorSchemeId(), companyId);
                if (lookAndFeelColorScheme == null) {
                    lookAndFeelColorScheme = new LookAndFeel(theme, colorScheme, companyId);
                    lookAndFeelDao.save(lookAndFeelColorScheme);
                    permissionService.addAllPermissions(lookAndFeelColorScheme);
                    LOGGER.info("Look and feels " + lookAndFeelColorScheme + " has been registered");
                } else {
                    ids.remove(lookAndFeelColorScheme.getId());
                }
            }
        }

        if (!ids.isEmpty()) { //todo remove permissions
            for (Integer id : ids) {
                lookAndFeelDao.delete(id);
            }
            LOGGER.info("Look and feels " + ids + " have been removed because they had not been found in the portal registry");
        }
        LOGGER.debug("Look and feel synchronizing for " + companyId + " finished in " + Utils.spentTime(start));
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
    public LookAndFeel find(LookAndFeel lookAndFeel) {
        String themeId = lookAndFeel.getThemeId(), colorSchemeId = lookAndFeel.getColorSchemeId();
        long companyId = lookAndFeel.getCompanyId();
        return colorSchemeId != null ?
                lookAndFeelDao.findColorScheme(themeId, colorSchemeId, companyId) : lookAndFeelDao.findTheme(themeId, companyId);
    }

    @Override
    public List<ThemeOption> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault, User user) throws ApplicationException {
        List<ThemeOption> lookAndFeels = new ArrayList<ThemeOption>();

        long companyId = fromView.getLookAndFeel().getCompanyId();
        for (LookAndFeel themeLookAndFeel : findAllThemes(companyId)) {
            ThemeOption themeOption = createThemeOption(themeLookAndFeel, persisted, portalDefault.getThemeId(), user);
            if (themeOption.getAllowedActions().contains(Action.VIEW)) {
                lookAndFeels.add(themeOption);

                for (LookAndFeel csLookAndFeel : lookAndFeelDao.findColorSchemes(themeLookAndFeel.getThemeId(), companyId)) {
                    LookAndFeelOption colorSchemeOption = createColorSchemeOption(csLookAndFeel, persisted, portalDefault.getColorSchemeId(), user);
                    if (colorSchemeOption.getAllowedActions().contains(Action.VIEW)) {
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
                LookAndFeelOption colorSchemeOption = createColorSchemeOption(csLookAndFeel);
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
        themeOption.setAllowedActions(permissionService.getAllowedActions(themeLookAndFeel, user));
        return themeOption;
    }

    private ThemeOption createThemeOption(LookAndFeel themeLookAndFeel) {
        Theme theme = themeLookAndFeel.getTheme();
        ThemeOption themeOption = new ThemeOption(themeLookAndFeel.getId(), theme.getName());
        themeOption.setScreenShotPath(LookAndFeelUtils.getScreenShotPath(theme));
        return themeOption;
    }

    private LookAndFeelOption createColorSchemeOption(LookAndFeel csLookAndFeel, LookAndFeelBinding persisted, String defaultColorSchemeId, User user) throws ApplicationException {
        LookAndFeelOption colorSchemeOption = createColorSchemeOption(csLookAndFeel);
        colorSchemeOption.setBind(colorSchemeOption.getId().equals(persisted.getLookAndFeel().getColorSchemeId()));
        colorSchemeOption.setPortalDefault(colorSchemeOption.getId().equals(defaultColorSchemeId));
        colorSchemeOption.setAllowedActions(permissionService.getAllowedActions(csLookAndFeel, user));
        return colorSchemeOption;
    }

    private LookAndFeelOption createColorSchemeOption(LookAndFeel csLookAndFeel) {
        ColorScheme colorScheme = csLookAndFeel.getColorScheme();
        LookAndFeelOption colorSchemeOption = new LookAndFeelOption(csLookAndFeel.getId(), colorScheme.getName());
        colorSchemeOption.setScreenShotPath(LookAndFeelUtils.getScreenShotPath(colorScheme));
        return colorSchemeOption;
    }

}