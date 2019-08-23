package org.osivia.services.user.savedsearches.administration.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationForm;

import javax.portlet.PortletException;

/**
 * User saved searches administration portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface UserSavedSearchesAdministrationService {

    /**
     * Get user saved searches administration form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    UserSavedSearchesAdministrationForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


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


    // FIXME
    void tmpAddData(PortalControllerContext portalControllerContext);

}
