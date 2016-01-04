package com.aimprosoft.look_and_feel_switcher.dao;

import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Mikhail Tkachenko
 */
public interface UserLookAndFeelBindingDao extends CrudRepository<LookAndFeelBinding, Integer> {

    @Query("from LookAndFeelBinding b where b.userId = ?1 and b.groupId = ?2 and b.companyId = ?3")
    LookAndFeelBinding findByUserAndGroup(Long userId, Long groupId, Long companyId);

}