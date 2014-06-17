/*
 * (C) Copyright 2014 OSIVIA (http://www.osivia.com)
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
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
import org.osivia.portal.api.login.IUserDatasModuleRepository;
import org.osivia.portal.api.login.UserDatasModuleMetadatas;
import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;


public class DirectoryPortlet extends CMSPortlet {

    private IDirectoryServiceLocator dirLocator;

    protected IUserDatasModuleRepository repository;
    private UserDatasModuleMetadatas userDatasModule;

    @Override
    public void init(PortletConfig config) throws PortletException {

        super.init(config);

        PortletContext pc = this.getPortletContext();

        // export the directory service for other portlets
        ApplicationContext context = PortletApplicationContextUtils.getWebApplicationContext(pc);
        DirectoryService directoryService = (DirectoryService) context.getBean("directoryService");

        directoryService.setPortletContext(pc);
        directoryService.setPortletConfig(config);

        dirLocator = Locator.findMBean(IDirectoryServiceLocator.class, IDirectoryServiceLocator.MBEAN_NAME);

        IDirectoryService iface = (IDirectoryService) directoryService;
        dirLocator.register(iface);

        // register the provider of current user informations
        try {
            repository = (IUserDatasModuleRepository) directoryService.getPortletContext().getAttribute("UserDatasModulesRepository");

            userDatasModule = new UserDatasModuleMetadatas();

            userDatasModule.setName("LDAPUSER");
            userDatasModule.setOrder(0);
            userDatasModule.setModule(directoryService);

            repository.register(userDatasModule);
        } catch (Exception e) {
            throw new PortletException(e);
        }


        // force reload of other portlets
        try {

            MBeanServer mbeanServer = MBeanServerLocator.locateJBoss();

            MBeanInvoker mbean = (MBeanInvoker) MBeanProxy.get(MBeanInvoker.class, new ObjectName("portal:deployer=Adapter"), mbeanServer);

            mbean.invoke("stop", null, null);
            mbean.invoke("start", null, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // @Override
    public void destroy() {

        super.destroy();

        repository.unregister(userDatasModule);
    }

}
