package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Tkachenko
 */
public interface LookAndFeelService {

    LookAndFeel find(LookAndFeel lookAndFeel);

    void updateLookAndFeelsToShow(List<LookAndFeel> lookAndFeels);

    JsonResponse<Map<String, Object>> getAvailableLookAndFeels(LookAndFeelBinding fromView, LookAndFeelBinding persisted, LookAndFeel portalDefault) throws ApplicationException;

    JsonResponse<Map<String, Object>> getAllLookAndFeels(Long companyId) throws ApplicationException;
}