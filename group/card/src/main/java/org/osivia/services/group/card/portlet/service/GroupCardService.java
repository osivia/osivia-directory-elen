package org.osivia.services.group.card.portlet.service;

import javax.portlet.PortletException;

import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.group.card.portlet.model.GroupCard;
import org.osivia.services.group.card.portlet.model.GroupCardOptions;
import org.osivia.services.group.card.portlet.model.GroupEditionForm;

import net.sf.json.JSONObject;

public interface GroupCardService {

    /** Group cn window property. */
    String GROUP_CN_WINDOW_PROPERTY = "osivia.group.cn";
    
    
    /**
     * Get portlet options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    GroupCardOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Get group edition form.
     * 
     * @param portalControllerContext portal controller context
     * @return edition form
     * @throws PortletException
     */
    GroupEditionForm getEditionForm(PortalControllerContext portalControllerContext) throws PortletException;

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
    
    /**
     * Save group.
     * 
     * @param portalControllerContext portal controller context
     * @param options portlet options
     * @param form group edition form
     * @throws PortletException
     */
    void saveGroup(PortalControllerContext portalControllerContext, GroupCardOptions options, GroupEditionForm form) throws PortletException;

    
    /**
     * Search persons
     * @param portalControllerContext
     * @param options
     * @param form
     * @param filter
     * @return
     * @throws PortletException
     */
    public JSONObject searchPersons(PortalControllerContext portalControllerContext, GroupCardOptions options, GroupEditionForm form, String filter) throws PortletException;
    
    /**
     * Add member
     * @param portalControllerContext
     * @param form
     * @throws PortletException
     */
    public void addMember(PortalControllerContext portalControllerContext, GroupEditionForm form, PortalGroup portalGroup) throws PortletException;
    
}
