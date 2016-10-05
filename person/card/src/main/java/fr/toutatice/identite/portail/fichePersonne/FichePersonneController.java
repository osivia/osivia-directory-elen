package fr.toutatice.identite.portail.fichePersonne;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.login.IUserDatasModuleRepository;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.identite.portail.fichePersonne.formulaire.FormChgtMdp;
import fr.toutatice.identite.portail.fichePersonne.formulaire.FormUpload;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.FetchDocumentCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.GetUserProfileCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.QueryDocumentCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.UpdateProfilCommand;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.ResourceUtil;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({ "fiche" })
public class FichePersonneController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

	/**
	 * 
	 */
	private static final String LDAP_PORTLET_PERSON_CARD = "ldap.portlet.personCard";
	
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");
	protected static final Log logModifLdap = LogFactory.getLog("fr.toutatice.annuaire.modif");

	@Autowired
	private FichePersonneConfig config;

	@Autowired
	private PersonService personService;
	
	@Autowired
	private HabilitationModifFiche habilitationModifFiche;


	private PortletContext portletContext;
	private PortletConfig portletConfig;


	@ModelAttribute("formUpload")
	public FormUpload getFormUpload() {
		return new FormUpload();
	}

	@ModelAttribute("formChgtMdp")
	public FormChgtMdp getFormChgtMdp() {
		return new FormChgtMdp();
	}


	@Autowired
	private Validator chgtMdpValidator;


	@Autowired
	private Validator modifyValidator;


	@PostConstruct
	public void initNuxeoService() throws Exception {
		super.init();
		if ((this.portletContext != null) && (this.portletContext.getAttribute("nuxeoService") == null)) {

			this.init(this.portletConfig);
		}

	}

	/**
	 * Initialise la fiche personne avec le user connecté et le user recherché
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws ToutaticeAnnuaireException
	 * @throws CMSException
	 */
	@ModelAttribute("fiche")
	public Fiche initFichePerson(PortletRequest request, PortletResponse response) throws CMSException {

		//TODO replace with ATTR_LOGGED_PERSON_2
		Person userConnecte = (Person) request.getAttribute("osivia.directory.v2.loggedPerson");

//	TODO gérer les déconnectés

		Fiche fiche = new Fiche();

		PortalWindow window = WindowFactory.getWindow(request);
		String uid = window.getProperty("uidFichePersonne");

		Person userConsulte = null;
		// Consultation d'une autre personne, soit en anonyme, soit connecté et dans ce cas l'UID est différent de soi
		if ((userConnecte == null) || ((uid != null) && !(uid.equals(userConnecte.getUid())))) {

			userConsulte = personService.getPerson(uid);

		} else {
			userConsulte = userConnecte;
		}
		if(userConnecte.getUid().equals(userConsulte.getUid())){
			fiche.setSelf(true);
		}

		fiche.setUserConsulte(userConsulte);
		fiche.setLevelUserConnecteModifFiche(this.habilitationModifFiche.findRoleUser(userConnecte, userConsulte));

		
		fiche.setAvatar(userConsulte.getAvatar());

		// chargement des données de profil présentes dans nuxeo
		ProfilNuxeo profilNx = this.initProfilNuxeo(userConnecte, fiche.getUserConsulte(), request, response);
		if (profilNx != null) {
			fiche.setProfilNuxeo(profilNx);
		}

		return fiche;
	}

	@RenderMapping
	public String showFichePersonne(@ModelAttribute("fiche") Fiche fiche, RenderRequest request, RenderResponse response) throws PortalException, CMSException {

		String retour;
		
		fiche.setIdConsulte(fiche.getUserConsulte().getUid());

		retour = "fichePersonne";
		return retour;
	}

	@RenderMapping(params = "action=chgtMdp")
	public String showChgtMdp(@ModelAttribute("fiche") Fiche fiche, RenderRequest request, RenderResponse response, PortletSession session)
			throws PortalException, CMSException {

		
		response.setTitle("Modifier le mot de passe");
		
		String retour = null;

		if (fiche.getLevelUserConnecteModifFiche().equals(HabilitationModifFiche.Level.DROITMODIF)) {
			retour = "chgtMdp";
		} else {
			PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);
			this.addNotification(pcc, "label.modifNonAutorise", NotificationsType.WARNING);
			retour = this.showFichePersonne(fiche, request, response);
		}

		return retour;
	}


	@RenderMapping(params = "action=modify")
	public String showUpdateFiche(@ModelAttribute("fiche") Fiche fiche, @ModelAttribute("formUpload") FormUpload formUpload, RenderRequest request, RenderResponse response) throws 
			CMSException {
		
		response.setTitle("Modifier "+fiche.getUserConsulte().getCn());

		// si chargement d'une nouvelle photo de profil non encore enregistrée
		if (!fiche.getTmpFile().trim().isEmpty()) {
			ResourceURL res = response.createResourceURL();
			res.setParameter("type", "avatartmp");
			fiche.setAvatar(new Link(res.toString(), true));

		}

		// contournemnt Spring, le textarea et les select n'acceptent pas de value par défaut
		formUpload.setBio(fiche.getProfilNuxeo().getBio());
		formUpload.setTitle(fiche.getUserConsulte().getTitle());

		String retour = "modifFiche";

		return retour;
	}

	@ActionMapping(params = "action=submit")
	public void submitModif(@ModelAttribute("formUpload") FormUpload formUpload, BindingResult result, @ModelAttribute("fiche") Fiche fiche,
			ActionRequest request, ActionResponse response, SessionStatus status, ModelMap model) throws Exception {
		// le boolean chargementAvatar permet de savoir si l'utilisateur vient
		// de demander le chargement d'un avatar, ou si il a demandé
		// d'enregistrer ses modifications
		// la JSP n'a qu'un seul formulaire pour ces 2 actions. Un JS positionne
		// le booleen sur un onChange du champ input File

		if (formUpload.isChargementAvatar()) {
			// chargement du fichier image pour affichage
			formUpload.setChargementAvatar(false);
			this.uploadFile(fiche, formUpload, result, request, response);
		} else {
			// enregistrement des modifications
			this.modifyValidator.validate(formUpload, result);

			if (result.hasErrors()) {
				PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);
				//List<FieldError> tmp = result.getFieldErrors("nouveauEmail");
				List<FieldError> tmp = result.getFieldErrors();
				int i = tmp.size();
				for(FieldError err : tmp){
					this.addNotification(pcc, err.getCode(), NotificationsType.ERROR);
				}
				// mémorisation des informations saisies par l'utilisateur pour
				// affichage
				this.mergeProperties(fiche.getUserConsulte(), formUpload);
				BeanUtils.copyProperties(fiche.getProfilNuxeo(), formUpload);

				response.setRenderParameter("action", "modify");
			}else{

				
				Person userConnecte = (Person) request.getAttribute("osivia.directory.v2.loggedPerson");
				this.updatefichePersonne(fiche, userConnecte, formUpload, result, request, response, status, model);


			}
		}
	}

	private void uploadFile(Fiche fiche, FormUpload formUpload, BindingResult result, ActionRequest request, ActionResponse response) throws Exception {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		MultipartFile file = formUpload.getFile();

		if (!file.getContentType().split("/")[0].equals("image")) {

			this.addNotification(pcc, "label.noImageFile", NotificationsType.ERROR);
		} else {

			InputStream inputStream = file.getInputStream();

			File tmp = File.createTempFile("avatar", "");
			tmp.deleteOnExit();

			FileOutputStream fos = new FileOutputStream(tmp);

			byte[] buf = new byte[8192];
			while (true) {
				int length = inputStream.read(buf);
				if (length < 0) {
                    break;
                }
				fos.write(buf, 0, length);
			}

			fos.close();

			fiche.setTypeMIME(file.getContentType());
			fiche.setTmpFile(tmp.getAbsolutePath());
			fiche.setAvatar(new Link(fiche.getTmpFile(), false));

		}

		// mémorisation des autres informations éventuellement modifiées par
		// l'utilisateur
		BeanUtils.copyProperties(fiche.getProfilNuxeo(), formUpload);

		this.mergeProperties(fiche.getUserConsulte(), formUpload);

		response.setRenderParameter("action", "modify");

	}

	private void updatefichePersonne(Fiche fiche, Person userConnecte, FormUpload formUpload, BindingResult result, ActionRequest request, ActionResponse response,
			SessionStatus status, ModelMap model) throws NamingException {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);


			if (fiche == null) {
				response.setRenderParameter("action,", "fichePersonne");
				this.addNotification(pcc, "label.erreurUpdate", NotificationsType.ERROR);
			} else {
				//
				try {
					// MAJ Annuaire
					Person p = personService.getPerson(fiche.getUserConsulte().getUid());

					this.mergeProperties(p, formUpload);

					personService.update(p);

					logModifLdap.debug("L'utilisateur " + fiche.getUserConsulte().getUid() + " a modifié ses informations");

					// MAJ Nuxeo
					// TODO ça sert a quoi ?
					// String s = formUpload.getBio().replaceAll("\n",
					// "l1n3bR3aKK");
					// String s2 = Jsoup.clean(s, Whitelist.none());
					// String bio = s2.replaceAll(" l1n3bR3aKK", "\n");

					BeanUtils.copyProperties(fiche.getProfilNuxeo(), formUpload);

					NuxeoController nuxeoCtl = new NuxeoController(request, response, this.portletContext);

					//Si modification par un admin, passage en mode SuperUser
//					if((fiche.getLevelUserConnecteModifFiche()==Level.DROITMODIF) && !(fiche.getUserConsulte().getUid().equalsIgnoreCase(userConnecte.getUid()))){
//						nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
//					}

					Document doc = this.loadFiche(fiche.getUserConsulte(), request, response, fiche.getProfilNuxeo().getPathUserWorkspace());
					String pathNx = doc.getPath();
					File tmp = null;
					if ((fiche.getTmpFile() == null) || !fiche.getTmpFile().trim().isEmpty()) {
						tmp = new File(fiche.getTmpFile());

					}

					nuxeoCtl.executeNuxeoCommand(new UpdateProfilCommand(nuxeoCtl, pathNx, fiche.getProfilNuxeo(), tmp));
					fiche.setTypeMIME("");
					fiche.setTmpFile("");

					// used to notify the cache avatar to reload the picture
					nuxeoCtl.refreshUserAvatar(p.getUid());

					request.setAttribute("osivia.updateContents", "true");

					status.setComplete();

					// Contournement Spring pour s'assurer que le model n'a plus de trace de l'ancienne fiche
					model.remove("fiche");

					response.setRenderParameter("action", "fichePersonne");
					response.setRenderParameter("uidFichePersonne", p.getUid());

					this.addNotification(pcc, "label.updateOk", NotificationsType.SUCCESS);

					// ========= maj user connecte

					if (fiche.isSelf()) {
						IUserDatasModuleRepository userRepo = Locator.findMBean(IUserDatasModuleRepository.class, IUserDatasModuleRepository.MBEAN_NAME);
						userRepo.reload(request);
					}

				} catch (Exception e) {
					response.setRenderParameter("action", "modify");

					this.addNotification(pcc, "label.erreurUpdate", NotificationsType.ERROR);
					logger.error("impossible de mettre a jour la fiche", e);
				}

			}


	}

	private Person mergeProperties(Person p, FormUpload formUpload) {

		p.setMail(StringUtils.trimToNull(formUpload.getNouveauEmail()));
		
		p.setTitle(StringUtils.trimToNull(formUpload.getTitle()));
		
		if (formUpload.getSn() != null) {
			p.setSn(formUpload.getSn());
		}
		if (formUpload.getGivenName() != null) {
			p.setGivenName(formUpload.getGivenName());
		}
		if((formUpload.getSn() != null) && (formUpload.getGivenName() != null)) {
			String fullName = formUpload.getGivenName() + " " + formUpload.getSn();
			p.setCn(fullName);
			p.setDisplayName(fullName);
		}

		return p;
	}

	private Document loadFiche(Person p, PortletRequest request, PortletResponse response, String pathUserWorkspace) {
		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);
		Document doc = null;

		NuxeoController nuxeoCtl = new NuxeoController(request, response, this.portletContext);

		try {
			// recherche du document profil du user
			String requete = "ecm:isProxy=0 and ecm:primaryType='UserProfile' and ecm:path STARTSWITH'" + pathUserWorkspace + "'";
			nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			nuxeoCtl.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
			Documents results = (Documents) nuxeoCtl.executeNuxeoCommand(new QueryDocumentCommand(requete));
			if (!results.isEmpty()) {
				// le profil nuxeo existe déjà
				doc = results.get(0);
			} else {

				Person personConnect = (Person) request.getAttribute("osivia.directory.v2.loggedPerson");

				if (p.getUid().equals(personConnect.getUid())) {
					// création du userProfile si c'est celui de l'utilisateur
					// connecté
					Document userProfile = (Document) nuxeoCtl.executeNuxeoCommand(new GetUserProfileCommand());
					userProfile = (Document) nuxeoCtl.executeNuxeoCommand(new FetchDocumentCommand(nuxeoCtl, userProfile.getPath()));
				}
				if (doc == null) {

					this.addNotification(pcc, "label.noNuxeoProfil", NotificationsType.ERROR);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			doc = null;
		}
		return doc;
	}

	private ProfilNuxeo initProfilNuxeo(Person userConnecte, Person p, PortletRequest request, PortletResponse response) throws CMSException {
		NuxeoController nuxeoCtl = new NuxeoController(request, response, this.portletContext);
		ProfilNuxeo profilNuxeo = new ProfilNuxeo();
		Document userProfile = null;

		userProfile = (Document) nuxeoCtl.executeNuxeoCommand(new GetUserProfileCommand(p.getUid()));
		int i = userProfile.getPath().lastIndexOf("/");
		profilNuxeo.setPathUserWorkspace(userProfile.getPath().substring(0, i));
		// opération Fetch pour récupérer les propriétés du document non
		// ramenées par le getUserProfile
		userProfile = (Document) nuxeoCtl.executeNuxeoCommand(new FetchDocumentCommand(nuxeoCtl, userProfile.getPath()));

		if (userProfile != null) {
            profilNuxeo.setBio(userProfile.getString("ttc_userprofile:bio"));
            profilNuxeo.setTelFixe(userProfile.getString("userprofile:phonenumber"));
            profilNuxeo.setTelMobile(userProfile.getString("ttc_userprofile:mobile"));
            profilNuxeo.setProfession(userProfile.getString("ttc_userprofile:profession"));

			String requete = "ecm:isProxy=0 and ecm:primaryType='BlogSite' and ecm:currentLifeCycleState <> 'deleted' and ecm:path STARTSWITH'"
					+ profilNuxeo.getPathUserWorkspace() + "'";
			Documents results = (Documents) nuxeoCtl.executeNuxeoCommand(new QueryDocumentCommand(requete));
			profilNuxeo.setListeBlogs(results);
		}

		return profilNuxeo;
	}


	@ActionMapping(params = "action=annuler")
	public void annuler(ActionResponse response, SessionStatus status, ModelMap model) throws Exception {

		// Contournement Spring pour s'assurer que le model n'a plus de trace de l'ancienne fiche
		model.remove("fiche");


		status.setComplete();

		response.setRenderParameter("action", "fichePersonne");
	}

	@ActionMapping(params = "action=updateMdp")
	public void updateChgtMdp(@ModelAttribute("fiche") Fiche fiche, @ModelAttribute FormChgtMdp formChgtMdp, BindingResult result, ActionRequest request,
			ActionResponse response) {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		this.chgtMdpValidator.validate(formChgtMdp, result);

		if (!result.hasErrors()) {

			if (fiche == null) {
				response.setRenderParameter("action,", "fichePersonne");
				// modelMap.addAttribute("messageErreur",this.messages.getMessage("label.erreurUpdateMdp",
				// null,null));
				this.addNotification(pcc, "label.erreurUpdateMdp", NotificationsType.ERROR);
			} else {

				if (!personService.verifyPassword(fiche.getUserConsulte().getUid(), formChgtMdp.getMdpActuel())) {
					// modelMap.put("messageErreur","Mot de passe erroné");
					this.addNotification(pcc, "label.mauvaisMdp", NotificationsType.ERROR);

					response.setRenderParameter("action", "chgtMdp");
				} else {
					// Suppression de la sucharge de mdp si nécessaire
//					if (fiche.getUserConsulte().isUserSurcharged() && this.config.getDeleteSurchageWhenUpdatePwd()) {
//						if (logger.isDebugEnabled()) {
//                            logger.debug("delete surcharge mot de passe");
//                        }
//						fiche.getUserConsulte().deleteSurcharge();
//					}
					// Modification du mot de passe
					personService.updatePassword(fiche.getUserConsulte(), formChgtMdp.getNouveauMdp());
					response.setRenderParameter("action", "fichePersonne");
					// modelMap.addAttribute("fiche", fiche);
					// modelMap.addAttribute("messageInfo",this.messages.getMessage("label.updateMdp",
					// null,null));
					this.addNotification(pcc, "label.updateMdp", NotificationsType.SUCCESS);

					logModifLdap.info("L'utilisateur " + fiche.getUserConsulte().getUid() + " a modifié son mot de passe");
				}

			}

		} else {
			response.setRenderParameter("action", "chgtMdp");
		}

	}


	private PersonUrl buildUrlPerson(Person p, PortalControllerContext portalControllerContext) throws Exception{

		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.hideTitle", "1");
		windowProperties.put("uidFichePersonne", p.getUid());

		String url = this.getPortalUrlFactory().getStartPortletUrl(portalControllerContext, System.getProperty(LDAP_PORTLET_PERSON_CARD),
				windowProperties, false);

		return new PersonUrl(p,url);
	}


	@ResourceMapping
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse, PortletSession session) throws PortletException, IOException {

		if ("avatartmp".equals(resourceRequest.getParameter("type"))) {
			Fiche fiche = (Fiche) session.getAttribute("fiche");
			File tmp = new File(fiche.getTmpFile());
			// Les headers doivent être positionnées avant la réponse
			resourceResponse.setContentType(fiche.getTypeMIME());
			resourceResponse.setProperty("Content-Disposition", "attachment; filename=\"" + tmp.getName() + "\"");

			ResourceUtil.copy(new FileInputStream(tmp), resourceResponse.getPortletOutputStream(), 4096);

			resourceResponse.setProperty("Cache-Control", "max-age=" + resourceResponse.getCacheControl().getExpirationTime());

			resourceResponse.setProperty("Last-Modified", this.formatResourceLastModified());

			return;

		}

		super.serveResource(resourceRequest, resourceResponse);

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
