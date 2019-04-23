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
package org.osivia.services.person.card.portlet.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Loïc Billon
 *
 */
@Component("config")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PersonCardConfig implements InitializingBean {

    /**
     * LDAP role for person management
     */
    private String roleAdministrator;

    private Boolean personCanChangePassword;

    /**
     * @return the roleAdministrator
     */
    public String getRoleAdministrator() {
        return roleAdministrator;
    }

    /**
     * @param roleAdministrator the roleAdministrator to set
     */
    @Value("#{systemProperties['personcard.roles.roleAdministrator']}")
    public void setRoleAdministrator(String roleAdministrator) {
        this.roleAdministrator = roleAdministrator;
    }

    /**
     * @return the personCanChangePassword
     */
    public Boolean getPersonCanChangePassword() {
        return personCanChangePassword;
    }

    /**
     * @param personCanChangePassword the personCanChangePassword to set
     */
    @Value("#{systemProperties['personcard.personCanChangePassword']}")
    public void setPersonCanChangePassword(Boolean personCanChangePassword) {
        this.personCanChangePassword = personCanChangePassword;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(personCanChangePassword == null) {
            personCanChangePassword = true;
        }

    }


}