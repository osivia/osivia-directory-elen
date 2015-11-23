package fr.toutatice.identite.portail.fichePersonne.validator;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.toutatice.identite.portail.fichePersonne.Fiche;
import fr.toutatice.identite.portail.fichePersonne.formulaire.FormUpload;

@Component("modifyValidator")
public class ModifyValidator implements Validator {

	protected static final Log logger = LogFactory
			.getLog("fr.toutatice.services.fichePersonne");

	public boolean supports(Class<?> klass) {
		return Fiche.class.isAssignableFrom(klass);
	}

	public void validate(Object target, Errors errors) {

		FormUpload form = (FormUpload) target;

		// Le champ mail n'est pas forcément affiché, on ne le contrôle que dans ce cas là
		if (form.getNouveauEmail() != null) {

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nouveauEmail",
					"NotEmpty.field");

			String email = StringUtils.defaultIfEmpty(form.getNouveauEmail(), "");
			// boolean emailValide =
			// Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$",
			// email);
			boolean emailValide = Pattern.matches(
					"^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", email);
			if (!emailValide) {
				errors.rejectValue("nouveauEmail", "Email.Invalide");
			}
		}

	}

}
