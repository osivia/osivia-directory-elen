package fr.toutatice.identite.portail.creationPersonnes.validator;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.toutatice.identite.portail.creationPersonnes.FicheCreation;
import fr.toutatice.identite.portail.creationPersonnes.formulaire.FormCreation;


@Component("creationValidator")
public class CreationValidator implements Validator {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.fichePersonne");
	
	public boolean supports(Class<?> klass) {
		return FicheCreation.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {
	
		FormCreation form = (FormCreation) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sn","NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "givenName","NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uid","NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.field");
		
		String email = form.getEmail();
		//boolean emailValide = Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", email);
		boolean emailValide = Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", email);
		if(!emailValide) {
			errors.rejectValue("email", "Email.Invalide");
		}
		
	}
	
}
