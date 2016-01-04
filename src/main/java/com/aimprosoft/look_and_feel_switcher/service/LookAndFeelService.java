package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;

import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Tkachenko
 */
public interface LookAndFeelService {

    LookAndFeelBinding NULL_BINDING = new LookAndFeelBinding() {{
        setLookAndFeel(new LookAndFeel());
    }};

    LookAndFeel find(LookAndFeel lookAndFeel);

    void updateLookAndFeelsToShow(List<LookAndFeel> lookAndFeels);

    List<ThemeOption> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault) throws ApplicationException;

    JsonResponse<Map<String, Object>> getAllLookAndFeels(Long companyId) throws ApplicationException;
}