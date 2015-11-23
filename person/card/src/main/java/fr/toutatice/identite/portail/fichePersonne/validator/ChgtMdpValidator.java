package fr.toutatice.identite.portail.fichePersonne.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.toutatice.identite.portail.fichePersonne.formulaire.FormChgtMdp;

@Component("chgtMdpValidator")
public class ChgtMdpValidator implements Validator {
	
protected static final Log logger = LogFactory.getLog("fr.toutatice.services.fichePersonne");
	
	public boolean supports(Class<?> klass) {
		return FormChgtMdp.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {
		
		logger.debug("entrée validator chgtMdp");
		
		FormChgtMdp fiche = (FormChgtMdp) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nouveauMdp", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmMdp", "NotEmpty.field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mdpActuel", "NotEmpty.mdpActuel");
		
		if(fiche.getNouveauMdp().length() < 8) {
			errors.rejectValue("nouveauMdp", "Mdp.Trop.Court");
		}
	
		if(!fiche.getNouveauMdp().equals(fiche.getConfirmMdp())) {
			errors.rejectValue("confirmMdp", "Not.Same.Mdp");
		}
		
	}

}
