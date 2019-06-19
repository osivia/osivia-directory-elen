package org.osivia.services.person.card.portlet.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.Avatar;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.RoleService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.person.card.portlet.model.PersonCard;
import org.osivia.services.person.card.portlet.model.PersonCardOptions;
import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.osivia.services.person.card.portlet.model.PersonNuxeoProfile;
import org.osivia.services.person.card.portlet.model.PersonPasswordEditionForm;
import org.osivia.services.person.card.portlet.model.PersonPasswordEditionMode;
import org.osivia.services.person.card.portlet.model.PersonTitle;
import org.osivia.services.person.card.portlet.repository.PersonCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Person card portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see PersonCardService
 */
@Service
public class PersonCardServiceImpl implements PersonCardService {

    /** Administrator role system property. */
    private static final String ADMINISTRATOR_ROLE_PROPERTY = "personcard.roles.roleAdministrator";
    /** Password edition allowed indicator system property. */
    private static final String PASSWORD_EDITION_ALLOWED_PROPERTY = "personcard.personCanChangePassword";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private PersonCardRepository repository;

    /** Person service. */
    @Autowired
    private PersonUpdateService personService;

    /** Role service. */
    @Autowired
    private RoleService roleService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /** Administrator role. */
    private final String administratorRole;
    /** Password edition allowed indicator. */
    private final boolean passwordEditionAllowed;


    /**
     * Constructor.
     */
    public PersonCardServiceImpl() {
        super();

        // Administrator role
        this.administratorRole = System.getProperty(ADMINISTRATOR_ROLE_PROPERTY);
        // Password edition allowed indicator
        this.passwordEditionAllowed = BooleanUtils.toBoolean(System.getProperty(PASSWORD_EDITION_ALLOWED_PROPERTY, String.valueOf(true)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PersonCardOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Person card options
        PersonCardOptions options = this.applicationContext.getBean(PersonCardOptions.class);

        if (StringUtils.isEmpty(options.getUid())) {
            // Portlet request
            PortletRequest request = portalControllerContext.getRequest();
            // Window
            PortalWindow window = WindowFactory.getWindow(request);

            // Person UID
            String uid = StringUtils.defaultIfEmpty(window.getProperty(PERSON_UID_WINDOW_PROPERTY), request.getRemoteUser());
            options.setUid(uid);

            // Person DN
            Name dn = this.personService.getEmptyPerson().buildDn(uid);
            options.setDn(dn);

            // Person
            Person person = this.personService.getPersonNoCache(options.getDn());
            options.setPerson(person);

            // Person titles
            List<PersonTitle> titles = Arrays.asList(PersonTitle.values());
            options.setTitles(titles);

            if (person != null) {
                // Editable person card indicator
                boolean editable = this.isEditable(portalControllerContext, person);
                options.setEditable(editable);

                // Password edition mode
                PersonPasswordEditionMode passwordEditionMode = this.getPasswordEditionMode(portalControllerContext, person);
                options.setPasswordEditionMode(passwordEditionMode);

                // Deletable person indicator
                boolean deletable = this.isDeletable(portalControllerContext, person);
                options.setDeletable(deletable);
            }
        }

        return options;
    }


    /**
     * Check if the person card can be edited by the current user.
     *
     * @param portalControllerContext portal controller context
     * @param person targeted person
     * @return true if the person card is editable
     */
    protected boolean isEditable(PortalControllerContext portalControllerContext, Person person) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current user UID
        String uid = request.getRemoteUser();

        // Editable person card indicator
        boolean editable;

        if (StringUtils.isEmpty(uid)) {
            editable = false;
        } else if (StringUtils.equals(uid, person.getUid())) {
            editable = true;
        } else {
            // Current user DN
            Name dn = this.personService.getEmptyPerson().buildDn(uid);

            // Check if the current user has administration role
            editable = StringUtils.isNotEmpty(this.administratorRole) && this.roleService.hasRole(dn, this.administratorRole);
        }

        return editable;
    }


    /**
     * Get password edition mode.
     *
     * @param portalControllerContext portal controller context
     * @param person targeted person
     * @return password edition mode
     */
    protected PersonPasswordEditionMode getPasswordEditionMode(PortalControllerContext portalControllerContext, Person person) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current user UID
        String uid = request.getRemoteUser();

        // Password edition mode
        PersonPasswordEditionMode passwordEditionMode;

        if (StringUtils.isEmpty(uid)) {
            passwordEditionMode = PersonPasswordEditionMode.DENY;
        } else {
            // Current user DN
            Name dn = this.personService.getEmptyPerson().buildDn(uid);

            // Check if the current user has administration role
            if (StringUtils.isNotEmpty(this.administratorRole) && this.roleService.hasRole(dn, this.administratorRole)) {
                passwordEditionMode = PersonPasswordEditionMode.OVERWRITE;
            } else if (StringUtils.equals(uid, person.getUid()) && BooleanUtils.isNotTrue(person.getExternal()) && this.passwordEditionAllowed) {
                passwordEditionMode = PersonPasswordEditionMode.ALLOW;
            } else {
                passwordEditionMode = PersonPasswordEditionMode.DENY;
            }
        }

        return passwordEditionMode;
    }


