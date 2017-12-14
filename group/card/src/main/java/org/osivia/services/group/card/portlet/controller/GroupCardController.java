package org.osivia.services.group.card.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.group.card.portlet.model.GroupCard;
import org.osivia.services.group.card.portlet.model.GroupCardOptions;
import org.osivia.services.group.card.portlet.service.GroupCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Group card portlet controller.
 * 
 * @author Julien Barberet
 */
@Controller
@RequestMapping("VIEW")
public class GroupCardController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;
    
    /** Group card Service */
    @Autowired
    private GroupCardService service;
    
   

    /**
     * Constructor.
     */
    public GroupCardController() {
        super();
    }
    

    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param options portlet options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("options") GroupCardOptions options) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.doView(portalControllerContext, options);
    }
    

    /**
     * Delete group action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options portlet option model attribute
     * @throws PortletException
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("options") GroupCardOptions options) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteGroup(portalControllerContext, options);
    }


    /**
     * Get portlet options.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public GroupCardOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }
    

    /**
     * Get group card model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return group card
     * @throws PortletException
     */
    @ModelAttribute("card")
    public GroupCard getGroupCard(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getGroupCard(portalControllerContext);
    }

}
