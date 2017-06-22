package org.osivia.directory.v2;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * Utility class for LDAP services
 * 
 * @author Dorian Licois
 */
public class LDAPUtil {

    private LDAPUtil() {
    }

    /**
     * PATTERN_RFC2253
     * 
     * @see https://www.ietf.org/rfc/rfc2253.txt
     * */
    private static final Pattern PATTERN_RFC2253 = Pattern.compile("[,+\"<>;]");

    /**
     * sanitize input to clear non rcf2253 complient characters
     * 
     * @param ldapString
     * @return
     */
    public static String sanitizeRfc2253Complient(String ldapString) {

        ldapString = PATTERN_RFC2253.matcher(ldapString).replaceAll(StringUtils.EMPTY);

        return ldapString;
    }

}
