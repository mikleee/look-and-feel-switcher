package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;
import com.liferay.portal.model.User;

import java.util.List;

/**
 * service for {@link LookAndFeel}
 *
 * @author Mikhail Tkachenko
 */
public interface LookAndFeelService {

    LookAndFeelBinding NULL_BINDING = new LookAndFeelBinding() {{
        setLookAndFeel(new LookAndFeel());
    }};

    LookAndFeel find(Integer id) throws ApplicationException;

    List<ThemeOption> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault, User user) throws ApplicationException;

    List<ThemeOption> getAllLookAndFeels(Long companyId) throws ApplicationException;

}