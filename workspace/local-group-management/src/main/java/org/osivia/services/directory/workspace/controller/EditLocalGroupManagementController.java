package org.osivia.services.directory.workspace.controller;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.model.LocalGroup;
import org.osivia.services.directory.workspace.model.Member;
import org.osivia.services.directory.workspace.service.LocalGroupManagementService;
import org.osivia.services.directory.workspace.validator.LocalGroupValidator;
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
 * Workspace local group management edition controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(value = "VIEW", params = "view=edit")
@SessionAttributes(value = {"localGroup", "members"})
public class EditLocalGroupManagementController implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;

    /** Local group management service. */
    @Autowired
    private LocalGroupManagementService service;

    /** Local group validator. */
    @Autowired
    private LocalGroupValidator localGroupValidator;


    /**
     * Constructor.
     */
    public EditLocalGroupManagementController() {
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
     * Save local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroup local group model attribute
     * @param result binding result
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping(value = "edit", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute(value = "localGroup") @Validated LocalGroup localGroup,
            BindingResult result, SessionStatus sessionStatus) throws PortletException {
        if (result.hasErrors()) {
            response.setRenderParameter("view", "edit");
            response.setRenderParameter("id", localGroup.getId());
        } else {
            // Portal controller context
            PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

            this.service.saveLocalGroup(portalControllerContext, localGroup);

            sessionStatus.setComplete();
        }
    }


    /**
     * Add members to local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroup local group model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "edit", params = "add")
    public void add(ActionRequest request, ActionResponse response, @ModelAttribute LocalGroup localGroup) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.addMembersToLocalGroup(portalControllerContext, localGroup);

        response.setRenderParameter("view", "edit");
        response.setRenderParameter("id", localGroup.getId());
    }


    /**
     * Cancel action mapping.
     *
     * @param request action request
     * @param response action response
     * @param sessionStatus session status
     */
    @ActionMapping(value = "edit", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
    }


    /**
     * Delete local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param id local group identifier
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping(value = "delete")
    public void delete(ActionRequest request, ActionResponse response, @RequestParam String id, SessionStatus sessionStatus)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteLocalGroup(portalControllerContext, id);

        sessionStatus.setComplete();
    }


    /**
     * Get local group model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param id local group identifier request parameter
     * @return local group
     * @throws PortletException
     */
    @ModelAttribute(value = "localGroup")
    public LocalGroup getLocalGroup(PortletRequest request, PortletResponse response, @RequestParam String id) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getLocalGroup(portalControllerContext, id);
    }


    /**
     * Local group init binder.
     *
     * @param binder web data binder
     */
    @InitBinder(value = "localGroup")
    public void localGroupInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.addValidators(this.localGroupValidator);
    }


    /**
     * Get members model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return members
     * @throws PortletException
     */
    @ModelAttribute(value = "members")
    public List<Member> getMembers(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getMembers(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
