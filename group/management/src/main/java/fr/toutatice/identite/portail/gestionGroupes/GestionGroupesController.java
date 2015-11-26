package fr.toutatice.identite.portail.gestionGroupes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.portlet.PortalGenericPortlet;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.services.directory.helper.DirectoryPortlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;



@Controller
@RequestMapping("VIEW")
@SessionAttributes({ "formulaire", "level", "orgaUserConnecte" })

public class GestionGroupesController extends PortalGenericPortlet implements PortletContextAware {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.identite");
	private PortletContext portletContext;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Person person;

	@Autowired
	private Profil profil;

	@Autowired
	private Organisation organisation;

	@Autowired
	private Habilitation habilitation;


	public Formulaire init(ModelMap model, PortletRequest request) {


		DirectoryPerson personConnect = (DirectoryPerson) request
				.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = this.person.findUtilisateur(personConnect.getUid());

		// model.addAttribute("userConnecte",userConnecte);

		if(userConnecte!=null){
			Habilitation.level level = this.habilitation.findRoleUser(userConnecte);
			model.addAttribute("level",level);

			List<Organisation> orgaUserConnecte = this.organisation.findOrganisationPersonneByProfil(userConnecte);
			Organisation toutesLesOrgas = this.context.getBean("organisation",
					Organisation.class);
			toutesLesOrgas.setId("");
			toutesLesOrgas.setDescription("Toutes mes organisations");
			orgaUserConnecte.add(0,toutesLesOrgas);
			model.addAttribute("orgaUserConnecte",orgaUserConnecte);
		}

		Formulaire formulaire = new Formulaire();
		String rneReference = this.findRneReference(request);
		if((rneReference==null)||rneReference.trim().isEmpty()){
			formulaire.setListeMesGroupes(userConnecte.findProfilsAssocies());
			formulaire.setListeGroupesGeres(userConnecte.findProfilsGeres());
		}else{
			formulaire.setListeMesGroupes(userConnecte.findProfilsAssociesByOrga(rneReference));
			formulaire.setListeGroupesGeres(userConnecte.findProfilsGeresParOrga(rneReference));
			formulaire.setContexteOrga(true);
			formulaire.setFiltreOrga(rneReference);
		}

		//Suppression des groupes de type space-group
		List<Profil> lstTmp = new ArrayList<Profil>();
		for(Profil p:formulaire.getListeMesGroupes()){
			if((p.getType()==null)||((p.getType()!=null) && !p.getType().equalsIgnoreCase("space-group"))){
				lstTmp.add(p);
			}
		}
		formulaire.setListeMesGroupes(lstTmp);

		List<Profil> lstTmp1 = new ArrayList<Profil>();
		for(Profil p:formulaire.getListeGroupesGeres()){
			if((p.getType()==null)||((p.getType()!=null) && !p.getType().equalsIgnoreCase("space-group"))){
				lstTmp1.add(p);
			}
		}
		formulaire.setListeGroupesGeres(lstTmp1);

		model.addAttribute("formulaire",formulaire);
		return formulaire;
	}

	/**
	 * Méthode invoquée à chaque nouvelle session ou sur expiration d'une session en cours
	 *
	 */
	@ModelAttribute("formulaire")
	public Formulaire getFormulaire(PortletRequest request, ModelMap model) {

		Formulaire formulaire = this.init(model, request);

		return formulaire;
	}



	@RenderMapping
	public String showGroupesGeres(RenderRequest request, ModelMap model, PortletSession session, RenderResponse response) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		// Person userConnecte = (Person) session.getAttribute("userConnecte");
		// if (userConnecte == null) {
		// userConnecte = initUserConnecte(model, request);
		// }
		// if(userConnecte==null){
		// return "nonConnecte";
		// }
		// else{

			Formulaire formulaire = (Formulaire) session.getAttribute("formulaire");
			if(formulaire==null) {
				formulaire = this.init(model, request);
			}
			this.setListeGroupesUrl(formulaire.getListeGroupesGeres(),request, response, model);



