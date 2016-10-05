package org.osivia.services.firstconnection.portlet.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.firstconnection.portlet.model.UserForm;
import org.osivia.services.firstconnection.portlet.model.validator.UserFormValidator;
import org.osivia.services.firstconnection.portlet.service.FirstConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * First connection portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class FirstConnectionController implements PortletContextAware {

    /** Portlet service. */
    @Autowired
    private FirstConnectionService service;

    /** User form validator. */
    @Autowired
    private UserFormValidator userFormValidator;

    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public FirstConnectionController() {
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
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form user form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("userForm") @Validated UserForm form, BindingResult result)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        if (!result.hasErrors()) {
            // Save
            this.service.save(portalControllerContext, form);

            // Redirection URL
            String redirectionUrl = this.service.getRedirectionUrl(portalControllerContext);
            if (StringUtils.isNotEmpty(redirectionUrl)) {
                response.sendRedirect(redirectionUrl);
            }
        }
    }


    /**
     * Get user form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return user form
     * @throws PortletException
     */
    @ModelAttribute("userForm")
    public UserForm getUserForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        
        return this.service.getUserForm(portalControllerContext);
    }


    /**
     * User form init binder.
     * 
     * @param binder portlet request data binder
     */
    @InitBinder("userForm")
    public void invitationsCreationFormInitBinder(PortletRequestDataBinder binder) {
        binder.setDisallowedFields("id", "email");
        binder.addValidators(this.userFormValidator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
