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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.common.invocation.Scope;
import org.jboss.portal.core.controller.ControllerContext;
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
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.portal.core.constants.InternationalizationConstants;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
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
 * @see LdapServiceImpl
 * @see PersonUpdateService
 */
@Service
public class PersonServiceImpl extends LdapServiceImpl implements PersonUpdateService {

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

    @Override
    public Person getPersonNoCache(Name dn) {

        Person p;
        try {
            p = dao.getPersonNoCache(dn);
            appendAvatar(p);

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

	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#update(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void create(Person p) {
		
		dao.create(p);
		
		ldapLogger.info("Person created : "+p.getUid());

	}

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#update(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void update(Person p) {
		dao.update(p);
		
		ldapLogger.info("Person updated : "+p.getUid());

	}
	
	@Transactional
	public void update(PortalControllerContext portalControllerContext, Person p, Avatar avatar, Map<String, String> properties) throws PortalException {
		
		update(p);
		
		NuxeoController controller = new NuxeoController(portalControllerContext);
		
		Document nuxeoProfile = (Document) getEcmProfile(portalControllerContext, p);
		
		UpdateUserProfileCommand updateCmd = new UpdateUserProfileCommand(nuxeoProfile,properties, avatar);
		controller.executeNuxeoCommand(updateCmd);
		
		if(avatar != null) {
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

	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.PersonUpdateService#validatePasswordRules(org.osivia.portal.api.context.PortalControllerContext)
	 */
	@Override
	public List<String> validatePasswordRules(PortalControllerContext portalControllerContext, String newPassword) {

		List<String> messages = new ArrayList<>();

		PasswordValidator pwv = new PasswordValidator(Arrays.asList(new LengthRule(8, 30),
				new UppercaseCharacterRule(1), new DigitCharacterRule(1), new SpecialCharacterRule(1),
				new LowercaseCharacterRule(1), new WhitespaceRule()));			

		RuleResult result = pwv.validate(new PasswordData(newPassword));
		
		Bundle bundle = bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
				
		for(RuleResultDetail detail : result.getDetails()) {
			String translatedMsg = bundle.getString(detail.getErrorCode());
			messages.add(translatedMsg);
		}
		
		return messages;
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

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.PersonUpdateService#delete(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void delete(Person userConsulte) {
		
		CollabProfile searchProfiles = workspaceService.getEmptyProfile();
		searchProfiles.setType(WorkspaceGroupType.space_group);
		List<Name> name = new ArrayList<Name>();
		name.add(userConsulte.getDn());
		searchProfiles.setUniqueMember(name);
		
		List<CollabProfile> spaces = workspaceService.findByCriteria(searchProfiles);
		for(CollabProfile space : spaces) {
			workspaceService.removeMember(space.getWorkspaceId(), userConsulte.getDn());
		}
		
		dao.delete(userConsulte);
		
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
        ControllerContext controllerContext;
        PortletRequest request;
        if (portalControllerContext == null) {
            controllerContext = null;
            request = null;
        } else {
            controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);
            request = portalControllerContext.getRequest();
        }
        // Portal administrator indicator attribute value
        Boolean attribute;
        if (controllerContext == null) {
            attribute = null;
        } else {
            // Search attribute value in principal scope
            attribute = (Boolean) controllerContext.getAttribute(Scope.PRINCIPAL_SCOPE, "osivia.isAdmin");
        }
        if ((attribute == null) && (request != null)) {
            // Search attribute value in portlet request
            attribute = (Boolean) request.getAttribute(InternalConstants.ADMINISTRATOR_INDICATOR_ATTRIBUTE_NAME);
        }

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
