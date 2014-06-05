package org.osivia.services.directory;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.osivia.portal.api.directory.IDirectoryService;
import org.osivia.portal.api.directory.IDirectoryServiceLocator;
import org.osivia.portal.api.locator.Locator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;


public class DirectoryPortlet extends CMSPortlet {


    @Override
    public void init(PortletConfig config) throws PortletException {

        super.init(config);

        PortletContext pc = this.getPortletContext();
        ApplicationContext context = PortletApplicationContextUtils.getWebApplicationContext(pc);
        DirectoryService directoryService = (DirectoryService) context.getBean("directoryService");

        directoryService.setPortletContext(pc);
        directoryService.setPortletConfig(config);

        IDirectoryServiceLocator dirLocator = Locator.findMBean(IDirectoryServiceLocator.class, IDirectoryServiceLocator.MBEAN_NAME);

        IDirectoryService iface = (IDirectoryService) directoryService;
        dirLocator.register(iface);


    }

}
