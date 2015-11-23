package fr.toutatice.outils.ldap.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChargementCdcIdEtab {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	
	public static Properties idEtabCdc;

	public static Properties setInstance(String url) {

		if (StringUtils.isNotBlank(url)) {
			try {
				idEtabCdc.load(new FileInputStream(url));
			} catch (FileNotFoundException e) {
				idEtabCdc = new Properties();
				logger.warn("no file found for CdcIdEtab");
			} catch (IOException e) {
				idEtabCdc = new Properties();
				logger.warn("no file found for CdcIdEtab");
			}
		}

		return idEtabCdc;
	}

	public static String getIdCdc(String rne) {
		return idEtabCdc.getProperty(rne);
	}

}
