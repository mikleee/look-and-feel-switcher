package com.aimprosoft.look_and_feel_switcher.dao;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;

import java.util.List;

/**
 * crated by m.tkachenko on 12.10.15 12:38
 */
public interface GuestLookAndFeelBindingDao {

    List<LookAndFeelBinding> findAll();

    LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding model);

    LookAndFeelBinding save(LookAndFeelBinding model);

    void delete(Integer id);

}
