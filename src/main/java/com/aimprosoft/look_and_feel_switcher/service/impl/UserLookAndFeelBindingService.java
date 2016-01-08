package com.aimprosoft.look_and_feel_switcher.service.impl;

import com.aimprosoft.look_and_feel_switcher.dao.UserLookAndFeelBindingDao;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mikhail Tkachenko
 */
@Component
public class UserLookAndFeelBindingService extends AbstractLookAndFeelBindingService {

    @Autowired
    protected UserLookAndFeelBindingDao bindingDao;

    @Override
    public LookAndFeelBinding findByUserAndGroup(LookAndFeelBinding binding) {
        return bindingDao.findByUserAndGroup(binding.getUserId(), binding.getGroupId(), binding.getCompanyId());
    }

    @Override
    protected void delete(LookAndFeelBinding binding) {
        bindingDao.delete(binding.getId());
    }

    @Override
    protected LookAndFeelBinding saveOrUpdate(LookAndFeelBinding binding) {
        return bindingDao.save(binding);
    }

}