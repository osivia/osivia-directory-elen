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

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Change password view validator
 * @author Loïc Billon
 *
 */
@Component("chgPwdValidator")
public class ChgPwdValidator implements Validator {

	public boolean supports(Class<?> klass) {
		return FormChgPwd.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {
		
		
		FormChgPwd fiche = (FormChgPwd) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPwd", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPwd", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPwd", "NotEmpty.field");
	
		if(fiche.getNewPwd().equals(fiche.getCurrentPwd())) {
			errors.rejectValue("newPwd", "Pwd.not.changed");
		}
		
		if(!fiche.getNewPwd().equals(fiche.getConfirmPwd())) {
			errors.rejectValue("confirmPwd", "Not.Same.Pwd");
		}
		
	}

}
