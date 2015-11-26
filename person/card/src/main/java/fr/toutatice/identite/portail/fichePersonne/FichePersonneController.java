package fr.toutatice.identite.portail.fichePersonne;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.login.IUserDatasModuleRepository;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.directory.helper.DirectoryPortlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import fr.toutatice.identite.portail.fichePersonne.HabilitationModifFiche.Level;
import fr.toutatice.identite.portail.fichePersonne.formulaire.FormChgtMdp;
import fr.toutatice.identite.portail.fichePersonne.formulaire.FormSurchargeMdp;
import fr.toutatice.identite.portail.fichePersonne.formulaire.FormUpload;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.FetchDocumentCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.GetUserProfileCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.QueryDocumentCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.QueryUserWorkspaceCommand;
import fr.toutatice.identite.portail.fichePersonne.nuxeo.UpdateProfilCommand;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.Structure;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.ResourceUtil;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({ "fiche" })
public class FichePersonneController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");
	protected static final Log logModifLdap = LogFactory.getLog("fr.toutatice.annuaire.modif");

	@Autowired
	private FichePersonneConfig config;

	@Autowired
	private Person personne;
	@Autowired
	private Structure structure;

	@Autowired
	private Profil profil;

	@Autowired
	private HabilitationSurcharge habilitationSurcharge;

	@Autowired
	private HabilitationRazMdp habilitationRazMdp;

	@Autowired
	private HabilitationModifMailPwd habilitationModifMailPwd;

	@Autowired
	private HabilitationModifFiche habilitationModifFiche;

	@Autowired
	private HabilitationConsultation habilitationConsultation;

	private PortletContext portletContext;
	private PortletConfig portletConfig;

	private JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@ModelAttribute("formUpload")
	public FormUpload getFormUpload() {
		return new FormUpload();
	}

	@ModelAttribute("formChgtMdp")
	public FormChgtMdp getFormChgtMdp() {
		return new FormChgtMdp();
	}

	@ModelAttribute("formSurchargeMdp")
	public FormSurchargeMdp getFormSurchargeMdp() {
		return new FormSurchargeMdp();
	}

	@Autowired
	private Validator chgtMdpValidator;

	@Autowired
	private Validator surchargeMdpValidator;

	@Autowired
	private Validator modifyValidator;

	public FichePersonneController() {
		super();

	}

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
	public Fiche initFichePerson(PortletRequest request, PortletResponse response) throws ToutaticeAnnuaireException, CMSException {

		DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);

		Person userConnecte = null;

		if(personConnect != null) {
			userConnecte = this.personne.findUtilisateur(personConnect.getUid());
		}

		Fiche fiche = new Fiche();

		PortalWindow window = WindowFactory.getWindow(request);
		String uid = window.getProperty("uidFichePersonne");

		Link avatar = null;

		Person userConsulte = null;
		// Consultation d'une autre personne, soit en anonyme, soit connecté et dans ce cas l'UID est différent de soi
		if ((userConnecte == null) || ((uid != null) && !(uid.equals(userConnecte.getUid())))) {

			DirectoryPerson personConsult = this.getDirectoryService().getPerson(uid);
			avatar = personConsult.getAvatar();
			userConsulte = this.personne.findUtilisateur(uid);
		} else {
			avatar = personConnect.getAvatar();
			userConsulte = userConnecte;
		}
		if(userConnecte.getUid().equals(userConsulte.getUid())){
			fiche.setSelf(true);
		}

		fiche.setUserConsulte(userConsulte);
		fiche.setListeEtb(this.structure.findStructuresPersonneByProfil(userConsulte));
		fiche.setListeProfils(this.profil.findProfilsPersonne(userConsulte));
		fiche.setLevelUserConnecteSurcharge(this.habilitationSurcharge.findRoleUser(userConnecte, userConsulte));
		fiche.setLevelUserConnecteRazMdp(this.habilitationRazMdp.findRoleUser(userConnecte, userConsulte));
		fiche.setLevelUserConnecteModifPwdMail(this.habilitationModifMailPwd.findRoleUser(userConnecte, userConsulte));
		fiche.setLevelConsultation(this.habilitationConsultation.getLevelConsultation(userConnecte, userConsulte));
		fiche.setLevelUserConnecteModifFiche(this.habilitationModifFiche.findRoleUser(userConnecte, userConsulte));

		if(userConsulte.getTitle().equalsIgnoreCase("ELE")){
			fiche.setClasse(this.profil.findClasseEleve(userConsulte));
			List<Person> listeParents = userConsulte.findParents();
			List<PersonUrl> listeParentsUrl = new ArrayList<PersonUrl>();
			PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
			for(Person p:listeParents){
				try{
					listeParentsUrl.add(this.buildUrlPerson(p, portalControllerContext));
				}catch(Exception e){
					logger.error(e);
				}
			}
			fiche.setListeParents(listeParentsUrl);
		}

		fiche.setAvatar(avatar);

		// chargement des données de profil présentes dans nuxeo
		ProfilNuxeo profilNx = this.initProfilNuxeo(userConnecte, fiche.getUserConsulte(), request, response);
		if (profilNx != null) {
			fiche.setProfilNuxeo(profilNx);
		}



		// Traduction entité
		NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

        Map<String, VocabularyEntry> children;
        try {
            VocabularyEntry vocabularyEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, "Organization");
            children = vocabularyEntry.getChildren();
        } catch (NuxeoException e) {
            logger.error(e.getMessage(), e);
            children = new HashMap<String, VocabularyEntry>(0);
        }
		Map<String, String> listeDptCns = new LinkedHashMap<String, String>();
		listeDptCns.put("", "");

		for(Map.Entry<String, VocabularyEntry>  entry : children.entrySet()) {
			listeDptCns.put(entry.getValue().getId(), entry.getValue().getLabel());
		}

		fiche.setListeDptCns(listeDptCns);


		if(userConsulte.getDivcod() != null) {
			fiche.setDepartementCns(listeDptCns.get(userConsulte.getDivcod()));
		}



		return fiche;
	}

	@RenderMapping
	public String showFichePersonne(@ModelAttribute("fiche") Fiche fiche, RenderRequest request, RenderResponse response) throws ToutaticeAnnuaireException,
			PortalException, CMSException {

		String retour;

		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
		this.setListeProfilUrlModel(fiche.getUserConsulte().getListeProfils(), portalControllerContext);

		// Pour les élèves, on affiche un message d'erreur si le mot de passe n'a pas été changé
		// TODO paramétriser cette alerte
		if (fiche.isSelf() && fiche.getUserConsulte().getTitle().equalsIgnoreCase("ELE")) {
			boolean mdpModifie = fiche.getUserConsulte().eleveHasChangedPassword();
			if (!mdpModifie && fiche.isSelf()) {
				this.addNotification(portalControllerContext, "label.mdpNonModifie", NotificationsType.ERROR);

			}
			if(fiche.getUserConsulte().getEmail().trim().isEmpty()){
				this.addNotification(portalControllerContext, "label.emailNonSaisi", NotificationsType.ERROR);
			}
		}

		// Avertissement à l'assistance si un profil n'est pas surchargeable
		if(fiche.getLevelUserConnecteSurcharge().equals(HabilitationSurcharge.level.NON_SURCHARGEABLE)) {
			this.addNotification(portalControllerContext, "warn.surchargeNonAutorise", NotificationsType.INFO);
		}

		response.setTitle(fiche.getUserConsulte().getCn());

		if (this.config.getEnableModeENT()) {
			if (fiche.getUserConsulte().isParent()) {
				int i = fiche.getUserConsulte().getUid().toLowerCase().indexOf("@aten");
				if(i>0){
					fiche.setIdConsulte(fiche.getUserConsulte().getUid().substring(0, i));
				}

				List<Enfant> liste = new ArrayList<Enfant>();
				for (String uid : fiche.getUserConsulte()
						.getListeUidElevesConcernes()) {
					Person p = this.personne.findUtilisateur(uid);
					Structure etb = this.structure.findStructure(p.getListeRnes()
							.get(0));
					liste.add(new Enfant(p, etb));
				}
				fiche.setListeEnfants(liste);
			}

			else if (fiche.getUserConsulte().getUid().toLowerCase().indexOf("@agri") > -1) {
				int i = fiche.getUserConsulte().getUid().toLowerCase().indexOf("@agri");
				fiche.setIdConsulte(fiche.getUserConsulte().getUid()
						.substring(0, i));
			} else {
				fiche.setIdConsulte(fiche.getUserConsulte().getUid());
			}
		} else {
			fiche.setIdConsulte(fiche.getUserConsulte().getUid());
		}


		retour = "fichePersonne";
		return retour;
	}

	@RenderMapping(params = "action=chgtMdp")
	public String showChgtMdp(@ModelAttribute("fiche") Fiche fiche, RenderRequest request, RenderResponse response, PortletSession session)
			throws ToutaticeAnnuaireException, PortalException, CMSException {

		String retour = null;

		if (fiche.getLevelUserConnecteModifPwdMail().equals(HabilitationModifMailPwd.Level.DROITMODIF)) {
			retour = "chgtMdp";
		} else {
			PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);
			this.addNotification(pcc, "label.modifNonAutorise", NotificationsType.WARNING);
			retour = this.showFichePersonne(fiche, request, response);
		}

		return retour;
	}

	@RenderMapping(params = "action=surchargeMdp")
	public String showSurchargeMdp(@ModelAttribute("fiche") Fiche fiche, RenderRequest request, RenderResponse response) throws ToutaticeAnnuaireException,
			CMSException, PortalException {

		String retour = null;
		if (fiche.getLevelUserConnecteSurcharge().equals(HabilitationSurcharge.level.DROIT_SURCHARGE)
				|| fiche.getLevelUserConnecteSurcharge().equals(HabilitationSurcharge.level.DROIT_SURCHARGE_ASSISTANCE)) {
			retour = "surchargeMdp";
		} else {
			PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);
			this.addNotification(pcc, "label.surchargeNonAutorise", NotificationsType.WARNING);
			retour = this.showFichePersonne(fiche, request, response);
		}

		return retour;
	}

	@RenderMapping(params = "action=modify")
	public String showUpdateFiche(@ModelAttribute("fiche") Fiche fiche, @ModelAttribute("formUpload") FormUpload formUpload, RenderRequest request, RenderResponse response) throws ToutaticeAnnuaireException,
			CMSException {

		// si chargement d'une nouvelle photo de profil non encore enregistrée
		if (!fiche.getTmpFile().trim().isEmpty()) {
			ResourceURL res = response.createResourceURL();
			res.setParameter("type", "avatartmp");
			fiche.setAvatar(new Link(res.toString(), true));

		}

		// contournemnt Spring, le textarea et les select n'acceptent pas de value par défaut
		formUpload.setBio(fiche.getProfilNuxeo().getBio());
		formUpload.setDepartementCns(fiche.getUserConsulte().getDivcod());
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
				DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
				Person userConnecte = null;
				userConnecte = this.personne.findUtilisateur(personConnect.getUid());
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
			SessionStatus status, ModelMap model) throws ToutaticeAnnuaireException, NamingException {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);


			if (fiche == null) {
				response.setRenderParameter("action,", "fichePersonne");
				this.addNotification(pcc, "label.erreurUpdate", NotificationsType.ERROR);
			} else {
				//
				try {
					// MAJ Annuaire
					Person p = this.personne.findUtilisateur(fiche.getUserConsulte().getUid());

					this.mergeProperties(p, formUpload);

					p.update();

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
					if((fiche.getLevelUserConnecteModifFiche()==Level.DROITMODIF) && !(fiche.getUserConsulte().getUid().equalsIgnoreCase(userConnecte.getUid()))){
						nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
					}

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

		if (formUpload.getNouveauEmail() != null) {
			p.setEmail(formUpload.getNouveauEmail());
		}
		if (formUpload.getAlias() != null) {
			p.setAlias(formUpload.getAlias());
		}
		if (formUpload.getTitle() != null) {
			p.setTitle(formUpload.getTitle());
		}
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


		if (formUpload.getDepartementCns() != null) {
			p.setDivcod(formUpload.getDepartementCns());
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

				DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);

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

	private Document loadUserWorkspace(Person p, ModelMap model, PortletRequest request, PortletResponse response) {
		Document userWorkspace = null;
		NuxeoController nuxeoCtl = new NuxeoController(request, response, this.portletContext);

		// Recherche du userworkspace du user
		nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
		nuxeoCtl.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

		Documents results = (Documents) nuxeoCtl.executeNuxeoCommand(new QueryUserWorkspaceCommand(p.getUid()));
		if (!results.isEmpty()) {
			userWorkspace = results.get(0);
		}
		return userWorkspace;
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
			ActionResponse response) throws ToutaticeAnnuaireException {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		this.chgtMdpValidator.validate(formChgtMdp, result);

		if (!result.hasErrors()) {

			if (fiche == null) {
				response.setRenderParameter("action,", "fichePersonne");
				// modelMap.addAttribute("messageErreur",this.messages.getMessage("label.erreurUpdateMdp",
				// null,null));
				this.addNotification(pcc, "label.erreurUpdateMdp", NotificationsType.ERROR);
			} else {

				if (!fiche.getUserConsulte().verifMdp(formChgtMdp.getMdpActuel())) {
					// modelMap.put("messageErreur","Mot de passe erroné");
					this.addNotification(pcc, "label.mauvaisMdp", NotificationsType.ERROR);

					response.setRenderParameter("action", "chgtMdp");
				} else {
					// Suppression de la sucharge de mdp si nécessaire
					if (fiche.getUserConsulte().isUserSurcharged() && this.config.getDeleteSurchageWhenUpdatePwd()) {
						if (logger.isDebugEnabled()) {
                            logger.debug("delete surcharge mot de passe");
                        }
						fiche.getUserConsulte().deleteSurcharge();
					}
					// Modification du mot de passe
					fiche.getUserConsulte().updatePassword(formChgtMdp.getNouveauMdp());
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

	@ActionMapping(params = "action=razMdp")
	public void razMdp(@ModelAttribute("fiche") Fiche fiche, ActionRequest request, ActionResponse response) throws ToutaticeAnnuaireException {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);

		Person userConnecte = this.personne.findUtilisateur(personConnect.getUid());

		if (fiche == null) {
			response.setRenderParameter("action,", "fichePersonne");
			// model.addAttribute("messageErreur",this.messages.getMessage("label.erreurRaz",
			// null,null));
			this.addNotification(pcc, "label.erreurRaz", NotificationsType.ERROR);

		}

		if (fiche.getLevelUserConnecteRazMdp().equals(HabilitationRazMdp.level.DROITRAZ)) {
			// Suppression de la sucharge de mdp si nécessaire
			if (fiche.getUserConsulte().isUserSurcharged() && this.config.getDeleteSurchageWhenUpdatePwd()) {
				logger.debug("delete surcharge mot de passe");
				fiche.getUserConsulte().deleteSurcharge();
			}
			// Raz du mot de passe
			if (fiche.getUserConsulte().getTitle().equalsIgnoreCase("ELE")) {
				fiche.getUserConsulte().razMdp();
				response.setRenderParameter("action", "fichePersonne");

				this.addNotification(pcc, "label.razOk", NotificationsType.SUCCESS);

				logModifLdap.info("L'utilisateur " + userConnecte.getUid() + " a ré-initialisé le mot de passe de l'élève " + fiche.getUserConsulte().getDn());
			} else {

				this.addNotification(pcc, "label.razNonAutorise", NotificationsType.ERROR);
			}
		} else {

			this.addNotification(pcc, "label.razNonAutorise", NotificationsType.ERROR);
		}

	}

	@ActionMapping(params = "action=surchargeMdp")
	public void surchargeMdp(@ModelAttribute("fiche") Fiche fiche, @ModelAttribute FormSurchargeMdp formSurchargeMdp, BindingResult result,
			ActionRequest request, ActionResponse response, ModelMap modelMap, PortletSession session, final ModelMap model) throws ToutaticeAnnuaireException {

		PortalControllerContext pcc = new PortalControllerContext(this.portletContext, request, response);

		if (fiche == null) {
			response.setRenderParameter("action,", "fichePersonne");

			this.addNotification(pcc, "label.erreurSurcharge", NotificationsType.ERROR);

		} else {

			fiche.setMotifSurcharge(formSurchargeMdp.getMotifSurcharge());
			this.surchargeMdpValidator.validate(formSurchargeMdp, result);

			if (!result.hasErrors()) {

				if (fiche.getLevelUserConnecteSurcharge().equals(HabilitationSurcharge.level.DROIT_SURCHARGE)
						|| fiche.getLevelUserConnecteSurcharge().equals(HabilitationSurcharge.level.DROIT_SURCHARGE_ASSISTANCE)) {

					DirectoryPerson personConnect = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);

					Person userConnecte = this.personne.findUtilisateur(personConnect.getUid());

					if (userConnecte.verifMdp(formSurchargeMdp.getMdpUserConnecte())) {
						String typeSurcharge = "";
						if (userConnecte.hasRole(this.config.getRoleAssistance())) {
							typeSurcharge = "ASSISTANCE";
						}
						if (userConnecte.hasRole(this.config.getRoleAdministrateur())) {
							typeSurcharge = "ADMIN";
						}
						if (userConnecte.hasRole(this.config.getRoleSuperAdministrateur())) {
							typeSurcharge = "SUPERADMIN";
						}

						HttpServletRequest httpRequest = (HttpServletRequest) request.getAttribute("osivia.httpRequest");
						String ipAdress = httpRequest.getRemoteAddr();

						fiche.getUserConsulte().surchargeMdp(formSurchargeMdp.getMdpSurcharge(), userConnecte.getUid(), typeSurcharge,
								formSurchargeMdp.getMotifSurcharge(), ipAdress);
						response.setRenderParameter("action", "fichePersonne");
						model.addAttribute("fiche", fiche);

						this.addNotification(pcc, "label.surchargeOk", NotificationsType.SUCCESS);

						// En mode assistance, envoi d'un mail d'avertissement à l'utilisateur surchargé
						// Possibilité de désactiver cet envoi de mail dans les param d'environnement du portail
						if (fiche.getLevelUserConnecteSurcharge().equals(HabilitationSurcharge.level.DROIT_SURCHARGE_ASSISTANCE) && this.config.getEnableSendMail()
								&& (fiche.getUserConsulte().getEmail() != null) && !fiche.getUserConsulte().getEmail().trim().isEmpty()) {

							String texteMessage = "Bonjour,<br><br>"
									+ "Dans le cadre de l'assistance que vous avez demandée au service informatique du Rectorat de l'académie Rennes, un personnel d'assistance a surchargé votre mot de passe de façon provisoire. Cette opération va lui permettre de régler le problème que vous signalez. Cette surcharge sera supprimée automatiquement la nuit prochaine."
									+ "<br><br>Cordialement"
									+ "<br><br>le Service informatique académique (SERIA)"
									+ "<br><br>Merci de ne pas répondre à ce message."
									+ "<br><br>plate-forme d'assistance de l'académie de Rennes"
									+ "<br><ul>"
									+ "<li>formulaire en ligne : <a href=\"http://assistance.ac-rennes.fr\">http://assistance.ac-rennes.fr</a></li>"
									+ "<li>mél : <a href=\"mailto:assistance@ac-rennes.fr\">assistance@ac-rennes.fr</a></li>"
									+ "<li>appel téléphonique(urgences liées aux accès réseau) : 0810 454 454, (N° Azur, coût d'une communication locale à partir d'un poste fixe)</li>";

							final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
							final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

							try {
								message.setFrom("noreply@toutatice.fr");
								message.setTo(fiche.getUserConsulte().getEmail());
								message.setSubject("information de l'assistance aux usagers de Toutatice");
								message.setText(texteMessage, true);

								this.mailSender.send(mimeMessage);

							} catch (MessagingException e) {
								logger.error("Erreur lors de l'envoi d'un email surcharge à " + fiche.getUserConsulte().getUid());
								System.out.println(e.toString());
							}

						}

					} else {
						model.addAttribute("messageErreur", "label.mdpInvalide");
						this.addNotification(pcc, "label.mdpInvalide", NotificationsType.ERROR);
						response.setRenderParameter("action", "surchargeMdp");
					}
				} else {

					this.addNotification(pcc, "label.surchargeNonAutorise", NotificationsType.ERROR);
					response.setRenderParameter("action", "fichePersonne");
				}
			} else {
				model.addAttribute("fiche", fiche);
				response.setRenderParameter("action", "surchargeMdp");
			}
		}

	}

	private PersonUrl buildUrlPerson(Person p, PortalControllerContext portalControllerContext) throws Exception{

		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.hideTitle", "1");
		windowProperties.put("uidFichePersonne", p.getUid());

		String url = this.getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.fichePersonne.getInstanceName(),
				windowProperties, false);

		return new PersonUrl(p,url);
	}

	private void setListeProfilUrlModel(List<String> listeProfils, PortalControllerContext portalControllerContext) throws PortalException {

		List<ProfilUrl> liste = new ArrayList<ProfilUrl>();

		for (String dnProfil : listeProfils) {
			Profil p;
			p = this.profil.findProfilByDn(dnProfil);
			if (p != null) {

				Map<String, String> windowProperties = new HashMap<String, String>();
				windowProperties.put("osivia.ajaxLink", "1");
				windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
				windowProperties.put("osivia.title", p.getDisplayName());
				windowProperties.put("osivia.hideTitle", "1");
				windowProperties.put("cnProfil", p.getCn());

				String url = this.getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.ficheProfil.getInstanceName(),
						windowProperties, false);
				liste.add(new ProfilUrl(p, url));
			}
		}

		portalControllerContext.getRequest().setAttribute("listeProfilsUrl", liste);

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
