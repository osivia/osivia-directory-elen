package org.osivia.services.directory.various.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.various.portlet.model.DuplicatedGroups;

/**
 * Duplicated groups detector portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface DuplicatedGroupsDetectorService {

    /**
     * Get duplicated groups.
     * 
     * @param portalControllerContext portal controller context
     * @return duplicated groups
     * @throws PortletException
     */
    DuplicatedGroups getDuplicatedGroups(PortalControllerContext portalControllerContext) throws PortletException;

}
