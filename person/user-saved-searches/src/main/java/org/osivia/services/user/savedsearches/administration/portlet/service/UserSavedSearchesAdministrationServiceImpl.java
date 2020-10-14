package org.osivia.services.user.savedsearches.administration.portlet.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationForm;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationWindowSettings;
import org.osivia.services.user.savedsearches.administration.portlet.model.comparator.UserSavedSearchOrderComparator;
import org.osivia.services.user.savedsearches.administration.portlet.repository.UserSavedSearchesAdministrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.*;

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
    public UserSavedSearchesAdministrationWindowSettings getWindowSettings(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Window settings
        UserSavedSearchesAdministrationWindowSettings windowSettings = this.applicationContext.getBean(UserSavedSearchesAdministrationWindowSettings.class);

        // All categories indicator
        boolean allCategories = BooleanUtils.toBoolean(window.getProperty(ALL_CATEGORIES_WINDOW_PROPERTY));
        windowSettings.setAllCategories(allCategories);

        // Category identifier
        String categoryId = StringUtils.trimToEmpty(window.getProperty(CATEGORY_ID_WINDOW_PROPERTY));
        windowSettings.setCategoryId(categoryId);

        return windowSettings;
    }


    @Override
    public void setWindowSettings(PortalControllerContext portalControllerContext, UserSavedSearchesAdministrationWindowSettings windowSettings) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // All categories indicator
        window.setProperty(ALL_CATEGORIES_WINDOW_PROPERTY, String.valueOf(windowSettings.isAllCategories()));

        // Category identifier
        window.setProperty(CATEGORY_ID_WINDOW_PROPERTY, StringUtils.trimToEmpty(windowSettings.getCategoryId()));
    }


    @Override
    public UserSavedSearchesAdministrationForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window settings
        UserSavedSearchesAdministrationWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        // Form
        UserSavedSearchesAdministrationForm form = this.applicationContext.getBean(UserSavedSearchesAdministrationForm.class);

        Map<String, List<UserSavedSearch>> categorizedSavedSearches;
        if (windowSettings.isAllCategories()) {
            categorizedSavedSearches = this.repository.getUserCategorizedSavedSearches(portalControllerContext);
        } else {
            // Category identifier
            String categoryId = StringUtils.trimToEmpty(windowSettings.getCategoryId());

            List<UserSavedSearch> savedSearches = this.repository.getUserSavedSearches(portalControllerContext, categoryId);

            if (CollectionUtils.isEmpty(savedSearches)) {
                categorizedSavedSearches = null;
            } else {
                categorizedSavedSearches = new HashMap<>(1);
                categorizedSavedSearches.put(categoryId, savedSearches);
            }
        }
        form.setCategorizedSavedSearches(categorizedSavedSearches);

        return form;
    }


    @Override
    public String renderView(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window settings
        UserSavedSearchesAdministrationWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);

        // Empty response
        boolean empty;
        if (windowSettings.isAllCategories()) {
            empty = MapUtils.isEmpty(form.getCategorizedSavedSearches());
        } else {
            // Category identifier
            String categoryId = StringUtils.trimToEmpty(windowSettings.getCategoryId());

            empty = MapUtils.isEmpty(form.getCategorizedSavedSearches()) || CollectionUtils.isEmpty(form.getCategorizedSavedSearches().get(categoryId));
        }
        if (empty) {
            request.setAttribute("osivia.emptyResponse", String.valueOf(1));
        }

        return "view";
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, UserSavedSearchesAdministrationForm form) throws PortletException {
        // Window settings
        UserSavedSearchesAdministrationWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        if (windowSettings.isAllCategories()) {
            if (MapUtils.isNotEmpty(form.getCategorizedSavedSearches())) {


                for (Map.Entry<String, List<UserSavedSearch>> entry : form.getCategorizedSavedSearches().entrySet()) {
                    // Category identifier
                    String categoryId = StringUtils.trimToEmpty(entry.getKey());
                    // Category searches
                    List<UserSavedSearch> categorySearches = entry.getValue();

                    this.repository.saveUserSavedSearches(portalControllerContext, categoryId, categorySearches);
                }
            }
        } else {
            // Category identifier
            String categoryId = StringUtils.trimToEmpty(windowSettings.getCategoryId());
            // Category searches
            List<UserSavedSearch> categorySearches;
            if (MapUtils.isEmpty(form.getCategorizedSavedSearches()) || CollectionUtils.isEmpty(form.getCategorizedSavedSearches().get(categoryId))) {
                categorySearches = null;
            } else {
                categorySearches = form.getCategorizedSavedSearches().get(categoryId);
            }

            this.repository.saveUserSavedSearches(portalControllerContext, categoryId, categorySearches);
        }

        // Update model
        if (MapUtils.isNotEmpty(form.getCategorizedSavedSearches())) {
            for (List<UserSavedSearch> searches : form.getCategorizedSavedSearches().values()) {
                if (CollectionUtils.isNotEmpty(searches)) {
                    searches.sort(this.savedSearchOrderComparator);
                }
            }
        }
    }


    @Override
    public void move(PortalControllerContext portalControllerContext, int id, boolean up) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);

        // Saved searches
        List<UserSavedSearch> searches = this.findUserSavedSearches(portalControllerContext, form, id);

        if (CollectionUtils.isNotEmpty(searches)) {
            // Saved search index
            int index;
            try {
                index = searches.indexOf(this.userPreferencesService.createUserSavedSearch(portalControllerContext, id));
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            // Swap
            if (up && (index > 0)) {
                Collections.swap(searches, index - 1, index);
            } else if (!up && (index > -1) && (index < (searches.size() - 1))) {
                Collections.swap(searches, index, index + 1);
            }

            // Reset order
            for (int i = 0; i < searches.size(); i++) {
                UserSavedSearch savedSearch = searches.get(i);
                savedSearch.setOrder(i);
            }

            this.save(portalControllerContext, form);
        }
    }


    /**
     * Find user saved searches.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param id                      user saved search identifier
     * @return user saved searches, or null if not found
     */
    private List<UserSavedSearch> findUserSavedSearches(PortalControllerContext portalControllerContext, UserSavedSearchesAdministrationForm form, int id) throws PortletException {
        List<UserSavedSearch> searches;

        // Window settings
        UserSavedSearchesAdministrationWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        if (MapUtils.isEmpty(form.getCategorizedSavedSearches())) {
            searches = null;
        } else {
            if (windowSettings.isAllCategories()) {
                // Criteria
                UserSavedSearch criteria;
                try {
                    criteria = this.userPreferencesService.createUserSavedSearch(portalControllerContext, id);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }

                searches = null;
                Iterator<List<UserSavedSearch>> iterator = form.getCategorizedSavedSearches().values().iterator();
                while ((searches == null) && iterator.hasNext()) {
                    List<UserSavedSearch> userSavedSearches = iterator.next();
                    if (CollectionUtils.isNotEmpty(userSavedSearches) && userSavedSearches.contains(criteria)) {
                        searches = userSavedSearches;
                    }
                }
            } else {
                // Category identifier
                String categoryId = StringUtils.trimToEmpty(windowSettings.getCategoryId());

                searches = form.getCategorizedSavedSearches().get(categoryId);
            }
        }

        return searches;
    }


    @Override
    public void rename(PortalControllerContext portalControllerContext, int id, String displayName) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);
        // Saved searches
        List<UserSavedSearch> savedSearches = this.findUserSavedSearches(portalControllerContext, form, id);

        if (CollectionUtils.isNotEmpty(savedSearches)) {
            int index;
            try {
                index = savedSearches.indexOf(this.userPreferencesService.createUserSavedSearch(portalControllerContext, id));
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            if (index >= 0) {
                UserSavedSearch savedSearch = savedSearches.get(index);
                savedSearch.setDisplayName(displayName);

                this.save(portalControllerContext, form);
            }
        }
    }


    @Override
    public void delete(PortalControllerContext portalControllerContext, int id) throws PortletException {
        // Form
        UserSavedSearchesAdministrationForm form = this.getForm(portalControllerContext);
        // Saved searches
        List<UserSavedSearch> savedSearches = this.findUserSavedSearches(portalControllerContext, form, id);

        if (CollectionUtils.isNotEmpty(savedSearches)) {
            try {
                savedSearches.remove(this.userPreferencesService.createUserSavedSearch(portalControllerContext, id));
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            this.save(portalControllerContext, form);
        }
    }

}
