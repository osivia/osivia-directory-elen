package fr.toutatice.identite.portail.creationPersonnes;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.portlet.PortalGenericPortlet;
import org.osivia.services.directory.helper.DirectoryPortlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.identite.portail.creationPersonnes.formulaire.FormCreation;
import fr.toutatice.outils.ldap.entity.Person;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({ "userConnecte" })
public class CreationPersonneController extends PortalGenericPortlet implements PortletContextAware, PortletConfigAware {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");
	protected static final Log logModifLdap = LogFactory.getLog("fr.toutatice.annuaire.modif");


	@Autowired
	private ApplicationContext context;

	@Autowired
	private Person personne;


	private PortletContext portletContext;
	private PortletConfig portletConfig;



	@ModelAttribute("formCreation")
	public FormCreation getFormCreation() {
		return new FormCreation();
	}

	@Autowired
	@Qualifier("creationValidator")
	private Validator creationValidator;


	public Person initUserConnecte(ModelMap model, RenderRequest request) {

		// Map<String, Object> userDatas = (Map<String, Object>)
		// request.getAttribute("osivia.userDatas");
		DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = this.personne.findUtilisateur(person.getUid());

		return userConnecte;
	
	}


	@RenderMapping
	public String showCreation(RenderRequest request, ModelMap model, PortletSession session, RenderResponse response) throws Exception {

		Person userConnecte = (Person) session.getAttribute("userConnecte");
		if (userConnecte == null) {
			userConnecte = this.initUserConnecte(model, request);
		}

		if (userConnecte == null) {
			return "nonConnecte";
		} else {
			// FicheCreation fiche = (FicheCreation) session.getAttribute("fiche");
			// if (fiche == null) {
			// // Chargement de la fiche lors de la 1ère consultation
			// fiche = initFicheCreation(model, request, response, userConnecte);
			// }

			return "creerPersonne";

		}

	}

	@RenderMapping(params = "action=creationOk")
	public String renderCreationOk(RenderRequest request, RenderResponse response, ModelMap model) {

		String urlFichePersonne = this.buildUrlFichePersonne(request, response, request.getParameter("uidFichePersonne"));

		model.addAttribute("urlFichePersonne", urlFichePersonne);

		return "creationOk";
	}

	@ActionMapping(params = "action=submit")
	public void submitModif(@ModelAttribute("formCreation") FormCreation formCreation, BindingResult result, ActionRequest request, ActionResponse response,
			PortletSession session, final ModelMap model) throws Exception {

		// TODO gestion des droits

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);


		// FicheCreation fiche = (FicheCreation) session.getAttribute("fiche");
		this.creationValidator.validate(formCreation, result);

		if (!result.hasErrors()) {

			try {
				// MAJ Annuaire
				Person newPerson = this.context.getBean(Person.class);

				PropertyUtils.copyProperties(newPerson, formCreation);

				// calculés
				newPerson.setCn(newPerson.getGivenName() + " " + newPerson.getSn());
				newPerson.setDisplayName(newPerson.getGivenName() + " " + newPerson.getSn());
				newPerson.setAlias(newPerson.getGivenName() + " " + newPerson.getSn());

				newPerson.create();
				logModifLdap.debug("L'utilisateur " + newPerson.getUid() + " a été créé ");

				// model.remove("fiche");
				request.setAttribute("osivia.updateContents", "true");
				// session.removeAttribute("fiche", PortletSession.PORTLET_SCOPE);
				response.setRenderParameter("action", "creationOk");
				response.setRenderParameter("uidFichePersonne", newPerson.getUid());

				this.addNotification(pcc, "label.creationOK", NotificationsType.SUCCESS);


			} catch (Exception e) {
				response.setRenderParameter("action", "modify");

				this.addNotification(pcc, "label.errorCreation", NotificationsType.ERROR);

				logger.error("création impossible", e);
			}

		} else {

			// model.addAttribute("Fiche", fiche);
			response.setRenderParameter("action", "creerPersonne");
		}

	}

	private String buildUrlFichePersonne(RenderRequest request, RenderResponse response, String uid) {
		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.hideTitle", "1");
		windowProperties.put("uidFichePersonne", uid);

		String url = null;

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, null);
		try {
			url = this.getPortalUrlFactory().getStartPortletUrl(pcc, DirectoryPortlets.fichePersonne.getInstanceName(), windowProperties, false);
		} catch (PortalException e) {
			logger.info("erreur de création de l'url", e);
		}

		return url;

	}

	@Override
    public void setPortletContext(PortletContext ctx) {
		this.portletContext = ctx;

	}

	@Override
    public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
	}

}
