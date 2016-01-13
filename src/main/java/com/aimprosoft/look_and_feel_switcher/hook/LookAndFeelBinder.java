package com.aimprosoft.look_and_feel_switcher.hook;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.service.DefaultLookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.utils.NullColorScheme;
import com.aimprosoft.look_and_feel_switcher.utils.SpringUtils;
import com.aimprosoft.look_and_feel_switcher.utils.Utils;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.theme.ThemeDisplay;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aimprosoft.look_and_feel_switcher.utils.Utils.*;
import static com.liferay.portal.kernel.util.WebKeys.THEME;
import static com.liferay.portal.kernel.util.WebKeys.THEME_DISPLAY;
import static com.liferay.portal.model.GroupConstants.CONTROL_PANEL;
import static com.liferay.portal.util.WebKeys.COLOR_SCHEME;

/**
 * @author Mikhail Tkachenko
 */
public class LookAndFeelBinder extends Action {

    private final static Logger LOGGER = Logger.getLogger(LookAndFeelBinder.class);
    private static DefaultLookAndFeelService defaultLookAndFeelService;

    public LookAndFeelBinder() {
        if (defaultLookAndFeelService == null) {
            defaultLookAndFeelService = SpringUtils.getBean(DefaultLookAndFeelService.class);
        }
    }

    @Override
    @Transactional
    public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        ThemeDisplay themeDisplay = getThemeDisplay(request);

        if (isAjax(request) || isControlPanel(themeDisplay)) {
            return;
        }

        defaultLookAndFeelService.storeLookAndFeel(request, themeDisplay);
        LookAndFeelBinding model = getBindModel(themeDisplay);

        if (model != null) {
            try {
                model.init();
                registerThemeDisplay(model, themeDisplay, request);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private LookAndFeelBinding getBindModel(ThemeDisplay themeDisplay) {
        Class<? extends LookAndFeelBindingService> cls = Utils.isGuest(themeDisplay) ? GuestLookAndFeelBindingService.class : UserLookAndFeelBindingService.class;
        LookAndFeelBinding model = new LookAndFeelBinding(themeDisplay);
        return SpringUtils.getBean(cls).findByUserAndGroup(model);
    }


    private boolean isAjax(HttpServletRequest request) {
        String lifecycle = request.getParameter(LIFE_CYCLE);
        return lifecycle != null && lifecycle.equals(RESOURCE_PHASE);
    }


    private boolean isControlPanel(ThemeDisplay themeDisplay) {
        try {
            String groupName = themeDisplay.getLayout().getGroup().getName();
            return groupName.equals(CONTROL_PANEL);
        } catch (Exception e) {
            LOGGER.debug(e, e);
            return true;
        }
    }

    private void registerThemeDisplay(LookAndFeelBinding binding, ThemeDisplay themeDisplay, HttpServletRequest request) {
        LookAndFeel lookAndFeel = binding.getLookAndFeel();
        ColorScheme colorScheme = lookAndFeel.getColorScheme() != null ? lookAndFeel.getColorScheme() : new NullColorScheme();

        themeDisplay.setLookAndFeel(lookAndFeel.getTheme(), colorScheme);

        request.setAttribute(THEME, lookAndFeel.getTheme());
        request.setAttribute(COLOR_SCHEME, colorScheme);
        request.setAttribute(THEME_DISPLAY, themeDisplay);
    }

}