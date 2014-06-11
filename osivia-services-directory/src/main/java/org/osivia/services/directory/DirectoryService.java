package org.osivia.services.directory;

import java.lang.reflect.InvocationTargetException;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.directory.IDirectoryService;
import org.osivia.portal.api.directory.DirectoryBean;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.locator.Locator;
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
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.Structure;


public class DirectoryService implements IDirectoryService, PortletContextAware, PortletConfigAware {

    /** Logger. */
    protected static final Log logger = LogFactory.getLog(DirectoryService.class);

    private PortletContext portletContext;
    private PortletConfig portletConfig;

    @Autowired
    private Person personne;

    @Autowired
    private Structure structure;

    @Autowired
    private Profil profil;

    ApplicationContext appContext;


    public DirectoryPerson getPerson(String username) {
        Person findUtilisateur = personne.findUtilisateur(username);

        DirectoryPerson person = new DirectoryPerson();
        person.setNativeItem(findUtilisateur);

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
    }

    public <T extends DirectoryBean> T getDirectoryBean(String name, Class<T> requiredType) {

        return appContext.getBean(name, requiredType);

    }

    public void setPortletContext(PortletContext ctx) {
        this.portletContext = ctx;
        this.appContext = PortletApplicationContextUtils.getWebApplicationContext(portletContext);
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }
}
