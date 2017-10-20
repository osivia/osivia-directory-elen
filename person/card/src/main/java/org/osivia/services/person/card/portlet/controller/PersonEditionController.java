package org.osivia.services.person.card.portlet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.person.card.portlet.model.PersonCardOptions;
import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.osivia.services.person.card.portlet.model.validator.PersonEditionFormValidator;
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
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Person edition portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=edit")
@SessionAttributes("editionForm")
public class PersonEditionController extends CMSPortlet {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private PersonCardService service;

    /** Person edition form validator. */
    @Autowired
    private PersonEditionFormValidator editionFormValidator;


    /**
     * Constructor.
     */
    public PersonEditionController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
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
     * Upload avatar action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form person edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "upload-avatar")
    public void uploadAvatar(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") PersonEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Copy render parameters
        this.copyRenderParameters(request, response);

        this.service.uploadAvatar(portalControllerContext, form);
    }


    /**
     * Delete avatar action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form person edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "delete-avatar")
    public void deleteAvatar(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") PersonEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Copy render parameters
        this.copyRenderParameters(request, response);

        this.service.deleteAvatar(portalControllerContext, form);
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options portlet options model attribute
     * @param form person edition form model attribute
     * @param result binding result
     * @param session session status
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") PersonCardOptions options,
            @Validated @ModelAttribute("editionForm") PersonEditionForm form, BindingResult result, SessionStatus session) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
            // Copy render parameters
            this.copyRenderParameters(request, response);
        } else {
            this.service.savePerson(portalControllerContext, options, form);

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
     * Avatar preview resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param form person edition form model attribute
     * @throws IOException
     */
    @ResourceMapping("avatarPreview")
    public void avatarPreview(ResourceRequest request, ResourceResponse response, @ModelAttribute("editionForm") PersonEditionForm form) throws IOException {
        // Temporary file
        File temporaryFile = form.getAvatar().getTemporaryFile();

        // Upload size
        Long size = new Long(temporaryFile.length());
        response.setContentLength(size.intValue());

        // Content type
        String contentType = response.getContentType();
        response.setContentType(contentType);

        // Character encoding
        response.setCharacterEncoding(CharEncoding.UTF_8);

        // No cache
        response.getCacheControl().setExpirationTime(0);


        // Input steam
        InputStream inputSteam = new FileInputStream(temporaryFile);
        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();
        // Copy
        IOUtils.copy(inputSteam, outputStream);
        outputStream.close();
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
     * Get person edition form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return edition form
     * @throws PortletException
     */
    @ModelAttribute("editionForm")
    public PersonEditionForm getEditionForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getEditionForm(portalControllerContext);
    }


    /**
     * Person edition form init binder.
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
