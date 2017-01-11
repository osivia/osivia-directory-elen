package org.osivia.services.firstconnection.portlet.service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.firstconnection.portlet.model.UserForm;
import org.osivia.services.firstconnection.portlet.repository.FirstConnectionRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

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
        
        // User form
        UserForm form = this.applicationContext.getBean(UserForm.class);
        form.setId(user);
        form.setEmail(user);

        return form;
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

        // Update
        this.personService.update(person);

        // Update password
        String password = form.getPassword();
        this.personService.updatePassword(person, password);
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
