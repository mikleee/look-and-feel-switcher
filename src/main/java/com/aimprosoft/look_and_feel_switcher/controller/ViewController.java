package com.aimprosoft.look_and_feel_switcher.controller;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeelBinding;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;
import com.aimprosoft.look_and_feel_switcher.service.DefaultLookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.service.LookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.utils.Utils;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.theme.ThemeDisplay;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.util.Map;

import static com.aimprosoft.look_and_feel_switcher.utils.Utils.getThemeDisplay;

/**
 * @author Mikhail Tkachenko
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewController {

    private final static Logger LOGGER = Logger.getLogger(ViewController.class);

    @Autowired
    private UserLookAndFeelBindingService userThemesBindingService;
    @Autowired
    private GuestLookAndFeelBindingService guestThemeBindingService;
    @Autowired
    private LookAndFeelService lookAndFeelService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DefaultLookAndFeelService defaultLookAndFeelService;


    @RenderMapping
    public ModelAndView showThemes(RenderRequest request, ModelMap map) throws SystemException, PortalException, IOException {
        ThemeDisplay themeDisplay = getThemeDisplay(request);

        LookAndFeelBinding emptyBinding = new LookAndFeelBinding(themeDisplay);
        LookAndFeelBinding currentBinding = getLookAndFeelBindingService(request).findByUserAndGroup(emptyBinding);

        return new ModelAndView("select-look-and-feel", map)
                .addObject("themeDisplay", themeDisplay)
                .addObject("lookAndFeelBinding", objectMapper.writeValueAsString(currentBinding != null ? currentBinding : emptyBinding));
    }

    @ActionMapping(params = "action=resetBinding")
    public void resetBinding(ActionRequest request, ActionResponse response, String redirectURL, LookAndFeelBinding binding) throws ApplicationException, IOException {
        LookAndFeelBindingService lookAndFeelBindingService = getLookAndFeelBindingService(request);
        lookAndFeelBindingService.removeBinding(binding);
        response.sendRedirect(redirectURL);
    }

    @ResourceMapping(value = "applyBinding")
    public void applyBinding(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        LookAndFeelBindingService lookAndFeelBindingService = getLookAndFeelBindingService(request);
        LookAndFeelBinding model = objectMapper.readValue(request.getPortletInputStream(), LookAndFeelBinding.class);
        lookAndFeelBindingService.applyBinding(model);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.success(null));
    }

    @ResourceMapping(value = "initLookAndFeel")
    public void initLookAndFeel(ResourceRequest request, ResourceResponse response, LookAndFeelBinding fromView) throws ApplicationException, IOException {
        LookAndFeel portalDefault = defaultLookAndFeelService.getPortalDefaultLookAndFeel(request);
        LookAndFeelBinding persisted = getLookAndFeelBindingService(request).findByUserAndGroup(fromView);
        JsonResponse<Map<String, Object>> jsonResponse = lookAndFeelService.getAvailableLookAndFeels(fromView, persisted, portalDefault);
        objectMapper.writeValue(response.getPortletOutputStream(), jsonResponse);
    }

    private LookAndFeelBindingService getLookAndFeelBindingService(PortletRequest request) {
        return Utils.isGuest(request) ? guestThemeBindingService : userThemesBindingService;
    }

    @ExceptionHandler({ApplicationException.class})
    public void handleApplicationException(ApplicationException e, ResourceResponse response) throws IOException {
        LOGGER.warn(e.getMessage());
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.error(e.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    public void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        LOGGER.error(e, e);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.error(e.getMessage()));
    }

}