/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/) and others.
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
 *
 *
 * Contributors:
 *  aguihomat
 * 
 *    
 */
package fr.toutatice.workspace.portail.participantsWorkspace;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.workspace.portail.participantsWorkspace.bean.FormAdmin;


@Controller
@RequestMapping("ADMIN")

public class AdminController {
	

	@RequestMapping
	public String showAdmin(final  ModelMap model, RenderRequest request, PortletSession session) throws ToutaticeAnnuaireException {
		PortalWindow window = WindowFactory.getWindow(request);
		FormAdmin formAdmin = new FormAdmin();
		
		
		
		model.addAttribute("formAdmin",formAdmin);

		return "admin";
	}
	
	
	@ActionMapping(params="action=setAdminProperty") 
	public void setAdminProperty(@ModelAttribute FormAdmin formAdmin, BindingResult result, ActionRequest request, ActionResponse response, ModelMap modelMap, PortletSession session, final ModelMap model) throws Exception  {
		
		PortalWindow window = WindowFactory.getWindow(request);
		
		response.setPortletMode(PortletMode.VIEW);
		response.setRenderParameter("action", "");
		
		session.removeAttribute("workspace");
		
	}
	
	@ActionMapping(params="action=annuler") 
	public void annuler(@ModelAttribute FormAdmin formAdmin, BindingResult result, ActionRequest request, ActionResponse response, ModelMap modelMap, PortletSession session, final ModelMap model) throws Exception  {
		
		response.setPortletMode(PortletMode.VIEW);
		response.setRenderParameter("action", "");
	}

}
