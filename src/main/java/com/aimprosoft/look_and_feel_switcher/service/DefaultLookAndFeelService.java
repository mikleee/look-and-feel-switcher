package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.stereotype.Service;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;

/**
 * crated by m.tkachenko on 10.10.15 17:23
 */
@Service
public class DefaultLookAndFeelService {

    public static final String DEFAULT_THEME = "LIFERAY_SHARED_lfb-default-theme";
    public static final String DEFAULT_COLOR_SCHEME = "LIFERAY_SHARED_lfb-default-color-scheme";

    public void storeLookAndFeel(HttpServletRequest request, ThemeDisplay themeDisplay) {
        request.getSession().setAttribute(DEFAULT_THEME, themeDisplay.getTheme().getThemeId());
        request.getSession().setAttribute(DEFAULT_COLOR_SCHEME, themeDisplay.getColorScheme().getColorSchemeId());
    }

    public LookAndFeel getPortalDefaultLookAndFeel(PortletRequest request) {
        LookAndFeel result = new LookAndFeel();
        result.setThemeId(getThemeId(request));
        result.setColorSchemeId(getColorSchemeId(request));
        return result;
    }

    public String getThemeId(PortletRequest request) {
        return (String) request.getPortletSession().getAttribute(DEFAULT_THEME, PortletSession.APPLICATION_SCOPE);
    }

    public String getColorSchemeId(PortletRequest request) {
        return (String) request.getPortletSession().getAttribute(DEFAULT_COLOR_SCHEME, PortletSession.APPLICATION_SCOPE);
    }

}