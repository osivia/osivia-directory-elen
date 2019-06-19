package org.osivia.services.person.card.portlet.controller;

import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.collections.MapUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.person.card.portlet.model.PersonCardOptions;
import org.osivia.services.person.card.portlet.model.PersonPasswordEditionForm;
import org.osivia.services.person.card.portlet.model.validator.PersonPasswordEditionFormValidator;
import org.osivia.services.person.card.portlet.service.PersonCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Person password edition portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=editPassword")
@SessionAttributes("passwordEditionForm")
public class PersonPasswordEditionController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private PersonCardService service;

    /** Person password edition form validator. */
    @Autowired
    private PersonPasswordEditionFormValidator passwordEditionFormValidator;


    /**
     * Constructor.
     */
    public PersonPasswordEditionController() {
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
        return "edit-password";
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options portlet options model attribute
     * @param form person password edition form model attribute
     * @param result binding result
     * @param session session status
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") PersonCardOptions options,
            @Validated @ModelAttribute("passwordEditionForm") PersonPasswordEditionForm form, BindingResult result, SessionStatus session)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
            // Copy render parameters
            this.copyRenderParameters(request, response);
        } else {
        	
        	try {
        		this.service.updatePassword(portalControllerContext, options, form);
                // Complete session
                session.setComplete();
        	}
        	catch(PortletException e) {
                // Copy render parameters
                this.copyRenderParameters(request, response);
        	}
        }
    }


    /**
     * Cancel action mapping.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param session session status
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response, SessionStatus session) {
        // Complete session
        session.setComplete();
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
    public PersonCardOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get person passord edition form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return password edition form
     * @throws PortletException
     */
    @ModelAttribute("passwordEditionForm")
    public PersonPasswordEditionForm getPasswordEditionForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getPasswordEditionForm(portalControllerContext);
    }


    /**
     * Person password edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("passwordEditionForm")
    public void passwordEditionFormInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.passwordEditionFormValidator);
        binder.setDisallowedFields("uid", "overwrite");
    }


    /**
     * Copy render parameters.
     * 
     * @param request action request
     * @param response action response
     */
    private void copyRenderParameters(ActionRequest request, ActionResponse response) {
        Map<String, String[]> parameters = request.getPrivateParameterMap();
        if (MapUtils.isNotEmpty(parameters)) {
            for (Entry<String, String[]> entry : parameters.entrySet()) {
                response.setRenderParameter(entry.getKey(), entry.getValue());
            }
        }
    }

}
