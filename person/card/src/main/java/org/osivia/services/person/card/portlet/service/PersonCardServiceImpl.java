/**
 * 
 */
package org.osivia.services.person.card.portlet.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.ext.Avatar;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.RoleService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.login.IUserDatasModuleRepository;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.person.card.portlet.controller.Card;
import org.osivia.services.person.card.portlet.controller.FormChgPwd;
import org.osivia.services.person.card.portlet.controller.FormEdition;
import org.osivia.services.person.card.portlet.controller.NuxeoProfile;
import org.osivia.services.person.card.portlet.controller.PersonCardConfig;
import org.osivia.services.person.card.portlet.controller.PersonCardWorkspaceMember;
import org.osivia.services.person.card.portlet.repository.GetUserWorkspacesCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * @author Loïc Billon
 *
 */
@Service
public class PersonCardServiceImpl implements PersonCardService {

	/**
	 * 
	 */
	private static final String PROFESSION = "ttc_userprofile:profession";

	/**
	 * 
	 */
	private static final String MOBILE = "ttc_userprofile:mobile";

	/**
	 * 
	 */
	private static final String PHONE_NUMBER = "userprofile:phonenumber";

	/**
	 * 
	 */
	private static final String BIO = "ttc_userprofile:bio";

	/**
	 * 
	 */
	private static final String INSTITUTION = "ttc_userprofile:institution";
	
	/**
	 * 
	 */
	private static final String SHOWN_IN_SEARCH = "ttc_userprofile:shownInSearch";
	
	
	@Autowired
	private PersonCardConfig config;

	@Autowired
	private PersonUpdateService personService;
	
	@Autowired
	private WorkspaceService workspaceService;

	@Autowired
	private IPortalUrlFactory urlFactory;
	
	@Autowired
	private RoleService roleService;
	
    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * {@inheritDoc}
     */
    @Override
    public LevelEdition findLevelEdition(Person userConnecte, Person userConsulte) {
        // Level
        LevelEdition level;

        if (userConnecte == null) {
            level = LevelEdition.DENY;
        } else if (config.getRoleAdministrator() != null && roleService.hasRole(userConnecte.getDn(), config.getRoleAdministrator())) {
            level = LevelEdition.ALLOW;
        } else if (userConnecte.getUid().equals(userConsulte.getUid())) {
            level = LevelEdition.ALLOW;
        } else {
            level = LevelEdition.DENY;
        }

        return level;
    }
	

    /**
     * {@inheritDoc}
     */
    @Override
    public LevelChgPwd findLevelChgPwd(Person userConnecte, Person userConsulte) {
        // Level
    	LevelChgPwd level;

        if (userConnecte == null) {
            level = LevelChgPwd.DENY;
        } else if (config.getRoleAdministrator() != null && roleService.hasRole(userConnecte.getDn(), config.getRoleAdministrator())) {
            level = LevelChgPwd.OVERWRITE;
        } else if (userConnecte.getUid().equals(userConsulte.getUid()) && BooleanUtils.isNotTrue(userConnecte.getExternal())
                && config.getPersonCanChangePassword()) {
            level = LevelChgPwd.ALLOW;
        } else {
            level = LevelChgPwd.DENY;
        }

        return level;
    }
	
    
    /**
     * {@inheritDoc}
     */
	@Override
	public LevelDeletion findLevelDeletion(Person userConnecte, Person userConsulte) {
        // Level
        LevelDeletion level;

        if (userConnecte == null) {
            level = LevelDeletion.DENY;
        } else if ((config.getRoleAdministrator() != null) && (roleService.hasRole(userConnecte.getDn(), config.getRoleAdministrator()))
                && !userConnecte.getUid().equals(userConsulte.getUid())) {
            level = LevelDeletion.ALLOW;
        } else {
            level = LevelDeletion.DENY;
        }

        return level;
	}	
	