    /**
     * Check if the person can be deleted by the current user.
     *
     * @param portalControllerContext portal controller context
     * @param person targeted person
     * @return true if the person can be deleted
     */
    protected boolean isDeletable(PortalControllerContext portalControllerContext, Person person) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current user UID
        String uid = request.getRemoteUser();

        // Deletable person indicator
        boolean deletable;

        if (StringUtils.isEmpty(uid) || StringUtils.equals(uid, person.getUid())) {
            deletable = false;
        } else {
            // Current user DN
            Name dn = this.personService.getEmptyPerson().buildDn(uid);

            // Check if the current user has administration role
            deletable = StringUtils.isNotEmpty(this.administratorRole) && this.roleService.hasRole(dn, this.administratorRole);
        }

        return deletable;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PersonCard getPersonCard(PortalControllerContext portalControllerContext) throws PortletException {
        // Person card options
        PersonCardOptions options = this.getOptions(portalControllerContext);

        // Person
        Person person = this.personService.getPersonNoCache(options.getDn());

        // Person card
        PersonCard card;
        if (person == null) {
            card = null;
        } else {
            card = this.applicationContext.getBean(PersonCard.class);
            card.setPerson(person);

            // Person Nuxeo profile
            PersonNuxeoProfile nuxeoProfile = this.repository.getNuxeoProfile(portalControllerContext, person);
            card.setNuxeoProfile(nuxeoProfile);
        }

        return card;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePerson(PortalControllerContext portalControllerContext, PersonCardOptions options) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if (options.isDeletable()) {
            // Person
            Person person = options.getPerson();

            if (person != null) {
                this.personService.delete(person);

                // Update options
                options.setPerson(null);

                // Notification
                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("PERSON_CARD_DELETE_SUCCESS"),
                        NotificationsType.SUCCESS);
            }
        } else {
            // Forbidden
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("PERSON_CARD_DELETE_FORBIDDEN"), NotificationsType.ERROR);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PersonEditionForm getEditionForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Person card options
        PersonCardOptions options = this.getOptions(portalControllerContext);

        // Edition form
        PersonEditionForm form = this.applicationContext.getBean(PersonEditionForm.class);


        // Person
        Person person = this.personService.getPersonNoCache(options.getDn());

        if (person != null) {
            // Avatar
            Avatar avatar = new Avatar();
            avatar.setUrl(person.getAvatar().getUrl());
            form.setAvatar(avatar);

            this.fillLdapProperties(portalControllerContext, form, person);
        }


        // Person Nuxeo profile
        PersonNuxeoProfile nuxeoProfile = this.repository.getNuxeoProfile(portalControllerContext, person);

        if (nuxeoProfile != null) {
            this.fillNuxeoProperties(portalControllerContext, form, nuxeoProfile);
        }

