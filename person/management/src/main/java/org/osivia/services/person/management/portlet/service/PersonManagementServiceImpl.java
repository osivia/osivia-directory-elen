/**
 * 
 */
package org.osivia.services.person.management.portlet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.theme.ThemeConstants;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.windows.StartingWindowBean;
import org.osivia.services.person.management.portlet.model.PersonManagementForm;
import org.osivia.services.person.management.portlet.model.User;
import org.osivia.services.person.management.portlet.repository.PersonManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Person management service implementation.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see PersonManagementService
 */
@Service
public class PersonManagementServiceImpl implements PersonManagementService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private PersonManagementRepository repository;

    /** View resolver. */
    @Autowired
    private InternalResourceViewResolver viewResolver;


    /** Log. */
    private final Log log;


    /**
     * Constructor.
     */
    public PersonManagementServiceImpl() {
        super();
        this.log = LogFactory.getLog(this.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PersonManagementForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(PersonManagementForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers(PortalControllerContext portalControllerContext, String filter) throws PortletException {
        // Stripped filter
        String strippedFilter = StringUtils.strip(filter, "*");

        // Persons
        List<Person> persons;
        if (StringUtils.isBlank(strippedFilter)) {
            persons = new ArrayList<>(0);
        } else {
            persons = this.repository.searchPersons(portalControllerContext, strippedFilter);
        }

        // Users
        List<User> users = new ArrayList<>(persons.size());
        for (Person person : persons) {
            User user = this.applicationContext.getBean(User.class);
            user.setId(person.getUid());
            user.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid()));
            user.setExtra(person.getMail());
            if (person.getAvatar() != null) {
                user.setAvatarUrl(person.getAvatar().getUrl());
            }

            users.add(user);
        }

        return users;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void select(PortalControllerContext portalControllerContext, PersonManagementForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Selected user identifier
        String id = form.getSelectedUserId();
        // Region
        String region = System.getProperty(REGION_PROPERTY);

        if (region == null) {
            this.log.error("Unable to start person card portlet: property '" + REGION_PROPERTY + "' is not defined.");
        } else if (StringUtils.isNotEmpty(id)) {
            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put("osivia.hideTitle", "1");
            properties.put("osivia.bootstrapPanelStyle", String.valueOf(true));
            properties.put(ThemeConstants.PORTAL_PROP_REGION, region);
            properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
            properties.put("osivia.ajaxLink", "1");
            properties.put("uidFichePersonne", id);

            StartingWindowBean window = new StartingWindowBean("USER", "directory-person-card-instance", properties);
            request.setAttribute(Constants.PORTLET_ATTR_START_WINDOW, window);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException {
        // View
        View view;
        try {
            view = this.viewResolver.resolveViewName(name, null);
        } catch (Exception e) {
            throw new PortletException(e);
        }

        // Path
        String path;
        if ((view != null) && (view instanceof JstlView)) {
            JstlView jstlView = (JstlView) view;
            path = jstlView.getUrl();
        } else {
            path = null;
        }

        return path;
    }

}
