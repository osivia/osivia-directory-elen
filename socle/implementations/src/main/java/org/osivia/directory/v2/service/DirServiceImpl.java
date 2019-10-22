package org.osivia.directory.v2.service;

import javax.portlet.PortletContext;

import org.osivia.portal.api.directory.v2.IDirService;

/**
 * Directory service implementation abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see IDirService
 */
public abstract class DirServiceImpl implements IDirService {

    /** Portlet context. */
    private static PortletContext portletContext;


    /**
     * Constructor.
     */
    public DirServiceImpl() {
        super();
    }


    /**
     * Getter for portletContext.
     * 
     * @return the portletContext
     */
    public PortletContext getPortletContext() {
        return portletContext;
    }


    /**
     * Setter for portletContext.
     * 
     * @param portletContext the portletContext to set
     */
    public static void setPortletContext(PortletContext portletContext) {
        DirServiceImpl.portletContext = portletContext;
    }

}
