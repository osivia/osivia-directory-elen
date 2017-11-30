package org.osivia.services.group.management.portlet.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.group.management.portlet.model.GroupManagementForm;
import org.osivia.services.group.management.portlet.service.GroupManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Group management portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class GroupManagementController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Group management service. */
    @Autowired
    private GroupManagementService service;


    /**
     * Constructor.
     */
    public GroupManagementController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        return "view";
    }


    /**
     * Select group action mapping.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param form group management form
     * @throws PortletException
     */
    @ActionMapping(name = "submit", params = "select")
    public void select(ActionRequest request, ActionResponse response, @ModelAttribute("form") GroupManagementForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.select(portalControllerContext, form);
    }


    /**
     * Apply search filter action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form group management form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "select", params = "apply-filter")
    public void applyFilter(ActionRequest request, ActionResponse response, @ModelAttribute("form") GroupManagementForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.search(portalControllerContext, form);
    }


    /**
     * Search groups resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param filters search filters request parameter
     * @param form group management form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @RequestParam(name = "filters", required = false) String filters,
            @ModelAttribute("form") GroupManagementForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search
        this.service.search(portalControllerContext, form, filters);

        // View path
        String path = this.service.resolveViewPath(portalControllerContext, "results");
        PortletRequestDispatcher dispatcher = this.portletContext.getRequestDispatcher(path);
        dispatcher.include(request, response);
    }


    /**
     * Get group management form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public GroupManagementForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }

}
