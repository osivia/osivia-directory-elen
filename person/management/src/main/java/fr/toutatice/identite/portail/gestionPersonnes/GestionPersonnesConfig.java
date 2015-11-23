package fr.toutatice.identite.portail.gestionPersonnes;

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
public class GestionPersonnesConfig implements InitializingBean {

	protected static final Log logger = LogFactory.getLog("identite.config");

	private Integer minCarsSearch;

	private Boolean showStructures;

	private Boolean enableOverload;

	private Boolean enableCreation;

	private Boolean enableDeletion;

	private String roleSuperAdministrateur;
	private String roleAdministrateur;
	private String roleAssistance;

	public GestionPersonnesConfig() {
	}

	public Integer getMinCarsSearch() {
		return minCarsSearch;
	}

	@Value("#{systemProperties['gestionpersonnes.config.minCarsSearch']}")
	public void setMinCarsSearch(Integer minCarsSearch) {
		// injection des propriétés des fichiers de config
		this.minCarsSearch = minCarsSearch;
	}

	public Boolean getShowStructures() {
		return showStructures;
	}

	@Value("#{systemProperties['gestionpersonnes.config.showStructures']}")
	public void setShowStructures(Boolean showStructures) {
		this.showStructures = showStructures;
	}

	/**
	 * @return the enableOverload
	 */
	public Boolean getEnableOverload() {
		return enableOverload;
	}

	/**
	 * @param enableOverload
	 *            the enableOverload to set
	 */
	@Value("#{systemProperties['identity.config.enableOverload']}")
	public void setEnableOverload(Boolean enableOverload) {
		this.enableOverload = enableOverload;
	}

	public Boolean getEnableCreation() {
		return enableCreation;
	}

	@Value("#{systemProperties['gestionpersonnes.config.enableCreation']}")
	public void setEnableCreation(Boolean enableCreation) {
		this.enableCreation = enableCreation;
	}

	public Boolean getEnableDeletion() {
		return enableDeletion;
	}

	@Value("#{systemProperties['gestionpersonnes.config.enableDeletion']}")
	public void setEnableDeletion(Boolean enableDeletion) {
		this.enableDeletion = enableDeletion;
	}


	/**
	 * @return the roleSuperAdministrateur
	 */
	public String getRoleSuperAdministrateur() {
		return roleSuperAdministrateur;
	}

	/**
	 * @param roleSuperAdministrateur
	 *            the roleSuperAdministrateur to set
	 */
	@Value("#{systemProperties['gestionpersonnes.roles.roleSuperAdministrateur']}")
	public void setRoleSuperAdministrateur(String roleSuperAdministrateur) {
		this.roleSuperAdministrateur = roleSuperAdministrateur;
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
	@Value("#{systemProperties['gestionpersonnes.roles.roleAdministrateur']}")
	public void setRoleAdministrateur(String roleAdministrateur) {
		this.roleAdministrateur = roleAdministrateur;
	}

	/**
	 * @return the roleAssistance
	 */
	public String getRoleAssistance() {
		return roleAssistance;
	}

	/**
	 * @param roleAssistance
	 *            the roleAssistance to set
	 */
	@Value("#{systemProperties['gestionpersonnes.roles.roleAssistance']}")
	public void setRoleAssistance(String roleAssistance) {
		this.roleAssistance = roleAssistance;
	}

	public void afterPropertiesSet() throws Exception {

		if (minCarsSearch == null) {
			minCarsSearch = 3;
		}

		if (showStructures == null) {
			showStructures = false;
		}

		if (enableCreation == null) {
			enableCreation = false;
		}

		Map<String, Object> describe = PropertyUtils.describe(this);
		for (Map.Entry<String, Object> entry : describe.entrySet()) {
			logger.debug(entry.toString());
		}
	}
}
