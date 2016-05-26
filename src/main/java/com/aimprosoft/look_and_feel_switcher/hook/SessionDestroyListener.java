package com.aimprosoft.look_and_feel_switcher.hook;

import com.aimprosoft.look_and_feel_switcher.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.utils.SpringUtils;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SessionAction;

import javax.servlet.http.HttpSession;

/**
 * crated by m.tkachenko on 07.10.15 13:49
 */
public class SessionDestroyListener extends SessionAction {

    @Override
    public void run(HttpSession session) throws ActionException {
        GuestLookAndFeelBindingService bindingService = SpringUtils.getBean(GuestLookAndFeelBindingService.class);
        bindingService.removeBindings(session.getId());
    }

}