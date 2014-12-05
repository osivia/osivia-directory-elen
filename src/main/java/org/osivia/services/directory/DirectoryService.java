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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.directory.DirectoryBean;
import org.osivia.portal.api.directory.IDirectoryService;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.login.IUserDatasModule;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.outils.ldap.entity.Person;


public class DirectoryService implements IDirectoryService, IUserDatasModule, PortletContextAware, PortletConfigAware {

    /** Logger. */
    protected static final Log logger = LogFactory.getLog(DirectoryService.class);

    private PortletContext portletContext;
    private PortletConfig portletConfig;

    @Autowired
    private Person personne;


    ApplicationContext appContext;


    public DirectoryPerson getPerson(String username) {
        Person findUtilisateur = personne.findUtilisateur(username);

        if (findUtilisateur != null) {
            DirectoryPerson person = new DirectoryPerson();

            try {
                BeanUtils.copyProperties(person, findUtilisateur);
            } catch (IllegalAccessException e) {
                logger.error("unable to map properties from ldap ", e);
            } catch (InvocationTargetException e) {
                logger.error("unable to map properties from ldap ", e);
            }

            ICMSServiceLocator cmsLocator = Locator.findMBean(ICMSServiceLocator.class, "osivia:service=CmsServiceLocator");
            ICMSService cmsService = cmsLocator.getCMSService();

            try {
                CMSServiceCtx cmsCtx = new CMSServiceCtx();
                cmsCtx.setPortletCtx(portletContext);
                Link userAvatar = cmsService.getUserAvatar(cmsCtx, username);
                person.setAvatar(userAvatar);


            } catch (CMSException e) {
                logger.error("unable to prepare user avatar link ", e);
            }


            return person;
        } else {
            logger.warn("no person with uid " + username + " found ");
            return null;
        }
    }

    public <T extends DirectoryBean> T getDirectoryBean(String name, Class<T> requiredType) {

        return appContext.getBean(name, requiredType);

    }

    /**
     * @return the portletContext
     */
    public PortletContext getPortletContext() {
        return portletContext;
    }

    public void setPortletContext(PortletContext ctx) {
        this.portletContext = ctx;
        this.appContext = PortletApplicationContextUtils.getWebApplicationContext(portletContext);
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public void computeUserDatas(HttpServletRequest request, Map<String, Object> datas) {
        String uid = request.getUserPrincipal().getName();
        if (uid != null) {
            Person userConnecte = personne.findUtilisateur(uid);
            if (userConnecte != null) {
                userConnecte.populateMap(datas);
            }
        }
    }

    public DirectoryPerson computeLoggedUser(HttpServletRequest request) {
        DirectoryPerson person = null;
        String uid = request.getUserPrincipal().getName();
        if (uid != null) {
            person = getPerson(uid);
        }

        return person;
    }

}
