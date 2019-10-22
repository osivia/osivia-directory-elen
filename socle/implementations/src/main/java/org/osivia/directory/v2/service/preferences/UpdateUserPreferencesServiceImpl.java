package org.osivia.directory.v2.service.preferences;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.repository.preferences.UpdateUserPreferencesCommand;
import org.osivia.directory.v2.service.DirServiceImpl;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.preferences.UpdateUserPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletContext;
import javax.servlet.http.HttpSession;

/**
 * Update user preferences service implementation.
 *
 * @author Cédric Krommenhoek
 * @see DirServiceImpl
 * @see UpdateUserPreferencesService
 */
@Service
public class UpdateUserPreferencesServiceImpl extends DirServiceImpl implements UpdateUserPreferencesService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * User preferences service.
     */
    @Autowired
    private UserPreferencesServiceImpl userPreferencesService;


    /**
     * Constructor.
     */
    public UpdateUserPreferencesServiceImpl() {
        super();
    }


    @Override
    public void update(HttpSession httpSession) {
        // User preferences implementation
        UserPreferences preferences = this.userPreferencesService.getUserPreferences(httpSession);

        if ((preferences != null) && preferences.areUpdated()) {
            // Portlet context
            PortletContext portletContext = this.getPortletContext();

            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portletContext);
            nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
            nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(UpdateUserPreferencesCommand.class, preferences);
            nuxeoController.executeNuxeoCommand(command);
        }
    }

}
