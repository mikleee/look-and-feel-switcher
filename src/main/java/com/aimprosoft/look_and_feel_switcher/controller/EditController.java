package com.aimprosoft.look_and_feel_switcher.controller;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;
import com.aimprosoft.look_and_feel_switcher.service.DefaultLookAndFeelService;
import com.aimprosoft.look_and_feel_switcher.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.utils.LookAndFeelsTypeReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.util.List;

import static com.aimprosoft.look_and_feel_switcher.utils.Utils.getThemeDisplay;

/**
 * @author Mikhail Tkachenko
 */
@Controller
@RequestMapping(value = "EDIT")
public class EditController extends BaseController {

    @RenderMapping
    public ModelAndView showAdministrationPage(RenderRequest request, ModelMap map) throws SystemException, PortalException {
        ThemeDisplay themeDisplay = getThemeDisplay(request);
        return new ModelAndView("administration", map).addObject("themeDisplay", themeDisplay);
    }

    @ResourceMapping("getLookAndFeelMap")
    public void getLookAndFellMap(ResourceRequest request, ResourceResponse response, Long companyId) throws ApplicationException, IOException {
        List<ThemeOption> lookAndFeels = lookAndFeelService.getAllLookAndFeels(companyId);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.success().put("lookAndFeels", lookAndFeels));
    }

    @ResourceMapping("applyLookAndFeelsToShow") //todo collect request
    public void applyLookAndFeelsToShow(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        List<LookAndFeel> lookAndFeels = objectMapper.readValue(request.getPortletInputStream(), new LookAndFeelsTypeReference());
        lookAndFeelService.updateLookAndFeelsToShow(lookAndFeels);
    }

}