	public Card loadCard(PortalControllerContext context) throws PortalException {
		
		//TODO replace with ATTR_LOGGED_PERSON_2
		PortletRequest request = context.getRequest();
		Person userConnecte = (Person) request.getAttribute("osivia.directory.v2.loggedPerson");

		//	TODO gérer les déconnectés

		Card card = new Card();

		PortalWindow window = WindowFactory.getWindow(request);
		String uid = window.getProperty("uidFichePersonne");

		Person userConsulte = null;
		// Consultation d'une autre personne, soit en anonyme, soit connecté et dans ce cas l'UID est différent de soi
		if ((userConnecte == null) || ((uid != null) && !(uid.equals(userConnecte.getUid())))) {

			userConsulte = personService.getPerson(uid);

		} else {
			userConsulte = userConnecte;
		}
		
        if ((userConnecte != null) && userConnecte.getUid().equals(userConsulte.getUid())) {
            card.setSelf(true);
        }

		card.setUserConsulte(userConsulte);
		card.setLevelEdition(findLevelEdition(userConnecte, userConsulte));
		card.setLevelDeletion(findLevelDeletion(userConnecte, userConsulte));
		card.setLevelChgPwd(findLevelChgPwd(userConnecte, userConsulte));
		card.setAvatar(userConsulte.getAvatar());
		
		
		Document nuxeoProfile = (Document) personService.getEcmProfile(context, userConsulte);
		card.setNxProfile(convertNxProfile(nuxeoProfile));
		
		// User workspaces
		if(card.getLevelEdition().equals(LevelEdition.ALLOW)) {
			setNxWorkspaces(context, card, userConsulte.getUid());
			
		}
		
		return card;
	}


	/**
	 * Evaluate the map memberOfSpace
	 *  
	 * @param context
	 * @param card the form
	 * @param uid the current uid
	 */
	private void setNxWorkspaces(PortalControllerContext context, Card card, String uid) {
		NuxeoController controller = new NuxeoController(context);
		
		Documents documents = (Documents)  controller.executeNuxeoCommand(new GetUserWorkspacesCommand(uid));
		
		for(Document workspace : documents) {
			
			WorkspaceMember member = workspaceService.getMember(workspace.getString("webc:url"), uid);
			
			PersonCardWorkspaceMember personCardWorkspaceMember = new PersonCardWorkspaceMember(member);
			
            // Title
			personCardWorkspaceMember.setTitle(workspace.getTitle());
            // Description
			personCardWorkspaceMember.setDescription(workspace.getString("dc:description"));

            // Vignette URL
            PropertyMap vignette = workspace.getProperties().getMap("ttc:vignette");
            String vignetteUrl;
            if (vignette == null) {
                vignetteUrl = null;
            } else {
                vignetteUrl = controller.createFileLink(workspace, "ttc:vignette");
            }
            personCardWorkspaceMember.setVignetteUrl(vignetteUrl);
            
            String cmsUrl = urlFactory.getCMSUrl(context, null, workspace.getPath(), null, null, null, null, null, null, null);
            
            personCardWorkspaceMember.setLink(cmsUrl);
            
			card.getMemberOfSpace().add(personCardWorkspaceMember);
			
		}
	}
	
	/**
	 * Convert from nuxeo document to object
	 * @param docNxProfile
	 * @return 
	 */
	public NuxeoProfile convertNxProfile(Document docNxProfile) {
		
		NuxeoProfile profile = new NuxeoProfile();
		
		profile.setBio(docNxProfile.getString(BIO));
		profile.setPhone(docNxProfile.getString(PHONE_NUMBER));
		profile.setMobilePhone(docNxProfile.getString(MOBILE));
		profile.setOccupation(docNxProfile.getString(PROFESSION));
		profile.setInstitution(docNxProfile.getString(INSTITUTION));
		profile.setShownInSearch(Boolean.valueOf(docNxProfile.getString(SHOWN_IN_SEARCH)));
		
		return profile;
	}

