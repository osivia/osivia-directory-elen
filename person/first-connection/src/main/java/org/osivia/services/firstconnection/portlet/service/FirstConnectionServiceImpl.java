package org.osivia.services.firstconnection.portlet.service;

import java.util.Date;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.firstconnection.portlet.model.UserForm;
import org.osivia.services.firstconnection.portlet.repository.FirstConnectionRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * First connection portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see FirstConnectionService
 */
@Service
public class FirstConnectionServiceImpl implements FirstConnectionService, ApplicationContextAware {

    /** Portlet repository. */
    @Autowired
    private FirstConnectionRepository repository;

    /** Person service. */
    @Autowired
    private PersonUpdateService personService;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public FirstConnectionServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserForm getUserForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // User
        String user = request.getRemoteUser();
        
        Person person = personService.getPerson(user);
        
        // User form
        UserForm form = this.applicationContext.getBean(UserForm.class);
        form.setId(user);
        form.setEmail(person.getMail());
        form.setFirstName(person.getSn());
        form.setLastName(person.getGivenName());
        
        // If a person is internal (managed by ldap), the password should be changed. 
        if(person.getExternal() == Boolean.FALSE) {
        	form.setMustChangePassword(true);
        }

        return form;
    }


    @Override
    public void validatePasswordRules(Errors errors, String field, String password) {
        Map<String, String> messages = this.personService.validatePasswordRules(password);

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                errors.rejectValue(field, entry.getKey(), entry.getValue());
            }
        }
    }


    @Override
    public Element getPasswordRulesInformation(PortalControllerContext portalControllerContext, String password) {
        // Information
        Map<String, Boolean> information = this.personService.getPasswordRulesInformation(password);

        // Container
        Element container = DOM4JUtils.generateDivElement(StringUtils.EMPTY);

        if (MapUtils.isNotEmpty(information)) {
            Element ul = DOM4JUtils.generateElement("ul", "list-unstyled", StringUtils.EMPTY);
            container.add(ul);

            for (Map.Entry<String, Boolean> entry : information.entrySet()) {
                Element li = DOM4JUtils.generateElement("li", null, StringUtils.EMPTY);
                ul.add(li);

                String htmlClass;
                String icon;
                if (BooleanUtils.isTrue(entry.getValue())) {
                    htmlClass = "text-success";
                    icon = "glyphicons glyphicons-check";
                } else {
                    htmlClass = null;
                    icon = "glyphicons glyphicons-unchecked";
                }
                Element item = DOM4JUtils.generateElement("span", htmlClass, entry.getKey(), icon, null);
                li.add(item);
            }
        }

        return container;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, UserForm form) throws PortletException {
        // Person
        Person person = this.personService.getPerson(form.getId());

        // Title
        String title = form.getTitle();
        person.setTitle(StringUtils.trimToNull(title));
        
        // First name
        String firstName = form.getFirstName();
        person.setGivenName(firstName);

        // Last name
        String lastName = form.getLastName();
        person.setSn(lastName);

        // Display name
        String displayName = firstName + " " + lastName;
        person.setCn(displayName);
        person.setDisplayName(displayName);
        
        // E-mail
        person.setMail(form.getEmail());
        
        // First set of connexion date
        person.setCreationDate(new Date());
        person.setLastConnection(new Date());

        // Update
        this.personService.update(person);

        // Update password
        if(form.isMustChangePassword()) {
        	String password = form.getPassword();
        	this.personService.updatePassword(person, password);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getRedirectionUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Redirection URL
        String redirectionUrl = this.repository.getRedirectionUrl(portalControllerContext);

        if (redirectionUrl != null) {
            try {
                redirectionUrl = portalUrlFactory.getDestroyCurrentPageUrl(portalControllerContext, redirectionUrl);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        return redirectionUrl;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
