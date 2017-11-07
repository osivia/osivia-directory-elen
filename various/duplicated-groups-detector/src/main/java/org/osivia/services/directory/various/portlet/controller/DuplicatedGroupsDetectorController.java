package org.osivia.services.directory.various.portlet.controller;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.various.portlet.model.DuplicatedGroups;
import org.osivia.services.directory.various.portlet.service.DuplicatedGroupsDetectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Duplicated groups detector portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class DuplicatedGroupsDetectorController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private DuplicatedGroupsDetectorService service;


    /**
     * Constructor.
     */
    public DuplicatedGroupsDetectorController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Get duplicated groups model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return duplicated groups
     * @throws PortletException
     */
    @ModelAttribute("duplicatedGroups")
    public DuplicatedGroups getDuplicatedGroups(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getDuplicatedGroups(portalControllerContext);
    }

}
