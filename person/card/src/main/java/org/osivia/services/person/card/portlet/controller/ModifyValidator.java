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

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Loïc Billon
 *
 */
@Component("modifyValidator")
public class ModifyValidator implements Validator {


    public boolean supports(Class<?> klass) {
        return FormEdition.class.isAssignableFrom(klass);
    }

    public void validate(Object target, Errors errors) {

        FormEdition form = (FormEdition) target;

        // Le champ mail n'est pas forcément affiché, on ne le contrôle que dans ce cas là
        if (form.getMail() != null) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mail",
                    "NotEmpty.field");

            String email = StringUtils.defaultIfEmpty(form.getMail(), "");

            boolean emailValide = Pattern.matches(
                    "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", email);
            if (!emailValide) {
                errors.rejectValue("mail", "wrong.mail");
            }
        }

    }

}