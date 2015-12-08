package fr.toutatice.identite.portail.gestionPersonnes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.directory.helper.DirectoryPortlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.identite.portail.gestionPersonnes.HabilitationCreation.LevelCreation;
import fr.toutatice.identite.portail.gestionPersonnes.HabilitationSurcharge.level;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({ "formulaire" })
public class GestionPersonnesController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.identite");
	private PortletContext portletContext;

	private PortletConfig portletConfig;

	@Autowired
	private Person personneInstance;

	@Autowired
	private GestionPersonnesConfig config;

	@Autowired
	private HabilitationSurcharge habilitationSurcharge;

	@Autowired
	private HabilitationCreation habilitationCreation;

	@PostConstruct
	public void initNuxeoService() throws Exception {
		super.init();
		if ((this.portletContext != null) && (this.portletContext.getAttribute("nuxeoService") == null)) {

			this.init(this.portletConfig);
		}

	}

	@ModelAttribute("formulaire")
	public Formulaire init(PortletRequest request, PortletResponse response) throws ToutaticeAnnuaireException, PortalException {

		DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = this.personneInstance.findUtilisateur(person.getUid());

		Formulaire formulaire = new Formulaire();
		level findRoleUser = this.habilitationSurcharge.findRoleUser(userConnecte);
		formulaire.setLevelSurchargeUserConnecte(findRoleUser);
		
		LevelCreation findRoleCreation = this.habilitationCreation.findRoleUser(userConnecte);
		formulaire.setLevelCreation(findRoleCreation);
		

		if (this.config.getMinCarsSearch() == 0) {
			List<Person> liste = this.personneInstance.getPersonByCriteres("", "", "", "", "","sn");
			PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
			List<PersonUrl> listeUrl = this.getListePersonnesUrl(liste, portalControllerContext, request, response);
			formulaire.setListePersonnesRecherchees(listeUrl);
		}

        if (BooleanUtils.isTrue(this.config.getEnableOverload())) {
            if (findRoleUser != HabilitationSurcharge.level.NONHABILITE) {
                PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
                List<Person> listeMesSurcharges = this.personneInstance.rechercherSurchargeParUtilisateur(userConnecte.getUid(), "sn");
                List<PersonUrl> listeUrl = this.getListePersonnesUrl(listeMesSurcharges, portalControllerContext, request, response);

                formulaire.setListeMesSurcharges(listeUrl);

            }

            if ((findRoleUser == HabilitationSurcharge.level.ADMINISTRATEUR) || (findRoleUser == HabilitationSurcharge.level.SUPERADMINISTRATEUR)) {
                PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
                List<Person> listeSurcharges = this.personneInstance.rechercherSurcharge("sn");
                List<PersonUrl> listeUrl = this.getListePersonnesUrl(listeSurcharges, portalControllerContext, request, response);

                formulaire.setListePersonnesSurchargees(listeUrl);
            }
		}


		// Traduction entité
		NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
        Map<String, VocabularyEntry> children;
        try {
            VocabularyEntry vocabularyEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, "organization");
            children = vocabularyEntry.getChildren();
        } catch (NuxeoException e) {
            logger.error(e.getMessage(), e);
            children = new HashMap<String, VocabularyEntry>(0);
        }

		Map<String, String> listeDptCns = new LinkedHashMap<String, String>();

		for(Map.Entry<String, VocabularyEntry>  entry : children.entrySet()) {
			listeDptCns.put(entry.getValue().getId(), entry.getValue().getLabel());
		}

		formulaire.setListeDptCns(listeDptCns);


		return formulaire;
	}

	@RenderMapping
	public String showRecherchePersonne(@ModelAttribute("formulaire") Formulaire formulaire, RenderRequest request, RenderResponse response) throws Exception {

		request.setAttribute("currentTab", "recherchePersonne");

		this.getUrlCreation(request, response);

		return "recherchePersonne";

	}

	@RenderMapping(params = "action=personnesSurchargees")
	public String showPersonnesSurchargees(@ModelAttribute("formulaire") Formulaire formulaire, RenderRequest request, RenderResponse response)
			throws Exception {

		request.setAttribute("currentTab", "personnesSurchargees");

		String retour;

		if ((formulaire.getLevelSurchargeUserConnecte() == HabilitationSurcharge.level.ADMINISTRATEUR)
				|| (formulaire.getLevelSurchargeUserConnecte() == HabilitationSurcharge.level.SUPERADMINISTRATEUR)) {

			retour = "personnesSurchargees";
		} else {
			retour = "nonAutorise";
		}

		return retour;

	}

	@RenderMapping(params = "action=mesSurcharges")
	public String showMesSurcharges(@ModelAttribute("formulaire") Formulaire formulaire, RenderRequest request, RenderResponse response) throws Exception {

		request.setAttribute("currentTab", "mesSurcharges");

		String retour;

		if (formulaire.getLevelSurchargeUserConnecte() != HabilitationSurcharge.level.NONHABILITE) {

			retour = "mesSurcharges";
		} else {
			retour = "nonAutorise";
		}

		return retour;
	}

	// }

	@RenderMapping(params = "action=nonAutorise")
	public String showNonAutorise() {
		return "nonAutorise";
	}

	@ActionMapping("rechercherPersonne")
	public void recherchePersonne(@ModelAttribute Formulaire formulaire, BindingResult result, ActionRequest request, ActionResponse response)
			throws PortalException {

		Integer minCarsSearch = this.config.getMinCarsSearch();
		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		if ((formulaire.getFiltreNom().length() >= minCarsSearch) || (formulaire.getFiltreDptCns().length() >= minCarsSearch)) {
			List<Person> liste = this.personneInstance.getPersonByCriteres(formulaire.getFiltreNom(), "", "", "", formulaire.getFiltreDptCns(), "sn");

			List<PersonUrl> personnesUrl = this.getListePersonnesUrl(liste, pcc, request,null);

			formulaire.setListePersonnesRecherchees(personnesUrl);

			if (liste.size() < 1) {
				this.addNotification(pcc, "label.noresult", NotificationsType.INFO);
			}

		} else {
			this.addNotification(pcc, "label.3charmin", NotificationsType.WARNING);

		}

		response.setRenderParameter("action", "recherchePersonne");

	}

	@ActionMapping(params = "action=deleteMaSurcharge")
	public void deleteMaSurcharge(@ModelAttribute("formulaire") Formulaire formulaire, @RequestParam String uid, ActionRequest request,
			ActionResponse response, SessionStatus status) {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		Person personneADeSurcharger = this.personneInstance.findUtilisateur(uid);
		try {
			personneADeSurcharger.deleteSurcharge();
		} catch (ToutaticeAnnuaireException e) {

			DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);

			logger.error("Erreur lors de la suppression de la surcharge de la personne " + uid + " par l'utilisateur " + person.getUid());
			this.addNotification(pcc, "label.erreurDeleteMaSurcharge", NotificationsType.ERROR, uid, person.getUid());

		}

		this.addNotification(pcc, "label.succesDeleteSurcharge", NotificationsType.SUCCESS, uid);

		status.setComplete();
		response.setRenderParameter("action", "mesSurcharges");

	}

	@ActionMapping(params = "action=deleteSurcharge")
	public void deleteSurcharge(@ModelAttribute("formulaire") Formulaire formulaire, @RequestParam String uid, ActionRequest request, ActionResponse response,
			SessionStatus status) {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		Person personneADeSurcharger = this.personneInstance.findUtilisateur(uid);
		try {
			personneADeSurcharger.deleteSurcharge();
		} catch (ToutaticeAnnuaireException e) {

			DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);

			logger.error("Erreur lors de la suppression de la surcharge de la personne " + uid + " par l'utilisateur " + person.getUid());

			this.addNotification(pcc, "label.erreurDeleteSurcharge", NotificationsType.ERROR, uid, person.getUid());
		}

		this.addNotification(pcc, "label.succesDeleteSurcharge", NotificationsType.SUCCESS, uid);

		status.setComplete();
		response.setRenderParameter("action", "personnesSurchargees");

	}

	private List<PersonUrl> getListePersonnesUrl(List<Person> listePersonnes, PortalControllerContext portalControllerContext, PortletRequest request, PortletResponse response) throws PortalException {
		List<PersonUrl> liste = new LinkedList<PersonUrl>();
		NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

		for (Person p : listePersonnes) {
			String url = "";

			Map<String, String> windowProperties = new HashMap<String, String>();
			windowProperties.put("osivia.ajaxLink", "1");
			windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
			windowProperties.put("osivia.close.refreshPage", "1");
			//windowProperties.put("osivia.hideTitle", "1");
			windowProperties.put("osivia.bootstrapPanelStyle", "true");

			windowProperties.put("uidFichePersonne", p.getUid());

			url = this.getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.fichePersonne.getInstanceName(), windowProperties, false);

			PersonUrl pers = new PersonUrl(p, url);
			try {
				pers.setAvatar(nuxeoController.getUserAvatar(p.getUid()));
			} catch (CMSException e) {
				// do nothing
			}

			liste.add(pers);


		}
		return liste;

	}

	@ActionMapping(params = "action=deleteUser")
	public void deleteUser(@ModelAttribute Formulaire formulaire, BindingResult result, ActionRequest request, ActionResponse response, SessionStatus status) {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		String uid = request.getParameter("uid");

		Person userToDelete = this.personneInstance.findUtilisateur(uid);

		try {
			userToDelete.delete();
			this.addNotification(pcc, "label.userDeleted", NotificationsType.SUCCESS);
		} catch (ToutaticeAnnuaireException e) {
			logger.error("impossible de supprimer la personne", e);
			this.addNotification(pcc, "label.userNotDeleted", NotificationsType.ERROR);
		} catch (NamingException e) {
			logger.error("impossible de supprimer la personne", e);
			this.addNotification(pcc, "label.userNotDeleted", NotificationsType.ERROR);
		}

		status.setComplete();

	}

	private void getUrlCreation(RenderRequest request, RenderResponse response) {

		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("osivia.title", "Créer un nouvel utilisateur");
		windowProperties.put("osivia.hideTitle", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);
		String url = "";
		try {
			url = this.getPortalUrlFactory().getStartPortletUrl(pcc, DirectoryPortlets.creationPersonne.getInstanceName(), windowProperties, false);
		} catch (PortalException e) {

			logger.info("Erreur lors de la création de l'url d'accès à la portlet création personne", e);

		}

		request.setAttribute("urlCreation", url);

	}

	@ActionMapping("refresh")
	public void refresh(@RequestParam String source, ActionRequest request, ActionResponse response, SessionStatus status) {

		status.setComplete();

		response.setRenderParameter("action", source);

	}

	@Override
    public void setPortletContext(PortletContext ctx) {
		this.portletContext = ctx;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletConfigAware#setPortletConfig(javax.portlet.PortletConfig)
	 */
	@Override
    public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig  = portletConfig;

	}

}
