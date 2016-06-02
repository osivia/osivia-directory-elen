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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;


/**
 * Config Implémentation of service
 * @author Loïc Billon
 * @since 4.4
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.directory.v2")
public class AppConfig {
	
	@Autowired
	private ApplicationContext context;

	@Bean(name="contextSource")
	public LdapContextSource getLdapContextSource() {
		
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
	
		return source;
	}

	
	@Bean(name="ldapTemplate")
	public LdapTemplate getLdapTemplate() {
		
		LdapContextSource contextSource = context.getBean(LdapContextSource.class);
		return new LdapTemplate(contextSource);
	}
}
