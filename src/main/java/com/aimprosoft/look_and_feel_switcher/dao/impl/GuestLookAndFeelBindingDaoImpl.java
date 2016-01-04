package com.aimprosoft.look_and_feel_switcher.dao.impl;

import com.aimprosoft.look_and_feel_switcher.dao.GuestLookAndFeelBindingDao;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mikhail Tkachenko
 */
@Component
public class GuestLookAndFeelBindingDaoImpl implements GuestLookAndFeelBindingDao {

    private static int idSequence = 0;
    private static Map<Integer, LookAndFeelBinding> dataSource = new ConcurrentHashMap<Integer, LookAndFeelBinding>();

    @Override
    public List<LookAndFeelBinding> findAll() {
        return (List<LookAndFeelBinding>) dataSource.values(); //todo
    }

    @Override
    public LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding model) {
        long userId = model.getUserId();
        long groupId = model.getGroupId();
        String sessionId = model.getSessionId();
        long companyId = model.getCompanyId();

        return findByUserAndGroup(userId, groupId, companyId, sessionId);
    }

    @Override
    public LookAndFeelBinding save(LookAndFeelBinding model) {
        LookAndFeelBinding persisted = findByUserAndGroup(model.getUserId(), model.getGroupId(), model.getCompanyId(), model.getSessionId());
        model.setId(persisted == null ? ++idSequence : persisted.getId());
        return dataSource.put(model.getId(), model);
    }

    @Override
    public void delete(Integer id) {
        dataSource.remove(id);
    }

    private LookAndFeelBinding findByUserAndGroup(long userId, long groupId, long companyId, String sessionId) {
        if (sessionId == null) {
            return null;
        }

        for (LookAndFeelBinding model : dataSource.values()) {
            if (model.getUserId() == userId && model.getGroupId() == groupId
                    && sessionId.equals(model.getSessionId()) && model.getCompanyId().equals(companyId)) {
                return model;
            }
        }
        return null;
    }

}