			if(formulaire.getListeGroupesGeres().size()<1){
				if(formulaire.isContexteOrga()){
					this.addNotification(pcc, "label.pasGroupeGereEspace", NotificationsType.INFO);
				}else{
					this.addNotification(pcc, "label.pasGroupeGere", NotificationsType.INFO);
				}

			}
			model.addAttribute("currentTab", "groupesGeres");
			return "groupesGeres";
		// }
	}


	@RenderMapping(params = "action=mesGroupes")
	public String showMesGroupes(RenderRequest request, ModelMap model, PortletSession session, RenderResponse response) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		// Person userConnecte = (Person) session.getAttribute("userConnecte");
		// if (userConnecte == null) {
		// userConnecte = initUserConnecte(model, request);
		// }
		Formulaire formulaire = (Formulaire) session.getAttribute("formulaire");
		if(formulaire==null) {
			formulaire = this.init(model, request);
		}
		this.setListeGroupesUrl(formulaire.getListeMesGroupes(), request, response, model);


		if(formulaire.getListeMesGroupes().size()<1){
			if(formulaire.isContexteOrga()){
				this.addNotification(pcc, "label.pasGroupeEspace", NotificationsType.INFO);
			}else{
				this.addNotification(pcc, "label.pasGroupe", NotificationsType.INFO);
			}

		}

		model.addAttribute("currentTab", "mesGroupes");
		return "mesGroupes";
	}



	@RenderMapping(params = "action=rechercheAvancee")
	public String showRechercheAvancee(RenderRequest request, ModelMap model, PortletSession session, RenderResponse response) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		// Person userConnecte = (Person) session.getAttribute("userConnecte");
		// if (userConnecte == null) {
		// userConnecte = initUserConnecte(model, request);
		// }
		Formulaire formulaire = (Formulaire) session.getAttribute("formulaire");
		if(formulaire==null) {
			formulaire = this.init(model, request);
			this.addNotification(pcc, "label.erreur", NotificationsType.ERROR);

		}
		this.setListeGroupesUrl(formulaire.getListeGroupesRecherches(),request, response, model);

		model.addAttribute("currentTab", "rechercheGroupe");

		return "rechercheGroupe";
	}


	@RenderMapping(params = "action=nonAutorise")
	public String showNonAutorise() {
		logger.debug("Entrée méthode nonAutorise");
		return "nonAutorise";
	}

	@RenderMapping(params = "action=filtrerGroupesGeresByOrga")
    public String filtrerGroupesGeresByOrga(@RequestParam("idOrga") String idOrga, RenderRequest request, RenderResponse response, final ModelMap model, PortletSession session) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		// Person userConnecte = (Person) session.getAttribute("userConnecte");
		// if (userConnecte == null) {
		// userConnecte = initUserConnecte(model, request);
		// }
		Formulaire formulaire = (Formulaire) session.getAttribute("formulaire");
		if(formulaire==null) {
			formulaire = this.init(model, request);
			this.addNotification(pcc, "label.erreur", NotificationsType.ERROR);
		}

		DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = this.person.findUtilisateur(personConnect.getUid());

        formulaire.setFiltreOrga(idOrga);
        if (! idOrga.equals("")) {
        	formulaire.setListeGroupesGeres(userConnecte.findProfilsGeresParOrga(idOrga));
        } else {
        	formulaire.setListeGroupesGeres(userConnecte.findProfilsGeres());
        }

        this.setListeGroupesUrl(formulaire.getListeGroupesGeres(),request, response, model);

		model.addAttribute("currentTab", "groupesGeres");
        return "groupesGeres";
    }

	@RenderMapping(params = "action=filtrerMesGroupesByOrga")
    public String filtrerMesGroupesByOrga(@RequestParam("idOrga") String idOrga, RenderRequest request, RenderResponse response, final ModelMap model, PortletSession session) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		// Person userConnecte = (Person) session.getAttribute("userConnecte");
		// if (userConnecte == null) {
		// userConnecte = initUserConnecte(model, request);
		// }
		Formulaire formulaire = (Formulaire) session.getAttribute("formulaire");
		if(formulaire==null) {
			formulaire = this.init(model, request);
			this.addNotification(pcc, "label.erreur", NotificationsType.ERROR);
		}

		DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = this.person.findUtilisateur(personConnect.getUid());
        formulaire.setFiltreOrga(idOrga);
        if (! idOrga.equals("")) {
        	formulaire.setListeMesGroupes(userConnecte.findProfilsAssociesByOrga(idOrga));
        } else {
        	formulaire.setListeMesGroupes(userConnecte.findProfilsAssocies());
        }

        this.setListeGroupesUrl(formulaire.getListeMesGroupes(), request, response, model);

        return "mesGroupes";
    }

	@ActionMapping(params="action=rechercheGroupe")
    public void rechercheGroupe(@ModelAttribute Formulaire formulaire, BindingResult result, ActionRequest request, ActionResponse response, ModelMap modelMap, PortletSession session, final ModelMap model) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		String filtre = formulaire.getFiltre();
		if(filtre.length() >= 3) {
			formulaire.setListeGroupesRecherches((this.profil.findProfilByDebutCn("*"+filtre)));
			if (formulaire.getListeGroupesRecherches().size() < 1) {

				this.addNotification(pcc, "label.noresult", NotificationsType.INFO);
			}
		} else{

			this.addNotification(pcc, "label.3charmin", NotificationsType.WARNING);

			formulaire.setListeGroupesRecherches(new ArrayList<Profil>());
		}

		response.setRenderParameter("action", "rechercheAvancee");
        model.addAttribute("formulaire", formulaire);
    }



	public String rafraichir(@RequestParam String source, RenderRequest request, ModelMap model, PortletSession session, RenderResponse response) throws Exception {
		// Person userConnecte = initUserConnecte(model, request);
		Formulaire formulaire = this.init(model, request);


		if (source.equals("mesGroupes")) {
			this.setListeGroupesUrl(formulaire.getListeMesGroupes(), request, response, model);
			// request.setAttribute("dernierePage", rowcount <
			// formulaire.getListeMesGroupes().size());
		}
		if (source.equals("groupesGeres")) {
			this.setListeGroupesUrl(formulaire.getListeGroupesGeres(), request, response, model);
			// request.setAttribute("dernierePage", rowcount <
			// formulaire.getListeGroupesGeres().size());
		}
		if (source.equals("rechercheGroupe")) {
			this.setListeGroupesUrl(formulaire.getListeGroupesRecherches(), request, response, model);
			// request.setAttribute("dernierePage", rowcount <
			// formulaire.getListeGroupesRecherches().size());
		}


		return source;
	}


	private void setListeGroupesUrl(List<Profil> listeProfil, RenderRequest request, RenderResponse response, ModelMap model) throws Exception{
		List<ProfilUrl> liste = new ArrayList<ProfilUrl>();
		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
		for(Profil p : listeProfil) {
			if(p.getDescription()!=null){
				p.setDescription(Jsoup.parse(p.getDescription()).text());
			}
			if(p.getDisplayName()!=null){
				p.setDisplayName(Jsoup.parse(p.getDisplayName()).text());
			}
			String url = "";
            // if(portalUrlFactory==null){
            // portalUrlFactory = (IPortalUrlFactory) portletContext.getAttribute("UrlService");
            // }
			Map<String, String> windowProperties = new HashMap<String, String>();
			windowProperties.put("osivia.ajaxLink", "1");
			windowProperties.put("osivia.title", "Groupe : "+p.getDisplayName());
			windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
			windowProperties.put("osivia.hideTitle", "1");
			windowProperties.put("cnProfil", p.getCn());

            url = this.getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.ficheProfil.getInstanceName(), windowProperties,
                    false);
			liste.add(new ProfilUrl(p,url));

		}
		Collections.sort(liste);
		model.addAttribute("listeGroupeUrl",liste);
	}





	public String findRneReference(PortletRequest request) {
		PortalWindow window = WindowFactory.getWindow(request);

		String rneReference=null;
		// Récupération du RNE de l'espace si la portlet se trouve dans un espace nuxeo
			NuxeoController nuxeoCtl = new NuxeoController(request, null, this.portletContext);
	        String spacePath = window.getPageProperty("osivia.cms.basePath");
	        CMSItem publishSpaceConfig = null;
	        if( spacePath != null)  {
	             try {
					publishSpaceConfig = nuxeoCtl.getCMSService().getSpaceConfig(nuxeoCtl.getCMSCtx(), spacePath);
					Document doc = (Document) publishSpaceConfig.getNativeItem();
		            rneReference = (String) doc.getProperties().get("dc:publisher");
				} catch (CMSException e) {
					logger.warn("Erreur lors de la récupération du SpaceConfig dans le menuApplication");
				}

	        }

	     if((rneReference==null)||rneReference.trim().isEmpty()){
	    	 rneReference =  window.getProperty("toutatice.identite.gestionGroupes.rne");
	     }
	     return rneReference;
	}



	@Override
    public void setPortletContext(PortletContext ctx) {
		this.portletContext = ctx;
	}

}

