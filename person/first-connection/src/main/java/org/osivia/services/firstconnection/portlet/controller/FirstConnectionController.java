package org.osivia.services.firstconnection.portlet.controller;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * First connection portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class FirstConnectionController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private FirstConnectionService service;

    /**
     * User form validator.
     */
    @Autowired
    private UserFormValidator userFormValidator;


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
     * Password rules information resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param password password
     */
    @ResourceMapping("password-information")
    public void passwordInformation(ResourceRequest request, ResourceResponse response, @RequestParam(name = "password", required = false) String password) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Password rules information
        Element information = this.service.getPasswordRulesInformation(portalControllerContext, password);

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(information);
        htmlWriter.close();
    }


    /**
     * Get user form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return user form
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
        binder.setDisallowedFields("id");
        binder.addValidators(this.userFormValidator);
    }

}
