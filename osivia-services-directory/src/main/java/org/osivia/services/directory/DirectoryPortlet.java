package org.osivia.services.directory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.jboss.mx.server.MBeanInvoker;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.mx.util.MBeanServerLocator;
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


        try {

            MBeanServer mbeanServer = MBeanServerLocator.locateJBoss();

            MBeanInvoker mbean = (MBeanInvoker) MBeanProxy.get(MBeanInvoker.class, new ObjectName("portal:deployer=Adapter"), mbeanServer);

            mbean.invoke("stop", null, null);
            mbean.invoke("start", null, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
