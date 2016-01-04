package com.aimprosoft.look_and_feel_switcher.hook;

import com.liferay.portal.kernel.portlet.DefaultFriendlyURLMapper;
import com.liferay.portal.kernel.util.WebKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * crated by m.tkachenko on 07.10.15 14:49
 */
public class FriendlyUrlMapper extends DefaultFriendlyURLMapper {

    private final static Logger LOGGER = LoggerFactory.getLogger(FriendlyUrlMapper.class);

    @Override
    public boolean isCheckMappingWithPrefix() {
        /**
         * todo
         * it should be false to avoid ".../-/..." separator
         * but there is liferay bug: look at {@link com.liferay.portlet.PortletURLImpl#generateToString}
         */
        return true;
    }

    public void populateParams(String friendlyURLPath, Map<String, String[]> parameterMap, Map<String, Object> requestContext) {
        try {
            String fullUrl = (String) ((HttpServletRequest) requestContext.get("request")).getAttribute(WebKeys.CURRENT_COMPLETE_URL);
            String normalizedUrl = fullUrl.substring(fullUrl.indexOf(friendlyURLPath));
            super.populateParams(normalizedUrl, parameterMap, requestContext);
        } catch (Exception e) {
            LOGGER.warn("Cant process friendly url: " + friendlyURLPath);
        }
    }

}