package org.osivia.services.firstconnection.portlet.repository;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.stereotype.Repository;

/**
 * First connection portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see FirstConnectionRepository
 */
@Repository
public class FirstConnectionRepositoryImpl implements FirstConnectionRepository {

    /**
     * Constructor.
     */
    public FirstConnectionRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getRedirectionUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        return StringEscapeUtils.unescapeHtml(window.getProperty(REDIRECTION_URL_PROPERTY));
    }

}
