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
import java.util.HashMap;
import java.util.Map;

import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManager;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.ldap.transaction.compensating.support.DefaultTempEntryRenamingStrategy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;


/**
 * Config Implémentation of service
 * @author Loïc Billon
 * @since 4.4
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "org.osivia.directory.v2")
public class AppConfig {
	
	@Autowired
	private ApplicationContext context;

	@Bean(name="contextSourceTransactionAwareProxy")
	public TransactionAwareContextSourceProxy txProxy() {
		
		LdapContextSource source = new LdapContextSource();
		source.setUrl(System.getProperty("ldap.url"));
		
		// Do not set ldap.base, spring-ldap won't generate full DN in the object-to-directory mapping
		//source.setBase(System.getProperty("ldap.base"));
		
		source.setUserDn(System.getProperty("ldap.manager.dn"));
		source.setPassword(System.getProperty("ldap.manager.pswd"));
		source.setPooled(true);
		
		Map<String, Object> baseEnvironmentProperties = new HashMap<String, Object>();
		baseEnvironmentProperties.put("com.sun.jndi.ldap.connect.timeout", System.getProperty("ldap.timeout"));
		source.setBaseEnvironmentProperties(baseEnvironmentProperties);
		source.afterPropertiesSet();		
		
		return new TransactionAwareContextSourceProxy(source);
	}	
	
	@Bean(name="ldapTemplate")
	public LdapTemplate getLdapTemplate() {
		
		TransactionAwareContextSourceProxy contextSource = context.getBean(TransactionAwareContextSourceProxy.class);
		return new LdapTemplate(contextSource);
	}
	
	
	@Bean
	public ContextSourceTransactionManager getTxManager() {
		ContextSourceTransactionManager txManager = new ContextSourceTransactionManager();
		txManager.setContextSource((ContextSource) context.getBean("contextSourceTransactionAwareProxy"));
		txManager.setRenamingStrategy(new DefaultTempEntryRenamingStrategy());		
		
		return txManager;
	}	
	
	/**
	 * For composite TM
	 * @return
	 */
	@Bean(name="ldapTransactionManagerDelegate")
	public ContextSourceTransactionManagerDelegate getTxManagerDelegate() {
		ContextSourceTransactionManagerDelegate txManagerDelegate = new ContextSourceTransactionManagerDelegate();
		txManagerDelegate.setContextSource((ContextSource) context.getBean("contextSourceTransactionAwareProxy"));
		
		return txManagerDelegate;
	}	
	

    /**
     * Get portal URL factory.
     * 
     * @return portal URL factory
     */
	@Bean(name="urlFactory")
	public IPortalUrlFactory getUrlFactory() {
		return Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
	}


    /**
     * Get Nuxeo service.
     * 
     * @return Nuxeo service
     */
    @Bean
    public INuxeoService getNuxeoService() {
        return Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
    }

    /**
     * Get bundle factory.
     *
     * @return bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

}
