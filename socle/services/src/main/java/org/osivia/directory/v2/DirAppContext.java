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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Class used to scan all beans within the package
 * Sub-library shoud have a @Configuration class with this package to be checked
 * @author Loïc Billon
 * @since 4.4
 */
@Configuration
@EnableCaching
@ComponentScan(basePackages = "org.osivia.directory.v2")
public class DirAppContext {


	@Autowired
	private ApplicationContext context;
	
	@Bean(name="ehcache")
	public EhCacheManagerFactoryBean getCacheFactory() {
		EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
		factory.setConfigLocation(new ClassPathResource("ehcache-ldap.xml"));
		return factory;
	}
	
	@Bean(name="cacheManager")
	public CacheManager getCacheManager() {
		
		EhCacheManagerFactoryBean bean = context.getBean(EhCacheManagerFactoryBean.class);
		
		EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(bean.getObject());

		return ehCacheCacheManager;
	}
}
