package com.aimprosoft.look_and_feel_switcher.service;

import com.aimprosoft.look_and_feel_switcher.model.persist.ConfigKey;
import com.aimprosoft.look_and_feel_switcher.model.persist.UserConfig;

import java.util.Map;

/**
 * @author Mikhail Tkachenko created on 21.06.16 19:52
 */
public interface UserConfigService {

    UserConfig get(long userId, ConfigKey key);

    UserConfig saveOrUpdate(UserConfig userConfig);

    Map<String, Object> getSearchContainerConfig(long userId);

}