	/* (non-Javadoc)
	 * @see org.osivia.services.person.card.portlet.service.PersonCardService#uploadAvatar(org.osivia.portal.api.context.PortalControllerContext, org.osivia.services.person.card.portlet.controller.FormEdition)
	 */
	@Override
	public void uploadAvatar(PortalControllerContext portalControllerContext,
			FormEdition form) throws IllegalStateException, IOException {
		
        // Vignette
        Avatar avatar = form.getAvatar();
        avatar.setUpdated(true);
        avatar.setDeleted(false);

        // Temporary file
        MultipartFile upload = form.getAvatar().getUpload();
        File temporaryFile = File.createTempFile("avatar-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        avatar.setTemporaryFile(temporaryFile);
		
	}

	/* (non-Javadoc)
	 * @see org.osivia.services.person.card.portlet.service.PersonCardService#deleteAvatar(org.osivia.portal.api.context.PortalControllerContext, org.osivia.services.person.card.portlet.controller.FormEdition)
	 */
	@Override
	public void deleteAvatar(PortalControllerContext portalControllerContext,
			FormEdition form) {
        // Vignette
        Avatar avatar = form.getAvatar();
        avatar.setUpdated(false);
        avatar.setDeleted(true);
		
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.services.person.card.portlet.service.PersonCardService#saveCard(org.osivia.portal.api.context.PortalControllerContext, org.osivia.services.person.card.portlet.controller.FormEdition)
	 */
	@Override
	public void saveCard(PortalControllerContext portalControllerContext,
			Card card, FormEdition form) throws PortalException {
		
		// update the person in ldap
		Person p = personService.getPerson(card.getUserConsulte().getUid());
		mergeLdapProperties(p, form);
		
		Map<String, String> nxProperties = new HashMap<String, String>();
		mergeNxProperties(nxProperties, form);
		personService.update(portalControllerContext, p, form.getAvatar(), nxProperties);
		
		
		// ========= maj user connecte
		if (card.isSelf()) {
			
			IUserDatasModuleRepository userRepo = Locator.findMBean(IUserDatasModuleRepository.class, IUserDatasModuleRepository.MBEAN_NAME);
			userRepo.reload(portalControllerContext.getRequest());
			
		}
	}


	/**
	 * @param p
	 * @param form
	 */
	protected void mergeLdapProperties(Person p, FormEdition form) {
		
		p.setMail(StringUtils.trimToNull(form.getMail()));
		
		p.setTitle(StringUtils.trimToNull(form.getTitle()));
		
		if (form.getSn() != null) {
			p.setSn(form.getSn());
		}
		if (form.getGivenName() != null) {
			p.setGivenName(form.getGivenName());
		}
		if((form.getSn() != null) && (form.getGivenName() != null)) {
			String fullName = form.getGivenName() + " " + form.getSn();
			String reversefullName = form.getSn() + " " + form.getGivenName();
			p.setCn(reversefullName);
			p.setDisplayName(fullName);
		}
	}
	

	/**
	 * @param nxProperties
	 * @param form
	 */
	protected void mergeNxProperties(Map<String, String> nxProperties,
			FormEdition form) {
		
		nxProperties.put(BIO, form.getBio());
		nxProperties.put(PHONE_NUMBER, form.getPhone());
		nxProperties.put(MOBILE, form.getMobilePhone());
		nxProperties.put(PROFESSION, form.getOccupation());
		nxProperties.put(INSTITUTION,  form.getInstitution());
		nxProperties.put(SHOWN_IN_SEARCH, form.getShownInSearch().toString());
		
	}

	/* (non-Javadoc)
	 * @see org.osivia.services.person.card.portlet.service.PersonCardService#changePassword(org.osivia.services.person.card.portlet.controller.FormChgPwd)
	 */
	@Override
	public boolean changePassword(Card card, FormChgPwd formChgPwd) {

		if (!personService.verifyPassword(card.getUserConsulte().getUid(), formChgPwd.getCurrentPwd())) {
			return false;
		} else {

			// Modification du mot de passe
			personService.updatePassword(card.getUserConsulte(), formChgPwd.getNewPwd());
			
			return true;

		}
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.services.person.card.portlet.service.PersonCardService#overwritePassword(org.osivia.services.person.card.portlet.controller.Card, org.osivia.services.person.card.portlet.controller.FormChgPwd)
	 */
	@Override
	public void overwritePassword(Card card, FormChgPwd formChgPwd) {
		// Modification du mot de passe
		personService.updatePassword(card.getUserConsulte(), formChgPwd.getNewPwd());
		
	}

	/* (non-Javadoc)
	 * @see org.osivia.services.person.card.portlet.service.PersonCardService#deletePerson(org.osivia.services.person.card.portlet.controller.Card)
	 */
	@Override
	public void deletePerson(Card card) {
		
		personService.delete(card.getUserConsulte());
		
	}



}
