package fr.toutatice.identite.portail.fichePersonne.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.toutatice.identite.portail.fichePersonne.formulaire.FormSurchargeMdp;



@Component("surchargeMdpValidator")
public class SurchargeMdpValidator implements Validator {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.fichePersonne");
	
	public boolean supports(Class<?> klass) {
		return FormSurchargeMdp.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {
		
		FormSurchargeMdp form = (FormSurchargeMdp) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mdpSurcharge", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mdpUserConnecte", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "motifSurcharge", "NotEmpty.field");
	}
	
}
