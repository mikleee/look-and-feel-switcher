package com.aimprosoft.lfs.service;

import com.aimprosoft.lfs.dao.GuestLookAndFeelBindingDao;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import com.liferay.portal.util.PortalUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores guest sessions which point to guest bindings
 *
 * @author Mikhail Tkachenko
 */
@Service
public final class GuestSessionRegistry {
    private final static Logger LOGGER = Logger.getLogger(GuestSessionRegistry.class);
    private static Map<String, HttpSession> registry = new ConcurrentHashMap<String, HttpSession>();

    @Autowired
    private GuestLookAndFeelBindingDao dao;


    public void register(PortletRequest request) {
        HttpSession session = PortalUtil.getHttpServletRequest(request).getSession();
        registry.put(session.getId(), session);
        LOGGER.debug("Guest session " + session.getId() + " has been registered");
    }

    /**
     * the scheduled method
     * <p>
     * Each time when session is expired removes all guest {@link LookAndFeelBinding}
     * from the {@link GuestLookAndFeelBindingDao}
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void checkForExpiredSessions() {
        LOGGER.trace("Start checking guest sessions for expiry. " + registry.keySet().size() + " candidates have been found");
        for (String key : registry.keySet()) {
            HttpSession session = registry.get(key);
            if (isSessionExpired(session)) {
                removeSessionBindings(session);
                registry.remove(session.getId());
                LOGGER.debug("Guest session " + session.getId() + " has been removed from registry");
            }
        }
    }

    private void removeSessionBindings(HttpSession session) {
        List<Integer> ids = new ArrayList<Integer>();
        String id = session.getId();
        List<LookAndFeelBinding> all = dao.findAll();
        for (LookAndFeelBinding binding : all) {
            if (id.equals(binding.getSessionId())) {
                ids.add(binding.getId());
            }
        }

        for (Integer integer : ids) {
            dao.delete(integer);
        }
    }

    private boolean isSessionExpired(HttpSession session) {
        try {
            long lifeTimeLeft = session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000 - System.currentTimeMillis();
            if (lifeTimeLeft > 0) {
                LOGGER.trace("Guest session " + session.getId() + " is alive and could get expired in " + lifeTimeLeft / 1000 + " s");
                return false;
            } else {
                LOGGER.debug("Guest session " + session.getId() + " was expired " + (-lifeTimeLeft) + " ms ago");
                return true;
            }
        } catch (IllegalStateException e) {
            LOGGER.debug("Guest session " + session.getId() + " has been expired");
            return true;
        }
    }


}
