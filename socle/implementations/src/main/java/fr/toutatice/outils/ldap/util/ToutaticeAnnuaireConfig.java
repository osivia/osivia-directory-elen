package fr.toutatice.outils.ldap.util;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("annuaireConfig")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ToutaticeAnnuaireConfig  implements InitializingBean {

	protected static final Log logger = LogFactory.getLog(ToutaticeAnnuaireConfig.class);
	
	/**
	 * Permet la prise en charge de tri au niveau LDAP (non supporté par tous les annuaires)
	 */
	private Boolean sortingEnabled;


	public Boolean getSortingEnabled() {
		return sortingEnabled;
	}
	@Value("#{systemProperties['toutaticeAnnuaire.config.sortingEnabled']}")
	public void setSortingEnabled(Boolean sortingEnabled) {
		this.sortingEnabled = sortingEnabled;
	}

	public void afterPropertiesSet() throws Exception {

		if (sortingEnabled == null) {
			sortingEnabled = true;
		}

		if(logger.isDebugEnabled()) {
			Map<String, Object> describe = PropertyUtils.describe(this);
			for (Map.Entry<String, Object> entry : describe.entrySet()) {
				logger.debug(entry.toString());
			}
		}
	}
	
	
}
