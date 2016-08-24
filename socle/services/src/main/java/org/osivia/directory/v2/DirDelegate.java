/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
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
package org.osivia.directory.v2;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.directory.v2.IDirDelegate;
import org.osivia.portal.api.directory.v2.IDirService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.stereotype.Service;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Delegate of the LDAP service, used to find services beans in current Spring context
 * and managed transversal function like cache management
 * @author Loïc Billon
 * @since 4.4
 *
 */
@Service
public class DirDelegate implements IDirDelegate, PortletContextAware {

    /** Logger. */
    protected static final Log LOGGER = LogFactory.getLog(DirDelegate.class);

    private PortletContext portletContext;

    private ApplicationContext appContext;
    
    /** ClassLoader kept in memory for calls from other sar, war, ... */
    private final ClassLoader serviceClassLoader = Thread.currentThread().getContextClassLoader();

    
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.IDirDelegate#getDirectoryService(java.lang.Class)
	 */
	@Override
	public <D extends IDirService> D getDirectoryService(Class<D> requiredType) throws InvocationTargetException {
		
		try {
			
			return appContext.getBean(requiredType);
			
		}
		catch(NoSuchBeanDefinitionException ex) {
			throw new InvocationTargetException(ex, "No service registered with type "+requiredType);
			
		}
		
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


    @Override
	public void clearCaches() {
		EhCacheCacheManager cacheManager = appContext.getBean(EhCacheCacheManager.class);
		
		if (cacheManager != null) {

			Collection<String> cacheNames = cacheManager.getCacheNames();

			for (String bean : cacheNames) {
				LOGGER.warn("clear cache : " + bean);
				cacheManager.getCache(bean).clear();
			}
		}
		
	}


	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.IDirDelegate#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {

		return serviceClassLoader;
	}


	@Override
	public ContextSourceTransactionManagerDelegate getDirectoryTxManagerDelegate() {
		
		return appContext.getBean(ContextSourceTransactionManagerDelegate.class);
	}

}
