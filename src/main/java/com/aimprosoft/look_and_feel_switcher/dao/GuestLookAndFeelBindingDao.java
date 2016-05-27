package com.aimprosoft.look_and_feel_switcher.dao;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;

import java.util.List;

/**
 * The data access object for guests' {@link LookAndFeelBinding}
 *
 * @author Mikhail Tkachenko
 */
public interface GuestLookAndFeelBindingDao {

    List<LookAndFeelBinding> findAll();

    LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding model);

    LookAndFeelBinding save(LookAndFeelBinding model);

    void delete(Integer id);

    long deleteAll();

    long count();

}
