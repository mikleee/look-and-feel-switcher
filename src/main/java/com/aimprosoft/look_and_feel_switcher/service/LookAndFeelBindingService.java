package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;

/**
 * the service for {@link LookAndFeelBinding}
 *
 * @author Mikhail Tkachenko
 */
public interface LookAndFeelBindingService {

    LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding binding);

    LookAndFeelBinding applyBinding(LookAndFeelBinding model) throws ApplicationException;

    void removeBinding(LookAndFeelBinding model) throws ApplicationException;

    long deleteAll();

    long count();

}