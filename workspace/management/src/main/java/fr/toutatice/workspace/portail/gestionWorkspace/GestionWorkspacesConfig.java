package fr.toutatice.workspace.portail.gestionWorkspace;

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
public class GestionWorkspacesConfig implements InitializingBean {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.nuxeo");

	private String pathOpenWorkspaceDirectory;
	
	private String pathTemplateWorkspace;
	
	private String wsShortName;
	
	private Boolean enableReaders;
	
	private Boolean enableGroups;
	
	private String excludeUsers;
	
	
	private String groupAnimateurs;
	private String groupContributeurs;
	private String groupLecteurs;
	
	
	
	
	/**
	 * @return the pathOpenWorkspaceDirectory
	 */
	public String getPathOpenWorkspaceDirectory() {
		return pathOpenWorkspaceDirectory;
	}

	/**
	 * @param pathOpenWorkspaceDirectory the pathOpenWorkspaceDirectory to set
	 */
	@Value("#{systemProperties['gestionWorkspace.pathOpenWorkspaceDirectory']}")
	public void setPathOpenWorkspaceDirectory(String pathOpenWorkspaceDirectory) {
		this.pathOpenWorkspaceDirectory = pathOpenWorkspaceDirectory;
	}

	/**
	 * @return the pathTemplateWorkspace
	 */
	public String getPathTemplateWorkspace() {
		return pathTemplateWorkspace;
	}

	/**
	 * @param pathTemplateWorkspace the pathTemplateWorkspace to set
	 */
	@Value("#{systemProperties['gestionWorkspace.pathTemplateWorkspace']}")
	public void setPathTemplateWorkspace(String pathTemplateWorkspace) {
		this.pathTemplateWorkspace = pathTemplateWorkspace;
	}

	/**
	 *
	 * @return the wsShortName
	 */
	public String getWsShortName() {
		return wsShortName;
	}

	/**
	 * @param wsShortName the wsShortName to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.wsShortName']}")
	public void setWsShortName(String wsShortName) {
		this.wsShortName = wsShortName;
	}

	/**
	 * @return the enableReaders
	 */
	public Boolean getEnableReaders() {
		return enableReaders;
	}

	/**
	 * @param enableReaders the enableReaders to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.enableReaders']}")
	public void setEnableReaders(Boolean enableReaders) {
		this.enableReaders = enableReaders;
	}
	
	/**
	 * @return the enableGroups
	 */
	public Boolean getEnableGroups() {
		return enableGroups;
	}
	
	

	/**
	 * @return the excludeUsers
	 */
	public String getExcludeUsers() {
		return excludeUsers;
	}

	/**
	 * @param excludeUsers the excludeUsers to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.excludeUsers']}")
	public void setExcludeUsers(String excludeUsers) {
		this.excludeUsers = excludeUsers;
	}

	/**
	 * @param enableGroups the enableGroups to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.enableGroups']}")
	public void setEnableGroups(Boolean enableGroups) {
		this.enableGroups = enableGroups;
	}

	
	
	
	/**
	 * @return the groupAnimateurs
	 */
	public String getGroupAnimateurs() {
		return groupAnimateurs;
	}

	/**
	 * @param groupAnimateurs the groupAnimateurs to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.groupAnimateurs']}")
	public void setGroupAnimateurs(String groupAnimateurs) {
		this.groupAnimateurs = groupAnimateurs;
	}

	/**
	 * @return the groupContributeurs
	 */
	public String getGroupContributeurs() {
		return groupContributeurs;
	}

	/**
	 * @param groupContributeurs the groupContributeurs to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.groupContributeurs']}")
	public void setGroupContributeurs(String groupContributeurs) {
		this.groupContributeurs = groupContributeurs;
	}

	/**
	 * @return the groupLecteurs
	 */
	public String getGroupLecteurs() {
		return groupLecteurs;
	}

	/**
	 * @param groupLecteurs the groupLecteurs to set
	 */
	@Value("#{systemProperties['gestionWorkspace.config.groupLecteurs']}")
	public void setGroupLecteurs(String groupLecteurs) {
		this.groupLecteurs = groupLecteurs;
	}

	public void afterPropertiesSet() throws Exception {

		if(enableGroups == null) {
			enableGroups = Boolean.TRUE;
		}
		if(enableReaders == null) {
			enableReaders = Boolean.TRUE;
		}
		

		Map<String, Object> describe = PropertyUtils.describe(this);
		for (Map.Entry<String, Object> entry : describe.entrySet()) {
			logger.debug(entry.toString());
		}
	}

}
