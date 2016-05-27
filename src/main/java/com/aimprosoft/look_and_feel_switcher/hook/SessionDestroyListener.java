package com.aimprosoft.look_and_feel_switcher.hook;

import com.aimprosoft.look_and_feel_switcher.dao.GuestLookAndFeelBindingDao;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.utils.SpringUtils;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SessionAction;

import javax.servlet.http.HttpSession;

/**
 * The implementation of the {@link SessionAction}.
 * Each time when session is expired removes all guest {@link LookAndFeelBinding}
 * from the {@link GuestLookAndFeelBindingDao}
 *
 * @author Mikhail Tkachenko
 */
public class SessionDestroyListener extends SessionAction {

    @Override
    public void run(HttpSession session) throws ActionException {
        GuestLookAndFeelBindingService bindingService = SpringUtils.getBean(GuestLookAndFeelBindingService.class);
        bindingService.removeBindings(session.getId());
    }

}