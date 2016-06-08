package org.osivia.services.directory.workspace.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.osivia.services.directory.workspace.portlet.model.LocalGroups;
import org.osivia.services.directory.workspace.portlet.model.validator.LocalGroupValidator;
import org.osivia.services.directory.workspace.portlet.service.LocalGroupManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Workspace local group management view controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes(value = "localGroups")
public class ViewLocalGroupManagementController implements PortletContextAware {

    /** Local group management service. */
    @Autowired
    private LocalGroupManagementService service;

    /** Local group validator. */
    @Autowired
    private LocalGroupValidator localGroupValidator;

    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public ViewLocalGroupManagementController() {
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
     * Delete local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroups local groups model attribute
     * @param id deleted local group identifier request parameter
     * @throws PortletException
     */
    @ActionMapping(value = "delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute LocalGroups localGroups, @RequestParam String id)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.prepareDeletion(portalControllerContext, localGroups, id);
    }


    /**
     * Save local groups action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroups local groups model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute LocalGroups localGroups) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.saveLocalGroups(portalControllerContext, localGroups);
    }


    /**
     * Create local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroups local groups model attribute
     * @param form local group creation form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping(value = "create")
    public void create(ActionRequest request, ActionResponse response, @ModelAttribute LocalGroups localGroups,
            @ModelAttribute(value = "creationForm") @Validated LocalGroup form, BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.createLocalGroup(portalControllerContext, localGroups, form);

            form.setDisplayName(null);
        }
    }


    /**
     * Cancel local group creation action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form local group creation form model attribute
     */
    @ActionMapping(value = "create", params = "cancel")
    public void cancelCreation(ActionRequest request, ActionResponse response, @ModelAttribute(value = "creationForm") LocalGroup form) {
        form.setDisplayName(null);
    }


    /**
     * Edit local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param id local group identifier
     * @param status session status
     */
    @ActionMapping(value = "edit")
    public void edit(ActionRequest request, ActionResponse response, @RequestParam String id, SessionStatus status) {
        status.setComplete();

        response.setRenderParameter("view", "edit");
        response.setRenderParameter("id", id);
    }


    /**
     * Get local groups model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return local groups
     * @throws PortletException
     */
    @ModelAttribute(value = "localGroups")
    public LocalGroups getLocalGroups(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getLocalGroups(portalControllerContext);
    }


    /**
     * Get local group creation form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return creation form
     */
    @ModelAttribute(value = "creationForm")
    public LocalGroup getCreationForm(PortletRequest request, PortletResponse response) {
        return new LocalGroup();
    }


    /**
     * Local group init binder.
     *
     * @param binder web data binder
     */
    @InitBinder(value = "creationForm")
    protected void localGroupInitBinder(WebDataBinder binder) {
        binder.addValidators(this.localGroupValidator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
