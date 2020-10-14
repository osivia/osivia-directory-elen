package org.osivia.services.user.savedsearches.administration.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationForm;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationWindowSettings;

import javax.portlet.PortletException;

/**
 * User saved searches administration portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface UserSavedSearchesAdministrationService {

    /**
     * All categories indicator window property.
     */
    String ALL_CATEGORIES_WINDOW_PROPERTY = "osivia.user-saved-searches.all-categories";
    /**
     * Category identifier window property.
     */
    String CATEGORY_ID_WINDOW_PROPERTY = "osivia.user-saved-searches.category-id";


    /**
     * Get window settings.
     *
     * @param portalControllerContext portal controller context
     * @return window settings.
     */
    UserSavedSearchesAdministrationWindowSettings getWindowSettings(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set window settings.
     *
     * @param portalControllerContext portal controller context
     * @param windowSettings          window settings
     */
    void setWindowSettings(PortalControllerContext portalControllerContext, UserSavedSearchesAdministrationWindowSettings windowSettings) throws PortletException;


    /**
     * Get user saved searches administration form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    UserSavedSearchesAdministrationForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Render view.
     *
     * @param portalControllerContext portal controller context
     * @return view path
     */
    String renderView(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save user saved searches administration.
     *
     * @param portalControllerContext portal controller context
     * @param form                    user saved searches administration form
     */
    void save(PortalControllerContext portalControllerContext, UserSavedSearchesAdministrationForm form) throws PortletException;


    /**
     * Move saved search.
     *
     * @param portalControllerContext portal controller context
     * @param id                      saved search identifier
     * @param up                      move up direction indicator, move down otherwise
     */
    void move(PortalControllerContext portalControllerContext, int id, boolean up) throws PortletException;


    /**
     * Rename saved search.
     *
     * @param portalControllerContext portal controller context
     * @param id                      saved search identifier
     * @param displayName             saved search display name
     */
    void rename(PortalControllerContext portalControllerContext, int id, String displayName) throws PortletException;


    /**
     * Delete saved search.
     *
     * @param portalControllerContext portal controller context
     * @param id                      saved search identifier
     */
    void delete(PortalControllerContext portalControllerContext, int id) throws PortletException;

}
