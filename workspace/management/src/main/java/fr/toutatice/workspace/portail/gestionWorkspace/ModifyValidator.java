package fr.toutatice.workspace.portail.gestionWorkspace;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component("modifyValidator")
public class ModifyValidator implements Validator {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.fichePersonne");
	
	public boolean supports(Class<?> klass) {
		return Workspace.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {
	
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nom", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty.field");
		
		
		
	}
	
}
