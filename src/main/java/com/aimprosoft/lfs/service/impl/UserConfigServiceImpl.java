package com.aimprosoft.lfs.service.impl;

import com.aimprosoft.lfs.dao.UserConfigDao;
import com.aimprosoft.lfs.model.persist.ConfigKey;
import com.aimprosoft.lfs.model.persist.UserConfig;
import com.aimprosoft.lfs.service.UserConfigService;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Tkachenko
 */
@Service
public class UserConfigServiceImpl implements UserConfigService {
    @Autowired
    private UserConfigDao dao;

    @Override
    public UserConfig get(long userId, ConfigKey key) {
        return dao.get(userId, key);
    }

    @Override
    public UserConfig saveOrUpdate(UserConfig userConfig) {
        return dao.save(userConfig);
    }

    public Map<String, Object> getSearchContainerConfig(long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("delta", PropsUtil.get(PropsKeys.SEARCH_CONTAINER_PAGE_DEFAULT_DELTA));
        map.put("deltas", PropsUtil.get(PropsKeys.SEARCH_CONTAINER_PAGE_DELTA_VALUES).split(","));
        return map;
    }


}