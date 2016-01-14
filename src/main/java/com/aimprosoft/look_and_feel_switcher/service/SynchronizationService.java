package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.dao.LookAndFeelDao;
import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelType;
import com.aimprosoft.look_and_feel_switcher.utils.Timer;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.ThemeLocalServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * crated by m.tkachenko on 14.01.16 11:31
 */
@Service
public class SynchronizationService implements InitializingBean {

    private final static Logger LOGGER = Logger.getLogger(SynchronizationService.class);

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

    public void synchronize(long companyId) throws ApplicationException {
        Timer timer = new Timer();
        List<Integer> ids = lookAndFeelDao.getIds(companyId);

        for (Theme theme : ThemeLocalServiceUtil.getThemes(companyId)) {
            if (SKIPPED_THEMES.contains(theme.getThemeId())) {
                continue;
            }

            LookAndFeel lookAndFeelTheme = lookAndFeelDao.findTheme(theme.getThemeId(), companyId);

            if (lookAndFeelTheme == null) {
                registerTheme(theme, companyId);
            } else {
                ids.remove(lookAndFeelTheme.getId());
            }

            for (ColorScheme colorScheme : theme.getColorSchemes()) {
                LookAndFeel lookAndFeelColorScheme = lookAndFeelDao.findColorScheme(theme.getThemeId(), colorScheme.getColorSchemeId(), companyId);
                if (lookAndFeelColorScheme == null) {
                    registerColorScheme(theme, colorScheme, companyId);
                } else {
                    ids.remove(lookAndFeelColorScheme.getId());
                }
            }
        }

        if (!ids.isEmpty()) {
            for (Integer id : ids) {
                lookAndFeelDao.delete(id);
                permissionService.deletePermissions(companyId, id.toString());
            }
            LOGGER.info("Look and feels " + ids + " have been removed because they had not been found in the portal registry");
        }
        LOGGER.debug("Look and feel synchronizing for " + companyId + " finished in " + timer.getSeconds() + " sec.");
    }

    private LookAndFeel registerTheme(Theme theme, long companyId) throws ApplicationException {
        LookAndFeel result = new LookAndFeel(theme, companyId);
        result.setType(LookAndFeelType.THEME);
        lookAndFeelDao.save(result);
        permissionService.addDefaultPermissions(result);
        LOGGER.info("Look and feels " + result + " has been registered");
        return result;
    }

    private LookAndFeel registerColorScheme(Theme theme, ColorScheme colorScheme, long companyId) throws ApplicationException {
        LookAndFeel result = new LookAndFeel(theme, colorScheme, companyId);
        result.setType(LookAndFeelType.COLOR_SCHEME);
        lookAndFeelDao.save(result);
        permissionService.addDefaultPermissions(result);
        LOGGER.info("Look and feels " + result + " has been registered");
        return result;
    }

}