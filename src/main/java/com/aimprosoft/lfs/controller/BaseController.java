package com.aimprosoft.lfs.controller;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.view.Config;
import com.aimprosoft.lfs.service.ConfigService;
import com.aimprosoft.lfs.service.LookAndFeelPermissionService;
import com.aimprosoft.lfs.service.LookAndFeelService;
import com.aimprosoft.lfs.service.ObjectMapper;
import com.liferay.portal.kernel.util.ParamUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.util.Map;

import static com.aimprosoft.lfs.model.view.JsonResponse.error;
import static com.aimprosoft.lfs.model.view.JsonResponse.success;

/**
 * Contains the common functionality for all web controllers
 *
 * @author Mikhail Tkachenko
 */
public abstract class BaseController {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected LookAndFeelService lookAndFeelService;
    @Autowired
    protected LookAndFeelPermissionService permissionService;
    @Autowired
    private ConfigService configService;


    @ResourceMapping("getTemplate")
    public ModelAndView getTemplate(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        return new ModelAndView(ParamUtil.getString(request, "template"));
    }

    @ResourceMapping("getPaginatorConfig")
    public void getPaginatorConfig(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Map<String, Object> searchContainerConfig = configService.getSearchContainerConfig(request.getPreferences());
        objectMapper.writeValue(response, success(searchContainerConfig));
    }

    @ResourceMapping("setUserConfig")
    public void setUserConfig(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Config config = objectMapper.readValue(request, Config.class);
        configService.saveOrUpdate(config.getKey(), config.getValue(), request.getPreferences());
        objectMapper.writeValue(response, success());
    }

    @ExceptionHandler({ApplicationException.class})
    public void handleApplicationException(ApplicationException e, ResourceResponse response) throws IOException {
        logger.warn(e.getMessage(), e);
        objectMapper.writeValue(response, error(e.getMessage()));
    }


    void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        logger.error(e, e);
        objectMapper.writeValue(response, error(e.getMessage()));
    }

}