package org.osivia.services.group.card.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.group.card.portlet.model.GroupCard;
import org.osivia.services.group.card.portlet.model.GroupCardOptions;

public interface GroupCardService {

    /** Group UID window property. */
    String GROUP_UID_WINDOW_PROPERTY = "uidFicheGroup";
    
    
    /**
     * Get portlet options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    GroupCardOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get group card.
     * 
     * @param portalControllerContext portal controller context
     * @return group card
     * @throws PortletException
     */
    GroupCard getGroupCard(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Delete group.
     * 
     * @param portalControllerContext portal controller context
     * @param options portlet options
     * @throws PortletException
     */
    void deleteGroup(PortalControllerContext portalControllerContext, GroupCardOptions options) throws PortletException;
    
}
