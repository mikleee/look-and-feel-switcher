package com.aimprosoft.lfs.dao;

import com.aimprosoft.lfs.model.persist.ConfigKey;
import com.aimprosoft.lfs.model.persist.UserConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * The data access object for {@link UserConfig}
 *
 * @author Mikhail Tkachenko
 */
public interface UserConfigDao extends CrudRepository<UserConfig, Integer> {

    @Query("select u from UserConfig u where u.userId = ?1 and u.key = ?2")
    UserConfig get(Long userId, ConfigKey key);

}