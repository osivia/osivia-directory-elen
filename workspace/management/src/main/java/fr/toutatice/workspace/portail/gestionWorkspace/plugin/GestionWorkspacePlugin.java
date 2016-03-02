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
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarDropdown;
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
 * Gestion workspace plugin.
 * 
 * @author Loïc Billon
 * @see AbstractPluginPortlet
 * @see IMenubarModule
 */
public class GestionWorkspacePlugin extends AbstractPluginPortlet implements IMenubarModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "gestionworkspace.plugin";


    /** Log. */
    private final Log log;

    /** Menubar service. */
    private final IMenubarService menubarService;


    /**
     * Constructor.
     */
    public GestionWorkspacePlugin() {
        super();
        this.log = LogFactory.getLog(this.getClass());

        // Menubar service
        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String id, CustomizationContext context) {
        List<IMenubarModule> menubars = this.getMenubars(context);
        menubars.add(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void adaptContentMenuBar(CMSServiceCtx cmsContext, List<MenubarItem> menubar, CMSPublicationInfos pubInfos,
            CMSExtendedDocumentInfos extendedDocumentInfos) {
        if (cmsContext.getDoc() != null) {
            // Current document
            Document document = (Document) cmsContext.getDoc();

            if ("Workspace".equals(document.getType())) {
                try {
                    if (!this.isUserWorkspace(cmsContext, document) && pubInfos.isManageableByUser()
                            && ContextualizationHelper.isCurrentDocContextualized(cmsContext)) {

                        // --------- EDIT LDAP MEMBERS
                        Map<String, String> windowProperties = new HashMap<String, String>();
                        windowProperties.put("osivia.ajaxLink", "1");
                        windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
                        windowProperties.put("action", "consulterRole");

                        PortalControllerContext portalControllerContext = new PortalControllerContext(this.getPortletContext(), cmsContext.getRequest(),
                                cmsContext.getResponse());

                        windowProperties.put("osivia.title", this.getMessage(portalControllerContext, "MANAGE_MEMBERS_ACTION"));
                        // windowProperties.put("osivia.hideTitle", "1");
                        windowProperties.put("workspacePath", document.getPath());

                        String urlEditMembers = this.getPortalUrlFactory().getStartPortletUrl(portalControllerContext,
                                DirectoryPortlets.gestionWorkspaces.getInstanceName(),
                                windowProperties, false);

                        MenubarDropdown parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);

                        MenubarItem manageMembersItem = new MenubarItem("MANAGE_MEMBERS", this.getMessage(portalControllerContext, "MANAGE_MEMBERS_ACTION"),
                                "glyphicons glyphicons-group", parent, 100, urlEditMembers, null, null, null);
                        manageMembersItem.setAjaxDisabled(true);
                        manageMembersItem.setDivider(true);

                        menubar.add(manageMembersItem);
                    }
                } catch (PortalException ex) {
                    log.warn(ex.getMessage());
                } catch (CMSException ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
    }


    /**
     * Check if current document is a user workspace.
     * 
     * @param cmsContext CMS context
     * @param document current document
     * @return true if current document is a user workspace
     * @throws CMSException
     */
    private boolean isUserWorkspace(CMSServiceCtx cmsContext, Document document) throws CMSException {
        // CMS service
        ICMSService cmsService = NuxeoController.getCMSService();

        // Path
        String path = document.getPath() + "/";

        // Check if current item is not in user workspaces
        boolean userWorkspace = false;

        List<CMSItem> userWorkspaces = cmsService.getWorkspaces(cmsContext, true, false);
        for (CMSItem cmsItem : userWorkspaces) {
            if (StringUtils.startsWith(path, cmsItem.getPath() + "/")) {
                userWorkspace = true;
                break;
            }
        }

        return userWorkspace;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return CUSTOMIZER_NAME;
    }
}
