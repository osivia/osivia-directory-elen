package fr.toutatice.identite.portail.gestionGroupes;

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


@Controller
@RequestMapping("ADMIN")

public class AdminController {
	
	@RequestMapping
	public String showAdmin(final  ModelMap model, RenderRequest request, PortletSession session) throws ToutaticeAnnuaireException {
		PortalWindow window = WindowFactory.getWindow(request);
		FormAdmin formAdmin = new FormAdmin();
		if(window.getProperty("toutatice.identite.gestionGroupes.rowcount")!=null){
			formAdmin.setRowcount(Integer.parseInt(window.getProperty("toutatice.identite.gestionGroupes.rowcount")));
		} else {
			formAdmin.setRowcount(10);
		}
		if(window.getProperty("toutatice.identite.gestionGroupes.rne")!=null){
			formAdmin.setRne(window.getProperty("toutatice.identite.gestionGroupes.rne"));
		} else {
			formAdmin.setRne("");
		}
		
		model.addAttribute("formAdmin",formAdmin);

		return "admin";
	}
	
	
	@ActionMapping(params="action=setAdminProperty") 
	public void setAdminProperty(@ModelAttribute FormAdmin formAdmin, BindingResult result, ActionRequest request, ActionResponse response, ModelMap modelMap, PortletSession session, final ModelMap model) throws Exception  {
		
		PortalWindow window = WindowFactory.getWindow(request);
		window.setProperty("toutatice.identite.gestionGroupes.rowcount", String.valueOf(formAdmin.getRowcount()));
		window.setProperty("toutatice.identite.gestionGroupes.rne", String.valueOf(formAdmin.getRne()));
		session.removeAttribute("formulaire");
		response.setPortletMode(PortletMode.VIEW);
		response.setRenderParameter("action", "groupesGeres");
	}
	
	@ActionMapping(params="action=annuler") 
	public void annuler(@ModelAttribute FormAdmin formulaire, BindingResult result, ActionRequest request, ActionResponse response, ModelMap modelMap, PortletSession session, final ModelMap model) throws Exception  {
		
		response.setPortletMode(PortletMode.VIEW);
		response.setRenderParameter("action", "groupesGeres");
	}

}
