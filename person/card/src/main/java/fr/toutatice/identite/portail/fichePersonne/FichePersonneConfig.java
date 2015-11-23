package fr.toutatice.identite.portail.fichePersonne;

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
public class FichePersonneConfig implements InitializingBean {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.identite");

	/**
	 * Permet l'accès aux fiches parent et aux critères de recherches sur les
	 * élèves, la consultation des établissements scolaires
	 */
	private Boolean enableModeENT;
	private Boolean enableResetPwd;

	/**
	 * Prise en charge de la surcharge de mot de passe
	 */
	private Boolean enableOverload;

	private Boolean enableSendMail;
	private Boolean enableEditPersonInfos;

	private Boolean anyoneCanViewProfiles;
	private Boolean forceChangePwd;

	private Boolean deleteSurchageWhenUpdatePwd;

	private String roleSuperAdministrateur;
	private String roleAdministrateur;
	private String roleRazMdp;
	private String roleAssistance;
	private String roleNonSurchargeable;

	/**
	 * @return the enableModeENT
	 */
	public Boolean getEnableModeENT() {
		return enableModeENT;
	}

	/**
	 * @param enableModeENT
	 *            the enableModeENT to set
	 */
	@Value("#{systemProperties['fichePersonne.config.enableModeENT']}")
	public void setEnableModeENT(Boolean enableModeENT) {
		this.enableModeENT = enableModeENT;
	}


	/**
	 * @return the enableResetPwd
	 */
	public Boolean getEnableResetPwd() {
		return enableResetPwd;
	}

	/**
	 * @param enableResetPwd
	 *            the enableResetPwd to set
	 */
	@Value("#{systemProperties['fichePersonne.config.enableResetPwd']}")
	public void setEnableResetPwd(Boolean enableResetPwd) {
		this.enableResetPwd = enableResetPwd;
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

	/**
	 * @return the enableSendMail
	 */
	public Boolean getEnableSendMail() {
		return enableSendMail;
	}

	/**
	 * @param enableSendMail
	 *            the enableSendMail to set
	 */
	@Value("#{systemProperties['fichePersonne.config.enableSendMail']}")
	public void setEnableSendMail(Boolean enableSendMail) {
		this.enableSendMail = enableSendMail;
	}

	/**
	 * @return the enableEditPersonInfos
	 */
	public Boolean getEnableEditPersonInfos() {
		return enableEditPersonInfos;
	}

	/**
	 * @param enableEditPersonInfos
	 *            the enableEditPersonInfos to set
	 */
	@Value("#{systemProperties['fichePersonne.config.enableEditPersonInfos']}")
	public void setEnableEditPersonInfos(Boolean enableEditPersonInfos) {
		this.enableEditPersonInfos = enableEditPersonInfos;
	}

	/**
	 * @return the anyoneCanViewProfiles
	 */
	public Boolean getAnyoneCanViewProfiles() {
		return anyoneCanViewProfiles;
	}

	/**
	 * @param anyoneCanViewProfiles
	 *            the anyoneCanViewProfiles to set
	 */
	@Value("#{systemProperties['fichePersonne.config.anyoneCanViewProfiles']}")
	public void setAnyoneCanViewProfiles(Boolean anyoneCanViewProfiles) {
		this.anyoneCanViewProfiles = anyoneCanViewProfiles;
	}

	/**
	 * @return the forceChangePwd
	 */
	public Boolean getForceChangePwd() {
		return forceChangePwd;
	}

	/**
	 * @param forceChangePwd
	 *            the forceChangePwd to set
	 */
	@Value("#{systemProperties['fichePersonne.config.forceChangePwd']}")
	public void setForceChangePwd(Boolean forceChangePwd) {
		this.forceChangePwd = forceChangePwd;
	}

	/**
	 * @return the deleteSurchageWhenUpdatePwd
	 */
	public Boolean getDeleteSurchageWhenUpdatePwd() {
		return deleteSurchageWhenUpdatePwd;
	}

	/**
	 * @param deleteSurchageWhenUpdatePwd
	 *            the deleteSurchageWhenUpdatePwd to set
	 */
	@Value("#{systemProperties['fichePersonne.config.deleteSurchageWhenUpdatePwd']}")
	public void setDeleteSurchageWhenUpdatePwd(Boolean deleteSurchageWhenUpdatePwd) {
		this.deleteSurchageWhenUpdatePwd = deleteSurchageWhenUpdatePwd;
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
	@Value("#{systemProperties['fichePersonne.roles.roleSuperAdministrateur']}")
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
	@Value("#{systemProperties['fichePersonne.roles.roleAdministrateur']}")
	public void setRoleAdministrateur(String roleAdministrateur) {
		this.roleAdministrateur = roleAdministrateur;
	}

	/**
	 * @return the roleRazMdp
	 */
	public String getRoleRazMdp() {
		return roleRazMdp;
	}

	/**
	 * @param roleRazMdp
	 *            the roleRazMdp to set
	 */
	@Value("#{systemProperties['fichePersonne.roles.roleRazMdp']}")
	public void setRoleRazMdp(String roleRazMdp) {
		this.roleRazMdp = roleRazMdp;
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
	@Value("#{systemProperties['fichePersonne.roles.roleAssistance']}")
	public void setRoleAssistance(String roleAssistance) {
		this.roleAssistance = roleAssistance;
	}

	/**
	 * @return the roleNonSurchargeable
	 */
	public String getRoleNonSurchargeable() {
		return roleNonSurchargeable;
	}

	/**
	 * @param roleNonSurchargeable
	 *            the roleNonSurchargeable to set
	 */
	@Value("#{systemProperties['fichePersonne.roles.roleNonSurchargeable']}")
	public void setRoleNonSurchargeable(String roleNonSurchargeable) {
		this.roleNonSurchargeable = roleNonSurchargeable;
	}

	public void afterPropertiesSet() throws Exception {

		if (enableModeENT == null) {
			enableModeENT = false;
		}

		Map<String, Object> describe = PropertyUtils.describe(this);
		for (Map.Entry<String, Object> entry : describe.entrySet()) {
			logger.debug(entry.toString());
		}
	}

}
