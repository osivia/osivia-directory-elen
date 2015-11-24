package fr.toutatice.identite.portail.ficheProfil;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("config")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class FicheProfilConfig implements InitializingBean {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.identite");

	/**
	 * Permet la prise en charge de macro profils
	 */
	private Boolean enableMacroProfils;
	
	private String macroProfilMER;
	private String macroProfilEN;
	private String macroProfilAGRI;
	
	private String roleAdministrateur;

	
	
	public Boolean getEnableMacroProfils() {
		return enableMacroProfils;
	}

	@Value("#{systemProperties['ficheProfil.config.enableMacroProfils']}")
	public void setEnableMacroProfils(Boolean enableMacroProfils) {
		this.enableMacroProfils = enableMacroProfils;
	}

	public String getMacroProfilMER() {
		return macroProfilMER;
	}

	@Value("#{systemProperties['ficheProfil.macroprofils.MER']}")
	public void setMacroProfilMER(String macroProfilMER) {
		this.macroProfilMER = macroProfilMER;
	}

	public String getMacroProfilEN() {
		return macroProfilEN;
	}

	@Value("#{systemProperties['ficheProfil.macroprofils.EN']}")
	public void setMacroProfilEN(String macroProfilEN) {
		this.macroProfilEN = macroProfilEN;
	}

	public String getMacroProfilAGRI() {
		return macroProfilAGRI;
	}

	@Value("#{systemProperties['ficheProfil.macroprofils.AGRI']}")
	public void setMacroProfilAGRI(String macroProfilAGRI) {
		this.macroProfilAGRI = macroProfilAGRI;
	}


	/**
	 * @return the roleAdministrateur
	 */
	public String getRoleAdministrateur() {
		return roleAdministrateur;
	}

	/**
	 * @param roleAdministrateur
	 *            the roleAdministrateur to set
	 */
	@Value("#{systemProperties['ficheProfil.roles.roleAdministrateur']}")
	public void setRoleAdministrateur(String roleAdministrateur) {
		this.roleAdministrateur = roleAdministrateur;
	}


	public void afterPropertiesSet() throws Exception {

		if (enableMacroProfils == null) {
			enableMacroProfils = false;
		}

		Map<String, Object> describe = PropertyUtils.describe(this);
		for (Map.Entry<String, Object> entry : describe.entrySet()) {
			logger.debug(entry.toString());
		}
	}

}
