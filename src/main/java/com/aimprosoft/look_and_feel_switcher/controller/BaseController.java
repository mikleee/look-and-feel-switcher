package com.aimprosoft.look_and_feel_switcher.controller;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelPermissionService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelService;
import com.liferay.portal.kernel.util.ParamUtil;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;

/**
 * Contains the common functionality for all web controllers
 *
 * @author Mikhail Tkachenko
 */
public abstract class BaseController {

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected LookAndFeelService lookAndFeelService;
    @Autowired
    protected LookAndFeelPermissionService permissionService;


    @ResourceMapping("getTemplate")
    public ModelAndView getTemplate(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        return new ModelAndView(ParamUtil.getString(request, "template"));
    }

    @ExceptionHandler({ApplicationException.class})
    public void handleApplicationException(ApplicationException e, ResourceResponse response) throws IOException {
        logger.warn(e.getMessage(), e);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.error(e.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        logger.error(e, e);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.error(e.getMessage()));
    }

}