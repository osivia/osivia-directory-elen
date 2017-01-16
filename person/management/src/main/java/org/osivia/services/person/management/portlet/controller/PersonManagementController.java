/**
 * 
 */
package org.osivia.services.person.management.portlet.controller;

import java.io.IOException;
import java.util.List;

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
import org.osivia.services.person.management.portlet.model.PersonManagementForm;
import org.osivia.services.person.management.portlet.model.User;
import org.osivia.services.person.management.portlet.service.PersonManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Person management portlet controller.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class PersonManagementController implements PortletContextAware {

    /** Portlet config. */
    private PortletContext portletContext;


    /** Portlet service. */
    @Autowired
    private PersonManagementService service;


    /**
     * Constructor.
     */
    public PersonManagementController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @param form form model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("form") PersonManagementForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Set users form in request
        List<User> users = this.service.getUsers(portalControllerContext, form.getFilter());
        request.setAttribute("users", users);

        return "view";
    }

    
    /**
     * Select user action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "select", params = "select")
    public void select(ActionRequest request, ActionResponse response, @ModelAttribute("form") PersonManagementForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        this.service.select(portalControllerContext, form);
    }


    /**
     * Apply filter action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     */
    @ActionMapping(name = "select", params = "apply-filter")
    public void applyFilter(ActionRequest request, ActionResponse response, @ModelAttribute("form") PersonManagementForm form) {
        // Do nothing
    }


    /**
     * Search resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param filter search filter request parameter
     * @param form form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @RequestParam(name = "filter", required = false) String filter,
            @ModelAttribute("form") PersonManagementForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Set users in request
        List<User> users = this.service.getUsers(portalControllerContext, filter);
        request.setAttribute("users", users);

        // Set form in request
        request.setAttribute("form", form);

        // View path
        String path = this.service.resolveViewPath(portalControllerContext, "results");
        PortletRequestDispatcher dispatcher = this.portletContext.getRequestDispatcher(path);
        dispatcher.include(request, response);
    }


    /**
     * Get person management form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public PersonManagementForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
