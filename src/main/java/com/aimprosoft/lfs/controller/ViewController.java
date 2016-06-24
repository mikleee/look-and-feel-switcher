package com.aimprosoft.lfs.controller;

import com.aimprosoft.lfs.exception.ApplicationException;
import com.aimprosoft.lfs.model.persist.LookAndFeel;
import com.aimprosoft.lfs.model.persist.LookAndFeelBinding;
import com.aimprosoft.lfs.model.view.ThemeOption;
import com.aimprosoft.lfs.service.DefaultLookAndFeelService;
import com.aimprosoft.lfs.service.GuestSessionRegistry;
import com.aimprosoft.lfs.service.LookAndFeelBindingService;
import com.aimprosoft.lfs.service.LookAndFeelService;
import com.aimprosoft.lfs.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.lfs.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.lfs.utils.Utils;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;
import java.util.List;

import static com.aimprosoft.lfs.model.view.JsonResponse.success;
import static com.aimprosoft.lfs.utils.Utils.getThemeDisplay;

/**
 * The web controller which handles theme switcher portlet requests in the VIEW mode
 *
 * @author Mikhail Tkachenko
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewController extends BaseController {
    @Autowired
    private UserLookAndFeelBindingService userThemesBindingService;
    @Autowired
    private GuestLookAndFeelBindingService guestThemeBindingService;
    @Autowired
    private DefaultLookAndFeelService defaultLookAndFeelService;
    @Autowired
    private GuestSessionRegistry guestSessionRegistry;


    @RenderMapping
    public ModelAndView renderThemes(RenderRequest request, ModelMap map) throws SystemException, PortalException, IOException {
        ThemeDisplay themeDisplay = getThemeDisplay(request);

        LookAndFeelBinding emptyBinding = new LookAndFeelBinding(themeDisplay);
        LookAndFeelBinding currentBinding = getLookAndFeelBindingService(request).findByUserAndGroup(emptyBinding);

        return new ModelAndView("view/select-look-and-feel", map)
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
        LookAndFeelBinding model = objectMapper.readValue(request, LookAndFeelBinding.class);
        lookAndFeelBindingService.applyBinding(model);
        if (Utils.isGuest(request)) {
            guestSessionRegistry.register(request);
        }
        objectMapper.writeValue(response, success());
    }

    @ResourceMapping(value = "initLookAndFeel")
    public void initLookAndFeel(ResourceRequest request, ResourceResponse response, LookAndFeelBinding fromView) throws ApplicationException, IOException {
        LookAndFeel portalDefault = defaultLookAndFeelService.getPortalDefaultLookAndFeel(request);
        LookAndFeelBinding persisted = getLookAndFeelBindingService(request).findByUserAndGroup(fromView);
        if (persisted == null) {
            persisted = LookAndFeelService.NULL_BINDING;
        }

        List<ThemeOption> lookAndFeels = lookAndFeelService.getAvailableLookAndFeels(fromView, persisted, portalDefault, getThemeDisplay(request).getUser());

        objectMapper.writeValue(response, success()
                .put("currentBinding", persisted.getId())
                .put("lookAndFeels", lookAndFeels));
    }

    private LookAndFeelBindingService getLookAndFeelBindingService(PortletRequest request) {
        return Utils.isGuest(request) ? guestThemeBindingService : userThemesBindingService;
    }

    @ExceptionHandler({Exception.class})
    public void handleApplicationError(Exception e, ResourceResponse response) throws IOException {
        super.handleApplicationError(e, response);
    }
}