package com.aimprosoft.look_and_feel_switcher.controller;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelPermissionService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.portlet.ResourceResponse;
import java.io.IOException;

/**
 * crated by m.tkachenko on 05.01.16 17:49
 */
@Controller
public abstract class BaseController {

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected LookAndFeelService lookAndFeelService;
    @Autowired
    protected LookAndFeelPermissionService permissionService;


    @ExceptionHandler({ApplicationException.class})
    public void handleApplicationException(ApplicationException e, ResourceResponse response) throws IOException {
        logger.warn(e.getMessage(), e);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.error(e.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    public void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        logger.error(e, e);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.error(e.getMessage()));
    }

}