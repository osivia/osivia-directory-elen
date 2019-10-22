package org.osivia.services.user.savedsearches.administration.portlet.service;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationForm;
import org.osivia.services.user.savedsearches.administration.portlet.model.comparator.UserSavedSearchOrderComparator;
import org.osivia.services.user.savedsearches.administration.portlet.repository.UserSavedSearchesAdministrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User saved searches administration portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see UserSavedSearchesAdministrationService
 */
@Service
public class UserSavedSearchesAdministrationServiceImpl implements UserSavedSearchesAdministrationService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private UserSavedSearchesAdministrationRepository repository;

    /**
     * Saved search order comparator.
     */
    @Autowired
    private UserSavedSearchOrderComparator savedSearchOrderComparator;

    /**
     * User preferences service.
     */
    @Autowired
    private UserPreferencesService userPreferencesService;



    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationServiceImpl() {
        super();
    }


    @Override
    public UserSavedSearchesAdministrationForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.applicationContext.getBean(UserSavedSearchesAdministrationForm.class);

        // Saved searches
        List<UserSavedSearch> savedSearches = this.repository.getUserSavedSearches(portalControllerContext);
        if (CollectionUtils.isNotEmpty(savedSearches)) {
            Collections.sort(savedSearches, this.savedSearchOrderComparator);
        }
        form.setSavedSearches(savedSearches);

        return form;
    }


    @Override
    public String renderView(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);

        // Empty response
        if (CollectionUtils.isEmpty(form.getSavedSearches())) {
            request.setAttribute("osivia.emptyResponse", "1");
        }

        return "view";
    }

    @Override
    public void save(PortalControllerContext portalControllerContext, UserSavedSearchesAdministrationForm form) throws PortletException {
        this.repository.saveUserSavedSearches(portalControllerContext, form.getSavedSearches());

        // Update model
        List<UserSavedSearch> savedSearches = form.getSavedSearches();
        if (CollectionUtils.isNotEmpty(savedSearches)) {
            Collections.sort(savedSearches, this.savedSearchOrderComparator);
        }
    }


    @Override
    public void move(PortalControllerContext portalControllerContext, int id, boolean up) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);
        // Saved searches
        List<UserSavedSearch> savedSearches = form.getSavedSearches();

        // Saved search index
        int index;
        try {
            index = savedSearches.indexOf(this.userPreferencesService.createUserSavedSearch(portalControllerContext, id));
        } catch (PortalException e) {
            index = -1;
        }

        // Swap
        if (up && (index > 0)) {
            Collections.swap(savedSearches, index - 1, index);
        } else if (!up && (index > -1) && (index < (savedSearches.size() - 1))) {
            Collections.swap(savedSearches, index, index + 1);
        }

        // Reset order
        for (int i = 0; i < savedSearches.size(); i++) {
            UserSavedSearch savedSearch = savedSearches.get(i);
            savedSearch.setOrder(i);
        }

        this.save(portalControllerContext, form);
    }


    @Override
    public void rename(PortalControllerContext portalControllerContext, int id, String displayName) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);
        // Saved searches
        List<UserSavedSearch> savedSearches = form.getSavedSearches();

        if (CollectionUtils.isNotEmpty(savedSearches)) {
            boolean found = false;
            Iterator<UserSavedSearch> iterator = savedSearches.iterator();
            while (!found && iterator.hasNext()) {
                UserSavedSearch savedSearch = iterator.next();
                if (id == savedSearch.getId()) {
                    savedSearch.setDisplayName(displayName);
                    found = true;
                }
            }
        }

        this.save(portalControllerContext, form);
    }


    @Override
    public void delete(PortalControllerContext portalControllerContext, int id) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);
        // Saved searches
        List<UserSavedSearch> savedSearches = form.getSavedSearches();

        if (CollectionUtils.isNotEmpty(savedSearches)) {
            boolean found = false;
            Iterator<UserSavedSearch> iterator = savedSearches.iterator();
            while (!found && iterator.hasNext()) {
                UserSavedSearch savedSearch = iterator.next();
                if (id == savedSearch.getId()) {
                    iterator.remove();
                    found = true;
                }
            }
        }

        this.save(portalControllerContext, form);
    }

}
