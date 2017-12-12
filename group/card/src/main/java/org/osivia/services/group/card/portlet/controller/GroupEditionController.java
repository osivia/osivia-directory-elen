package org.osivia.services.group.card.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import net.sf.json.JSONObject;

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

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;
    
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
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        response.setTitle(bundle.getString("GROUP_CARD_EDITION_TITLE"));
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
    @ActionMapping(name = "save", params="save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") GroupCardOptions options,
            @Validated @ModelAttribute("editionForm") GroupEditionForm form, BindingResult result, SessionStatus session) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
            //Stay on the edit page
            response.setRenderParameter("view","edit");
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
    @ActionMapping(name = "save", params="addMember")
    public void addMember(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") GroupEditionForm form,
            @ModelAttribute("options") GroupCardOptions options) throws PortletException{
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        this.service.addMember(portalControllerContext, form, options.getGroup());
        
        //Stay on the edit page
        response.setRenderParameter("view","edit");
    }
 
    /**
     * Update form after removing an added member
     * This member is removed from the list
     * 
     * @param request portlet request
     * @param response portlet response
     * @param session session status
     */
    @ActionMapping(name = "save", params="updateForm")
    public void updateForm(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") GroupEditionForm form,
            @ModelAttribute("options") GroupCardOptions options) throws PortletException{

        this.service.updateMemberList(form);
        
        //Stay on the edit page
        response.setRenderParameter("view","edit");
    }
    
    /**
     * Search persons action mapping.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param session session status
     * @throws IOException 
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @ModelAttribute("options") GroupCardOptions options,
            @ModelAttribute("editionForm") GroupEditionForm form, @RequestParam(value = "filter", required = false) String filter) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        JSONObject results = this.service.searchPersons(portalControllerContext, options, form, filter);
        
        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
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

}
