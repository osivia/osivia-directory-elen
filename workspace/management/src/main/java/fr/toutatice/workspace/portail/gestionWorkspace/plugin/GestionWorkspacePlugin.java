/**
 *
 */
package fr.toutatice.workspace.portail.gestionWorkspace.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSExtendedDocumentInfos;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.services.directory.helper.DirectoryPortlets;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.IMenubarModule;

/**
 * @author loic
 *
 */
public class GestionWorkspacePlugin extends AbstractPluginPortlet implements IMenubarModule  {



    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(GestionWorkspacePlugin.class);

    private static final String CUSTOMIZER_NAME = "gestionworkspace.plugin";



	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#customizeCMSProperties(java.lang.String, org.osivia.portal.api.customization.CustomizationContext)
	 */
	@Override
	protected void customizeCMSProperties(String arg0, CustomizationContext ctx) {

		List<IMenubarModule> menubars = this.getMenubars(ctx);

		menubars.add(this);

	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.IMenubarModule#adaptContentMenuBar(org.osivia.portal.core.cms.CMSServiceCtx, java.util.List)
	 */
	@Override
    public void adaptContentMenuBar(CMSServiceCtx ctx, List<MenubarItem> menuBar, CMSPublicationInfos pubInfos, CMSExtendedDocumentInfos extendedDocumentInfos) {

		if(ctx.getDoc() != null) {

	        // Current document
	        Document document = (Document) ctx.getDoc();

	        if(document.getType().equals("Workspace")) {


	            try {
			        if(!this.isUserWorkspace(ctx, document) && pubInfos.isManageableByUser() && ContextualizationHelper.isCurrentDocContextualized(ctx)) {

		            	// --------- EDIT LDAP MEMBERS

		        		Map<String, String> windowProperties = new HashMap<String, String>();
		        		windowProperties.put("osivia.ajaxLink", "1");
		        		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		        		windowProperties.put("action", "consulterRole");


		        		PortalControllerContext pcc = new PortalControllerContext(this.getPortletContext(), ctx.getRequest(), ctx.getResponse());

						windowProperties.put("osivia.title", this.getMessage(pcc ,"MANAGE_MEMBERS_ACTION"));
		        		//windowProperties.put("osivia.hideTitle", "1");
		        		windowProperties.put("workspacePath", document.getPath());

		        		String urlEditMembers = this.getPortalUrlFactory().getStartPortletUrl(pcc, DirectoryPortlets.gestionWorkspaces.getInstanceName(), windowProperties, false);

		        		MenubarDropdown parent = this.getOtherOptionsDropdown(pcc, this.getBundleFactory().getBundle(ctx.getRequest().getLocale()));

		                MenubarItem manageMembersItem = new MenubarItem("MANAGE_MEMBERS", this.getMessage(pcc, "MANAGE_MEMBERS_ACTION"), "glyphicons glyphicons-group",
		                        parent, 22, urlEditMembers, null, null, null);
			            manageMembersItem.setAjaxDisabled(true);

			            menuBar.add(manageMembersItem);

				     }
			    }
		        catch(PortalException ex) {
		        	LOGGER.warn(ex.getMessage());
		        } catch (CMSException ex) {
		        	LOGGER.warn(ex.getMessage());
				}
	        }

		}


	}

    /**
	 * Is document a user workspace ?
	 * @param document
     * @param ctx
     * @return true if is it
     * @throws CMSException
	 */
	private boolean isUserWorkspace(CMSServiceCtx ctx, Document document) throws CMSException {

        ICMSService cmsService = NuxeoController.getCMSService();

        String path = document.getPath() + "/";

        // Check if current item is not in user workspaces
        boolean userWorkspace = false;

		List<CMSItem> userWorkspaces = cmsService.getWorkspaces(ctx, true, false);
        for (CMSItem cmsItem : userWorkspaces) {
            if (StringUtils.startsWith(path, cmsItem.getPath() + "/")) {
                userWorkspace = true;
                break;
            }
        }

        return userWorkspace;
	}

	/**
     * Get menubar other options dropdown menu.
     *
     * @param portalControllerContext portal controller context
     * @param bundle internationalization bundle
     * @return menubar dropdown menu
     */
    private MenubarDropdown getOtherOptionsDropdown(PortalControllerContext portalControllerContext, Bundle bundle) {
        MenubarDropdown dropdown = this.getMenubarService().getDropdown(portalControllerContext, MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID);

        if (dropdown == null) {
            dropdown = new MenubarDropdown(MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID, bundle.getString("OTHER_OPTIONS"), "glyphicons glyphicons-option-vertical",
                    MenubarGroup.GENERIC, 40);
            dropdown.setReducible(false);
            this.getMenubarService().addDropdown(portalControllerContext, dropdown);
        }

        return dropdown;
    }

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getPluginName()
	 */
	@Override
	protected String getPluginName() {
		return CUSTOMIZER_NAME;
	}
}
