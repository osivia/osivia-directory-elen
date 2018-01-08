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

import org.osivia.directory.v2.model.converter.BooleanToString;
import org.osivia.directory.v2.model.converter.DateToGeneralizedTime;
import org.osivia.directory.v2.model.converter.GeneralizedTimeToDate;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.odm.core.ObjectDirectoryMapper;
import org.springframework.ldap.odm.core.impl.DefaultObjectDirectoryMapper;
import org.springframework.ldap.odm.typeconversion.ConverterManager;
import org.springframework.ldap.odm.typeconversion.impl.ConversionServiceConverterManager;
import org.springframework.ldap.odm.typeconversion.impl.ConversionServiceConverterManager.StringToNameConverter;
import org.springframework.ldap.pool.factory.PoolingContextSource;
import org.springframework.ldap.pool.validation.DefaultDirContextValidator;
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
	
    /** Application context. */
	@Autowired
	private ApplicationContext applicationContext;

    /** Boolean to string converter. */
    @Autowired
    private BooleanToString booleanToStringConverter;

    /** Generalized time to date converter. */
    @Autowired
    private GeneralizedTimeToDate generalizedTimeToDateConverter;

    /** Date to generalized time converter. */
    @Autowired
    private DateToGeneralizedTime dateToGeneralizedTimeConverter;


    /**
     * Get transaction proxy.
     * 
     * @return transaction proxy
     */
	@Bean(name="contextSourceTransactionAwareProxy")
    public TransactionAwareContextSourceProxy getTransactionProxy() {
		LdapContextSource source = new LdapContextSource();
		source.setUrl(System.getProperty("ldap.url"));
		
		// Do not set ldap.base, spring-ldap won't generate full DN in the object-to-directory mapping
		//source.setBase(System.getProperty("ldap.base"));
		
		source.setUserDn(System.getProperty("ldap.manager.dn"));
		source.setPassword(System.getProperty("ldap.manager.pswd"));
		source.setPooled(false);
		Map<String, Object> baseEnvironmentProperties = new HashMap<String, Object>();
		baseEnvironmentProperties.put("com.sun.jndi.ldap.connect.timeout", System.getProperty("ldap.timeout"));
		source.setBaseEnvironmentProperties(baseEnvironmentProperties);
		source.afterPropertiesSet();	
		
		PoolingContextSource configurePooling = configurePooling(source);
		
		return new TransactionAwareContextSourceProxy(configurePooling);
	}


	/**
     * Pooling configuration.
     * 
     * @param source LDAP context source
     */
	private PoolingContextSource configurePooling(LdapContextSource source) {
		// Enable pooling
		PoolingContextSource poolingContextSource = new PoolingContextSource();
		DefaultDirContextValidator defaultDirContextValidator = new DefaultDirContextValidator();
		
		poolingContextSource.setContextSource(source);
		poolingContextSource.setDirContextValidator(defaultDirContextValidator);
		if(System.getProperty("ldap.pool.testOnBorrow") != null) {
			boolean testOnBorrow = Boolean.parseBoolean(System.getProperty("ldap.pool.testOnBorrow"));
			poolingContextSource.setTestOnBorrow(testOnBorrow);
		}
		if(System.getProperty("ldap.pool.testWhileIdle") != null) {
			boolean testWhileIdle = Boolean.parseBoolean(System.getProperty("ldap.pool.testWhileIdle"));
			poolingContextSource.setTestWhileIdle(testWhileIdle);
		}
		if(System.getProperty("ldap.pool.minEvictableIdleTimeMillis") != null) {
			long minEvictableIdleTimeMillis = Long.parseLong(System.getProperty("ldap.pool.minEvictableIdleTimeMillis"));
			poolingContextSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis );
		}
		if(System.getProperty("ldap.pool.timeBetweenEvictionRunsMillis") != null) {
			long timeBetweenEvictionRunsMillis = Long.parseLong(System.getProperty("ldap.pool.timeBetweenEvictionRunsMillis"));
			poolingContextSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis );
		}
		
		return poolingContextSource;
	}	
	

    /**
     * Get conversion service.
     * 
     * @return conversion service
     */
    @Bean
    public GenericConversionService getConversionService() {
        GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToNameConverter());
        conversionService.addConverter(this.booleanToStringConverter);
        conversionService.addConverter(this.generalizedTimeToDateConverter);
        conversionService.addConverter(this.dateToGeneralizedTimeConverter);
        return conversionService;
    }


    /**
     * Get converter manager.
     * 
     * @param conversionService conversion service
     * @return converter manager
     */
    @Bean
    public ConverterManager getConverterManager(GenericConversionService conversionService) {
        return new ConversionServiceConverterManager(conversionService);
    }


    /**
     * Get object directory mapper.
     * 
     * @param converterManager converter manager
     * @return object directory mapper
     */
    @Bean
    public ObjectDirectoryMapper getObjectDirectoryMapper(ConverterManager converterManager) {
        DefaultObjectDirectoryMapper objectDirectoryMapper = new DefaultObjectDirectoryMapper();
        objectDirectoryMapper.setConverterManager(converterManager);
        return objectDirectoryMapper;
    }


    /**
     * Get LDAP template
     * 
     * @param contextSource context source
     * @param objectDirectoryMapper object directory mapper
     * @return LDAP template
     */
	@Bean(name="ldapTemplate")
	@Primary
	public LdapTemplate getLdapTemplate(TransactionAwareContextSourceProxy contextSource, ObjectDirectoryMapper objectDirectoryMapper) {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setObjectDirectoryMapper(objectDirectoryMapper);
        return ldapTemplate;
	}
	

    /**
     * Get authenticate LDAP template.
     * 
     * @return LDAP template
     */
	@Bean(name="authenticateLdapTemplate")
	public LdapTemplate getAuthenticateLdapTemplate() {
		LdapContextSource source = new LdapContextSource();
		source.setUrl(System.getProperty("ldap.url"));
		
		source.setPooled(false);
		Map<String, Object> baseEnvironmentProperties = new HashMap<String, Object>();
		baseEnvironmentProperties.put("com.sun.jndi.ldap.connect.timeout", System.getProperty("ldap.timeout"));
		source.setBaseEnvironmentProperties(baseEnvironmentProperties);
		source.afterPropertiesSet();	
		
		return new LdapTemplate(source);
	}
	
	
    /**
     * Get transaction manager.
     * 
     * @return transaction manager
     */
	@Bean
	public ContextSourceTransactionManager getTxManager() {
		ContextSourceTransactionManager txManager = new ContextSourceTransactionManager();
		txManager.setContextSource((ContextSource) applicationContext.getBean("contextSourceTransactionAwareProxy"));
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
		txManagerDelegate.setContextSource((ContextSource) applicationContext.getBean("contextSourceTransactionAwareProxy"));
		
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
     * Get internationalization service
     * 
     * @return internationalization service
     */
    @Bean
    public IInternationalizationService getInternationalizationService() {
        return Locator.findMBean(IInternationalizationService.class, IInternationalizationService.MBEAN_NAME);
    }


    /**
     * Get bundle factory.
     *
     * @param internationalizationService internationalization service
     * @return bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory(IInternationalizationService internationalizationService) {
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader(), this.applicationContext);
    }

}
