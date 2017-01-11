/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.services.person.card.portlet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.directory.v2.model.ext.Avatar;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.person.card.portlet.service.PersonCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Controller for edit and remove person
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping(value = "VIEW", params = "controller=edit")
@SessionAttributes({"card","formEdition"})
public class PersonEditionCardController  extends CMSPortlet implements PortletContextAware, PortletConfigAware  {

	private PortletContext portletContext;
	private PortletConfig portletConfig;
	
	@Autowired
	private PersonCardService service;
	

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;
    
    
	@Autowired
	private Validator modifyValidator;	
	
	
	@RenderMapping
	public String modify() {
		
		return "modify-card";
	}
	
	
	@ModelAttribute("formEdition")
	public FormEdition getFormEdition(@ModelAttribute("card") Card card) {
		FormEdition form = new FormEdition();
		
		Avatar a = new Avatar();
		a.setUrl(card.getAvatar().getUrl());
		form.setAvatar(a);
		
		form.setGivenName(card.getUserConsulte().getGivenName());
		form.setMail(card.getUserConsulte().getMail());
		form.setSn(card.getUserConsulte().getSn());
		form.setTitle(card.getUserConsulte().getTitle());
		
		form.setBio(card.getNxProfile().getBio());
		form.setOccupation(card.getNxProfile().getOccupation());
		form.setPhone(card.getNxProfile().getPhone());
		form.setMobilePhone(card.getNxProfile().getMobilePhone());
		
		return form;
	}
	
	

    /**
     * Upload avatar action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "edit", params = "upload-avatar")
    public void uploadAvatar(ActionRequest request, ActionResponse response, @ModelAttribute("formEdition") FormEdition form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadAvatar(portalControllerContext, form);
        
        response.setRenderParameter("controller", "edit");
    }


    /**
     * Delete avatar action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form
     * @throws PortletException
     */
    @ActionMapping(name = "edit", params = "delete-avatar")
    public void deleteAvatar(ActionRequest request, ActionResponse response, @ModelAttribute("formEdition") FormEdition form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteAvatar(portalControllerContext, form);
        
        response.setRenderParameter("controller", "edit");
    }
    
    
	
    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     * @throws PortalException 
     */
    @ActionMapping(value = "edit", params = "save")
    public void save(ActionRequest request, ActionResponse response,@ModelAttribute("card") Card card, @ModelAttribute("formEdition") FormEdition form,
    		BindingResult result, SessionStatus sessionStatus)
            throws PortletException, IOException, PortalException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // enregistrement des modifications
     	this.modifyValidator.validate(form, result);
     	

        // Notification
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        
        if (result.hasErrors()) {
	        String message = bundle.getString("CARD_UPDATE_ERROR");
	        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);			
			 
            
            response.setRenderParameter("controller", "edit");
        } else {
            this.service.saveCard(portalControllerContext, card, form);

            request.setAttribute("osivia.updateContents", "true");
            
            sessionStatus.setComplete();
            
	        String message = bundle.getString("CARD_UPDATE_OK");
	        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);			

        }
    }    

    /**
     * avatar preview resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param form workspace edition form
     * @throws IOException
     */
    @ResourceMapping("avatarPreview")
    public void avatarPreview(ResourceRequest request, ResourceResponse response, @ModelAttribute("formEdition") FormEdition form) throws IOException {
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

    @ActionMapping(value = "edit", params = "cancel")
	public void cancel(ActionResponse response, SessionStatus status, ModelMap model) throws Exception {

		status.setComplete();
	}
	
	@ActionMapping("delete")
	public void delete(@ModelAttribute("card") Card card, ActionRequest request, ActionResponse response) {
		service.deletePerson(card);
		
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
		
		// Notification
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        String message = bundle.getString("PERSON_DELETE_OK");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);			
        
        response.setRenderParameter("action", "blank");
	}

	
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletConfigAware#setPortletConfig(javax.portlet.PortletConfig)
	 */
	@Override
	public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletContextAware#setPortletContext(javax.portlet.PortletContext)
	 */
	@Override
	public void setPortletContext(PortletContext portletContext) {
		this.portletContext = portletContext;
		
	}	
}
