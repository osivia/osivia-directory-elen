package org.osivia.services.directory.workspace.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.model.AddForm;
import org.osivia.services.directory.workspace.model.Member;
import org.osivia.services.directory.workspace.model.MemberComparator;
import org.osivia.services.directory.workspace.model.MembersContainer;
import org.osivia.services.directory.workspace.model.Role;
import org.osivia.services.directory.workspace.service.MemberManagementService;
import org.osivia.services.directory.workspace.validator.AddFormValidator;
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
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import net.sf.json.JSONArray;

/**
 * Workspace member management portlet view controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes(value = "container")
public class ViewMemberManagementController implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;

    /** Member management service. */
    @Autowired
    private MemberManagementService service;

    /** Add form validator. */
    @Autowired
    private AddFormValidator addFormValidator;


    /**
     * Constructor.
     */
    public ViewMemberManagementController() {
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
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute(value = "container") MembersContainer container, @RequestParam(
            value = "sort", defaultValue = "name") String sortParameter, @RequestParam(value = "alt", required = false) String altParameter) {
        // Sort
        if (sortParameter != null) {
            request.setAttribute("sort", sortParameter);
        }
        boolean alt = BooleanUtils.toBoolean(altParameter);
        request.setAttribute("alt", alt);
        if (CollectionUtils.isNotEmpty(container.getMembers())) {
            Comparator<Member> comparator = new MemberComparator(sortParameter, alt);
            Collections.sort(container.getMembers(), comparator);
        }


        return "view";
    }


    /**
     * Update members action mapping.
     *
     * @param request action request
     * @param response action response
     * @param container members container model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "update", params = "save")
    public void update(ActionRequest request, ActionResponse response, @ModelAttribute(value = "container") MembersContainer container) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.update(portalControllerContext, container);

        this.copyRenderParameter(request, response);
    }


    /**
     * Delete member action mapping.
     *
     * @param request action request
     * @param response action response
     * @param container members container model attribute
     * @param name member name request parameter
     * @throws PortletException
     */
    @ActionMapping(value = "update", params = "delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute(value = "container") MembersContainer container, @RequestParam(
            value = "delete") String name) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, container, name);

        this.copyRenderParameter(request, response);
    }


    /**
     * Cancel update members action mapping.
     *
     * @param request action request
     * @param response action response
     * @param container members container
     * @throws PortletException
     */
    @ActionMapping(value = "update", params = "cancel")
    public void cancelUpdate(ActionRequest request, ActionResponse response, @ModelAttribute(value = "container") MembersContainer container)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        MembersContainer previousContainer = this.service.getMembersContainer(portalControllerContext);
        container.setMembers(previousContainer.getMembers());

        this.copyRenderParameter(request, response);
    }


    /**
     * Add members action mapping.
     *
     * @param request action request
     * @param response action response
     * @param container members container model attribute
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "add", params = "save")
    public void add(ActionRequest request, ActionResponse response, @ModelAttribute(value = "container") MembersContainer container, @ModelAttribute(
            value = "addForm") @Validated AddForm form, BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.add(portalControllerContext, container, form);

        // Reset role
        form.setRole(Role.DEFAULT);

        this.copyRenderParameter(request, response);
    }


    /**
     * Cancel add member action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form form
     * @throws PortletException
     */
    @ActionMapping(value = "add", params = "cancel")
    public void cancelAdd(ActionRequest request, ActionResponse response, @ModelAttribute(value = "addForm") AddForm form) {
        // Reset role
        form.setRole(Role.DEFAULT);

        this.copyRenderParameter(request, response);
    }


    /**
     * Search members resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param filter search filter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping(value = "search")
    public void search(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        JSONArray results = this.service.searchMembers(portalControllerContext, filter);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
    }


    /**
     * Get members container model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return members container
     * @throws PortletException
     */
    @ModelAttribute(value = "container")
    public MembersContainer getMembersContainer(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getMembersContainer(portalControllerContext);
    }


    /**
     * Get add members form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute(value = "addForm")
    public AddForm getAddForm(PortletRequest request, PortletResponse response) throws PortletException {
        return new AddForm();
    }


    /**
     * Add form model attribute init binder.
     *
     * @param binder web data binder
     */
    @InitBinder(value = "addForm")
    protected void addFormInitBinder(WebDataBinder binder) {
        binder.addValidators(this.addFormValidator);
    }


    /**
     * Get roles.
     *
     * @param request portlet request
     * @param response portlet response
     * @return roles
     * @throws PortletException
     */
    @ModelAttribute(value = "roles")
    public List<Role> getRoles(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(Role.values());
    }


    /**
     * Copy render parameters.
     *
     * @param request action request
     * @param response action response
     */
    private void copyRenderParameter(ActionRequest request, ActionResponse response) {
        // Sort
        String sort = request.getParameter("sort");
        if (sort != null) {
            response.setRenderParameter("sort", sort);
        }

        // Alt
        String alt = request.getParameter("alt");
        if (alt != null) {
            response.setRenderParameter("alt", alt);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