        return form;
    }


    /**
     * Fill LDAP properties.
     *
     * @param form person edition form
     * @param person person
     * @throws PortletException
     */
    protected void fillLdapProperties(PortalControllerContext portalControllerContext, PersonEditionForm form, Person person) throws PortletException {
        // Title
        String title = person.getTitle();
        form.setTitle(title);

        // First name
        String firstName = person.getGivenName();
        form.setFirstName(firstName);

        // Last name
        String lastName = person.getSn();
        form.setLastName(lastName);

        // Mail
        String mail = person.getMail();
        form.setMail(mail);
    }


    /**
     * Fill Nuxeo properties.
     *
     * @param form person edition form
     * @param nuxeoProfile Nuxeo profile
     * @throws PortletException
     */
    protected void fillNuxeoProperties(PortalControllerContext portalControllerContext, PersonEditionForm form, PersonNuxeoProfile nuxeoProfile)
            throws PortletException {
        // Occupation
        String occupation = nuxeoProfile.getOccupation();
        form.setOccupation(occupation);

        // Institution
        String institution = nuxeoProfile.getInstitution();
        form.setInstitution(institution);

        // Phone
        String phone = nuxeoProfile.getPhone();
        form.setPhone(phone);

        // Mobile phone
        String mobilePhone = nuxeoProfile.getMobilePhone();
        form.setMobilePhone(mobilePhone);

        // Bio
        String bio = nuxeoProfile.getBio();
        form.setBio(bio);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadAvatar(PortalControllerContext portalControllerContext, PersonEditionForm form) throws PortletException, IOException {
        // Avatar
        Avatar avatar = form.getAvatar();
        avatar.setUpdated(true);
        avatar.setDeleted(false);

        // Temporary file
        MultipartFile upload = avatar.getUpload();
        File temporaryFile = File.createTempFile("avatar-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        avatar.setTemporaryFile(temporaryFile);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAvatar(PortalControllerContext portalControllerContext, PersonEditionForm form) throws PortletException, IOException {
        // Avatar
        Avatar avatar = form.getAvatar();
        avatar.setUpdated(false);
        avatar.setDeleted(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void savePerson(PortalControllerContext portalControllerContext, PersonCardOptions options, PersonEditionForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if (options.isEditable()) {
            // Person
            Person person = options.getPerson();

            if (person != null) {
                // LDAP properties
                this.setLdapProperties(portalControllerContext, form, person);

                // Nuxeo properties
                Map<String, String> properties = this.repository.getNuxeoProperties(portalControllerContext, form);

                // Update person
                try {
                    this.personService.update(portalControllerContext, person, form.getAvatar(), properties);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }

                // Notification
                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("PERSON_CARD_EDITION_SUCCESS"),
                        NotificationsType.SUCCESS);
            }
        } else {
            // Forbidden
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("PERSON_CARD_EDITION_FORBIDDEN"),
                    NotificationsType.ERROR);
        }
    }


    /**
     * Set LDAP properties.
     *
     * @param form person edition form
     * @param person person
     * @throws PortletException
     */
    protected void setLdapProperties(PortalControllerContext portalControllerContext, PersonEditionForm form, Person person) throws PortletException {
        person.setTitle(StringUtils.trimToNull(form.getTitle()));
        person.setGivenName(StringUtils.trim(form.getFirstName()));
        person.setSn(StringUtils.trim(form.getLastName()));
        person.setCn(StringUtils.trim(form.getLastName() + " " + form.getFirstName()));
        person.setDisplayName(StringUtils.trim(form.getFirstName() + " " + form.getLastName()));
        person.setMail(StringUtils.trim(form.getMail()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PersonPasswordEditionForm getPasswordEditionForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Person card options
        PersonCardOptions options = this.getOptions(portalControllerContext);

        // Password edition form
        PersonPasswordEditionForm form = this.applicationContext.getBean(PersonPasswordEditionForm.class);

        // Person UID
        form.setUid(options.getUid());

        // Overwrite password indicator
        form.setOverwrite(PersonPasswordEditionMode.OVERWRITE.equals(options.getPasswordEditionMode()));

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(PortalControllerContext portalControllerContext, PersonCardOptions options, PersonPasswordEditionForm form)
            throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if ((options.getPasswordEditionMode() != null) && options.getPasswordEditionMode().isEditable()) {
            // Person
            Person person = options.getPerson();

            if (person != null) {
            	
                List<String> messages = personService.validatePasswordRules(portalControllerContext, form.getUpdate());
                if(!messages.isEmpty()) {
                	String messagesConcat = bundle.getString("PASSWORD_VALIDATION");
                	
                	messagesConcat = messagesConcat + StringUtils.join(messages, ", ");
                	// Notification
                    this.notificationsService.addSimpleNotification(portalControllerContext, messagesConcat,
                            NotificationsType.ERROR);
                    
                    throw new PortletException(messagesConcat);
                }
                else {
            	   	this.personService.updatePassword(person, form.getUpdate());
                    // Notification
                    this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("PERSON_CARD_PASSWORD_EDITION_SUCCESS"),
                            NotificationsType.SUCCESS);
                }


            }
        } else {
            // Forbidden
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("PERSON_CARD_PASSWORD_EDITION_FORBIDDEN"),
                    NotificationsType.ERROR);
            
            throw new PortletException(bundle.getString("PERSON_CARD_PASSWORD_EDITION_FORBIDDEN"));

        }
    }

}
