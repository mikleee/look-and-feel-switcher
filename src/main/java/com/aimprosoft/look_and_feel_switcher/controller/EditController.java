package com.aimprosoft.look_and_feel_switcher.controller;

import com.aimprosoft.look_and_feel_switcher.exception.ApplicationException;
import com.aimprosoft.look_and_feel_switcher.model.persist.LookAndFeel;
import com.aimprosoft.look_and_feel_switcher.model.view.JsonResponse;
import com.aimprosoft.look_and_feel_switcher.model.view.ResourcePermissions;
import com.aimprosoft.look_and_feel_switcher.model.view.ThemeOption;
import com.aimprosoft.look_and_feel_switcher.service.impl.GuestLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.service.impl.UserLookAndFeelBindingService;
import com.aimprosoft.look_and_feel_switcher.utils.ResourcePermissionTypeReference;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aimprosoft.look_and_feel_switcher.utils.Utils.getCompanyId;
import static com.aimprosoft.look_and_feel_switcher.utils.Utils.getThemeDisplay;

/**
 * The web controller which handles theme switcher portlet requests in the EDIT mode
 *
 * @author Mikhail Tkachenko
 */
@Controller
@RequestMapping(value = "EDIT")
public class EditController extends BaseController {

    @Autowired
    private GuestLookAndFeelBindingService guestLookAndFeelBindingService;
    @Autowired
    private UserLookAndFeelBindingService userLookAndFeelBindingService;

    @RenderMapping
    public ModelAndView renderPreferences(RenderRequest request, ModelMap map) throws SystemException, PortalException, ApplicationException {
        ThemeDisplay themeDisplay = getThemeDisplay(request);
        return new ModelAndView("edit/preferences", map).addObject("themeDisplay", themeDisplay);
    }

    @ResourceMapping("getLookAndFeelMap")
    public void getLookAndFellMap(ResourceRequest request, ResourceResponse response, Long companyId) throws ApplicationException, IOException {
        List<ThemeOption> lookAndFeels = lookAndFeelService.getAllLookAndFeels(companyId);
        objectMapper.writeValue(response.getPortletOutputStream(), JsonResponse.success().put("lookAndFeels", lookAndFeels));
    }

    @ResourceMapping("fetchPermissions")
    public void fetchPermissions(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        LookAndFeel model = objectMapper.readValue(request.getPortletInputStream(), LookAndFeel.class);
        writePermissions(request, response, model.getId());
    }

    @ResourceMapping("applyPermissions")
    public void applyPermissions(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        ResourcePermissions resourcePermissions = objectMapper.readValue(request.getPortletInputStream(), new ResourcePermissionTypeReference());
        permissionService.applyPermissions(resourcePermissions, getCompanyId(request));
        writePermissions(request, response, resourcePermissions.getId());
    }

    @ResourceMapping("setDefaultPermissions")
    public void setDefaultPermissions(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        ResourcePermissions resourcePermissions = objectMapper.readValue(request.getPortletInputStream(), new ResourcePermissionTypeReference());
        LookAndFeel lookAndFeel = lookAndFeelService.find(resourcePermissions.getId());
        permissionService.addDefaultPermissions(lookAndFeel);
        writePermissions(request, response, resourcePermissions.getId());
    }

    @ResourceMapping("bindingsStatUrl")
    public void bindingsStat(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        Map<String, Object> guest = new HashMap<String, Object>();
        guest.put("count", guestLookAndFeelBindingService.count());

        Map<String, Object> user = new HashMap<String, Object>();
        user.put("count", userLookAndFeelBindingService.count());

        objectMapper.writeValue(response.getWriter(), JsonResponse.success().put("guest", guest).put("user", user));
    }

    @ResourceMapping("removeAllBindings")
    public void removeAllBindings(ResourceRequest request, ResourceResponse response) throws ApplicationException, IOException {
        guestLookAndFeelBindingService.deleteAll();
        userLookAndFeelBindingService.deleteAll();
        bindingsStat(request, response);
    }


    private void writePermissions(ResourceRequest request, ResourceResponse response, Integer id) throws ApplicationException, IOException {
        ResourcePermissions permissions = permissionService.getPermissions(getThemeDisplay(request).getCompanyId(), id);
        objectMapper.writeValue(response.getWriter(), JsonResponse.success().put("permissions", permissions));
    }

}