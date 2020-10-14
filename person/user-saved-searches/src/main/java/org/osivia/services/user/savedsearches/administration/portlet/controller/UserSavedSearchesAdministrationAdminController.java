package org.osivia.services.user.savedsearches.administration.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationWindowSettings;
import org.osivia.services.user.savedsearches.administration.portlet.service.UserSavedSearchesAdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;

/**
 * User saved search administration portlet administration controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("ADMIN")
public class UserSavedSearchesAdministrationAdminController {

    /**
     * Portlet context.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private UserSavedSearchesAdministrationService service;


    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationAdminController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request        action request
     * @param response       action response
     * @param windowSettings window settings model attribute
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("windowSettings") UserSavedSearchesAdministrationWindowSettings windowSettings) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.setWindowSettings(portalControllerContext, windowSettings);

        response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(WindowState.NORMAL);
    }


    /**
     * Get window settings model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return window settings
     */
    @ModelAttribute("windowSettings")
    public UserSavedSearchesAdministrationWindowSettings getWindowSettings(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getWindowSettings(portalControllerContext);
    }

}
