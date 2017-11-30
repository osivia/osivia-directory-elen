package org.osivia.services.group.card.portlet.controller;

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
import org.osivia.services.group.card.portlet.model.GroupCardOptions;
import org.osivia.services.group.card.portlet.model.GroupEditionForm;
import org.osivia.services.group.card.portlet.model.validator.GroupEditionFormValidator;
import org.osivia.services.group.card.portlet.service.GroupCardService;
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
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping(path = "VIEW", params = "view=edit")
@SessionAttributes("editionForm")
public class GroupEditionController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;
    
    /** Group card Service */
    @Autowired
    private GroupCardService service;
    
    /** Form validator */
    @Autowired
    private GroupEditionFormValidator editionFormValidator;
    
    public GroupEditionController() {
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
        return "edit";
    }
    
    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options portlet options model attribute
     * @param form group edition form model attribute
     * @param result binding result
     * @param session session status
     * @throws PortletException
     */
    @ActionMapping(name = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") GroupCardOptions options,
            @Validated @ModelAttribute("editionForm") GroupEditionForm form, BindingResult result, SessionStatus session) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
            // Copy render parameters
            this.copyRenderParameters(request, response);
        } else {
            this.service.saveGroup(portalControllerContext, options, form);

            // Complete session
            session.setComplete();
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
     * Add member action mapping.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param session session status
     */
    @ActionMapping("addMember")
    public void addMember(ActionRequest request, ActionResponse response, SessionStatus session) {
        
        //TODO A ecrire
    }
    
    /**
     * Search persons action mapping.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param session session status
     */
    @ResourceMapping("search")
    public void search(ActionRequest request, ActionResponse response, SessionStatus session) {
        
        //TODO A ecrire
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
     * Get person edition form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return edition form
     * @throws PortletException
     */
    @ModelAttribute("editionForm")
    public GroupEditionForm getEditionForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getEditionForm(portalControllerContext);
    }


    /**
     * Group edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("editionForm")
    public void editionFormInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.editionFormValidator);
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
