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
package org.osivia.directory.v2.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Name;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.dao.PersonDao;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.Avatar;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.repository.GetUserProfileCommand;
import org.osivia.directory.v2.repository.UpdateUserProfileCommand;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.portalobject.bridge.PortalObjectUtils;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.portal.core.constants.InternationalizationConstants;
import org.passay.AbstractCharacterRule;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;

/**
 * Person service implementation.
 *
 * @author Loïc Billon
 * @since 4.4
 * @see DirServiceImpl
 * @see PersonUpdateService
 */
@Service
public class PersonServiceImpl extends DirServiceImpl implements PersonUpdateService {

	private final static Log ldapLogger = LogFactory.getLog("org.osivia.directory.v2");

	/** Person card portlet instance. */
    private static final String CARD_INSTANCE = "directory-person-card-instance";


    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;

    /** Person sample. */
    @Autowired
    protected Person personSample;

    /** Person DAO. */
    @Autowired
    protected PersonDao dao;

    /** Workspace service. */
    @Autowired
    protected WorkspaceService workspaceService;

    /** Portal URL factory. */
    @Autowired
    protected IPortalUrlFactory urlFactory;

    /** Internationalization service. */
    @Autowired
    protected IInternationalizationService internationalizationService;

    /** Internationalization bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;


    /**
     * {@inheritDoc}
     */
    @Override
    public Person getEmptyPerson() {
        return this.applicationContext.getBean(Person.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPerson(Name dn) {
        Person p;
        try {
            p = this.dao.getPerson(dn);
            this.appendAvatar(p);

        } catch (NameNotFoundException e) {
            ldapLogger.warn("Person with dn "+dn+" not found");
            return null;
        }

        return p;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPersonNoCache(Name dn) {
        Person p;
        try {
            p = this.dao.getPersonNoCache(dn);
            this.appendAvatar(p);
        } catch (NameNotFoundException e) {
            ldapLogger.warn("Person with dn "+dn+" not found");
            return null;
        }

        return p;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPerson(String uid) {
        Name dn = this.getEmptyPerson().buildDn(uid);

        return this.getPerson(dn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> findByCriteria(Person search) {
        List<Person> persons = this.dao.findByCriteria(search);
        for (Person p : persons) {
            this.appendAvatar(p);
        }
        return persons;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Person p) {
        this.dao.create(p);
		ldapLogger.info("Person created : "+p.getUid());
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Person p) {
        this.dao.update(p);
        
		ldapLogger.info("Person updated : "+p.getUid());
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void update(PortalControllerContext portalControllerContext, Person p, Avatar avatar, Map<String, String> properties) throws PortalException {
        this.update(p);

        NuxeoController controller = new NuxeoController(portalControllerContext);

        Document nuxeoProfile = (Document) this.getEcmProfile(portalControllerContext, p);

        UpdateUserProfileCommand updateCmd = new UpdateUserProfileCommand(nuxeoProfile, properties, avatar);
        controller.executeNuxeoCommand(updateCmd);

        if (avatar != null) {
            controller.refreshUserAvatar(p.getUid());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyPassword(String uid, String currentPassword) {
        return this.dao.verifyPassword(uid, currentPassword);
    }

	

    @Override
    public Map<String, String> validatePasswordRules(String password) {
        // Rules
        List<Rule> rules = this.getPasswordRules();
        // Message resolver
        MessageResolver messageResolver = this.getPasswordRulesMessageResolver();

        // Password validator
        PasswordValidator passwordValidator = new PasswordValidator(messageResolver, rules);

        // Password data
        PasswordData passwordData = new PasswordData(password);

        // Validation result
        RuleResult result = passwordValidator.validate(passwordData);

        Map<String, String> messages = new LinkedHashMap<String, String>(result.getDetails().size());
        for (RuleResultDetail detail : result.getDetails()) {
            String errorCode = detail.getErrorCode();
            String message = messageResolver.resolve(detail);
            messages.put(errorCode, message);
        }

        return messages;
    }


    @Override
    public Map<String, Boolean> getPasswordRulesInformation(String password) {
        // Rules
        List<Rule> rules = this.getPasswordRules();
        // Message resolver
        MessageResolver messageResolver = this.getPasswordRulesMessageResolver();

        // Password data
        PasswordData passwordData = new PasswordData(password);

        // Informations
        Map<String, Boolean> informations = new LinkedHashMap<>(rules.size());
        for (Rule rule : rules) {
            if (rule instanceof LengthRule) {
                // Length rule
                LengthRule lengthRule = (LengthRule) rule;
                int minimumLength = lengthRule.getMinimumLength();
                int maximumLength = lengthRule.getMaximumLength();

                // Detail parameters
                Map<String, Object> detailParameters = new LinkedHashMap<>(2);
                detailParameters.put("minimumLength", minimumLength);
                detailParameters.put("maximumLength", maximumLength);

                if (minimumLength > 0) {
                    // Minimum length rule
                    Rule minimumLengthRule = new LengthRule(minimumLength, Integer.MAX_VALUE);
                    RuleResultDetail detail = new RuleResultDetail(lengthRule.ERROR_CODE_MIN, detailParameters);
                    String message = messageResolver.resolve(detail);
                    RuleResult result = minimumLengthRule.validate(passwordData);

                    informations.put(message, result.isValid());
                }

                if (maximumLength < Integer.MAX_VALUE) {
                    // Maximum length rule
                    Rule maximumLengthRule = new LengthRule(0, maximumLength);
                    RuleResultDetail detail = new RuleResultDetail(lengthRule.ERROR_CODE_MAX, detailParameters);
                    String message = messageResolver.resolve(detail);
                    RuleResult result = maximumLengthRule.validate(passwordData);

                    informations.put(message, result.isValid());
                }
            } else if (rule instanceof AbstractCharacterRule) {
                // Character rule
                AbstractCharacterRule characterRule = (AbstractCharacterRule) rule;

                // Detail parameters
                Map<String, Object> detailParameters = new LinkedHashMap<>(1);
                detailParameters.put("minimumRequired", characterRule.getNumberOfCharacters());

                // Code
                String code;
                if (characterRule instanceof LowercaseCharacterRule) {
                    code = LowercaseCharacterRule.ERROR_CODE;
                } else if (characterRule instanceof UppercaseCharacterRule) {
                    code = UppercaseCharacterRule.ERROR_CODE;
                } else if (characterRule instanceof DigitCharacterRule) {
                    code = DigitCharacterRule.ERROR_CODE;
                } else if (characterRule instanceof SpecialCharacterRule) {
                    code = SpecialCharacterRule.ERROR_CODE;
                } else {
                    code = null;
                }

                if (StringUtils.isNotEmpty(code)) {
                    RuleResultDetail detail = new RuleResultDetail(code, detailParameters);
                    String message = messageResolver.resolve(detail);
                    RuleResult result = characterRule.validate(passwordData);

                    informations.put(message, result.isValid());
                }
            } else if (rule instanceof WhitespaceRule) {
                // Whitespace rule
                WhitespaceRule whitespaceRule = (WhitespaceRule) rule;

                RuleResultDetail detail = new RuleResultDetail(WhitespaceRule.ERROR_CODE, null);
                String message = messageResolver.resolve(detail);
                RuleResult result = whitespaceRule.validate(passwordData);

                informations.put(message, result.isValid());
            }
        }

        return informations;
    }


    /**
     * Get password rules.
     *
     * @return rules
     */
    private List<Rule> getPasswordRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new LengthRule(8, 30));
        rules.add(new LowercaseCharacterRule(1));
        rules.add(new UppercaseCharacterRule(1));
        rules.add(new DigitCharacterRule(1));
        rules.add(new EnhancedSpecialCharacter(1));
        rules.add(new WhitespaceRule());
        return rules;
    }


    /**
     * Get password rules message resolver.
     *
     * @return message resolver
     */
    private MessageResolver getPasswordRulesMessageResolver() {
        MessageResolver messageResolver;
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getResourceAsStream("/password-validation_fr.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            messageResolver = new PropertiesMessageResolver(properties);
        } catch (IOException e) {
            this.ldapLogger.error("Error loading message properties.", e);
            IOUtils.closeQuietly(inputStream);
            messageResolver = new PropertiesMessageResolver();
        }
        return messageResolver;
    }


    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(Person p, String newPassword) {
        this.dao.updatePassword(p, newPassword);
    }


    /**
     * Get avatar url for a person
     *
     * @param person the person
     */
    @SuppressWarnings("deprecation")
    protected void appendAvatar(Person person) {
        // Append avatar
        INuxeoService nuxeoService = Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
        INuxeoCustomizer cmsCustomizer = nuxeoService.getCMSCustomizer();

        Link userAvatar = new Link("", false);
        try {
            userAvatar = cmsCustomizer.getUserAvatar(null, person.getUid());
        } catch (CMSException e) {

        }
        person.setAvatar(userAvatar);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Link getCardUrl(PortalControllerContext portalControllerContext, Person person) throws PortalException {
        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put("osivia.hideTitle", "1");
        windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, "true");
        windowProperties.put(InternalConstants.PROP_WINDOW_TITLE, person.getDisplayName());
        windowProperties.put("uidFichePersonne", person.getUid());

        Map<String, String> parameters = new HashMap<String, String>();

        String url = this.urlFactory.getStartPortletInNewPage(portalControllerContext, "profile-" + person.getUid(), person.getDisplayName(),
                this.getCardInstance(), windowProperties, parameters);
        return new Link(url, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Link getMyCardUrl(PortalControllerContext portalControllerContext) throws PortalException {
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getHttpServletRequest().getLocale());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put(InternalConstants.PROP_WINDOW_TITLE, bundle.getString(InternationalizationConstants.KEY_MY_PROFILE));
        properties.put("osivia.hideTitle", "1");
        properties.put("osivia.ajaxLink", "1");
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));

        Map<String, String> parameters = new HashMap<String, String>();

        String url = this.urlFactory.getStartPortletInNewPage(portalControllerContext, "myprofile",
                bundle.getString(InternationalizationConstants.KEY_MY_PROFILE), this.getCardInstance(), properties, parameters);

        return new Link(url, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getEcmProfile(PortalControllerContext portalControllerContext, Person person) throws PortalException {

        // HTTP servlet request
        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.getPortletContext());
        nuxeoController.setServletRequest(servletRequest);

        return nuxeoController.executeNuxeoCommand(new GetUserProfileCommand(person.getUid()));

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Person userConsulte) {

        CollabProfile searchProfiles = this.workspaceService.getEmptyProfile();
        searchProfiles.setType(WorkspaceGroupType.space_group);
        List<Name> name = new ArrayList<Name>();
        name.add(userConsulte.getDn());
        searchProfiles.setUniqueMember(name);

        List<CollabProfile> spaces = this.workspaceService.findByCriteria(searchProfiles);
        for (CollabProfile space : spaces) {
            this.workspaceService.removeMember(space.getWorkspaceId(), userConsulte.getDn());
        }

        this.dao.delete(userConsulte);

		ldapLogger.info("Person deleted : "+userConsulte.getUid());
        
        
    }


    /**
     * Get person card portlet instance.
     *
     * @return portlet instance
     */
    protected String getCardInstance() {
        return CARD_INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPortalAdministrator(PortalControllerContext portalControllerContext) throws PortalException {
        // Controller context & portlet request
        PortletRequest request;
        if (portalControllerContext == null) {
            request = null;
        } else {
            request = portalControllerContext.getRequest();
        }

        // Portal administrator indicator attribute value
        Boolean attribute = PortalObjectUtils.isAdmin(portalControllerContext);

        return BooleanUtils.toBoolean(attribute);
    }


	@Override
	public List<Person> findByNoConnectionDate(Person p) {
		return dao.findByNoConnectionDate(p);
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.PersonUpdateService#findByValidityDate(java.util.Date)
	 */
	@Override
	public List<Person> findByValidityDate(Date d) {
		return dao.findByValidityDate(d);
	}
}
