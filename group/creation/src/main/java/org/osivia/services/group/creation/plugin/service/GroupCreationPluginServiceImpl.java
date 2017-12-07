package org.osivia.services.group.creation.plugin.service;

import java.text.Normalizer;
import java.util.Map;

import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.directory.v2.service.PortalGroupService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Group creation plugin service implementation.
 *
 * @author Julien Barberet
 * @see GroupCreationPluginService
 */
@Service
public class GroupCreationPluginServiceImpl implements GroupCreationPluginService {

    /** PortalGroup service. */
    @Autowired
    private PortalGroupService service;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Constructor.
     */
    public GroupCreationPluginServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createGroup(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        // HTTP servlet request
        HttpServletRequest servletRequest = context.getPortalControllerContext().getHttpServletRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

        // Variables
        Map<String, String> variables = context.getVariables();

        // Display name
        String displayNameVariableName = context.getParamValue(executor, DISPLAYNAME_FILTER_VARIABLE_NAME);
        String displayName = StringUtils.trim(variables.get(displayNameVariableName));

        // Description
        String descriptionVariableName = context.getParamValue(executor, DESCRIPTION_FILTER_VARIABLE_NAME);
        String description = StringUtils.trim(variables.get(descriptionVariableName));

        // CN
        String cn = this.generateCn(displayName);

        if (StringUtils.isEmpty(cn)) {
            String message = bundle.getString("GROUP_CREATION_FORM_FILTER_MESSAGE_EMPTY_DISPLAYNAME");
            throw new FormFilterException(message);
        } else {
            int i = 1;
            boolean created = false;
            while (!created) {
                // Suffixed CN
                String suffixedCn = cn;
                if (i > 1) {
                    suffixedCn += "-" + i;
                }

                Name dn = this.service.buildDn(suffixedCn);
                PortalGroup portalGroup = this.service.get(dn);

                if (portalGroup == null) {
                    this.create(suffixedCn, displayName, description);
                    created = true;
                }

                i++;
            }
        }
    }


    /**
     * Create group.
     * 
     * @param cn group CN
     * @param displayName group display name
     * @param description group description
     */
    private void create(String cn, String displayName, String description) {
        PortalGroup portalGroup = this.service.create(cn);

        portalGroup.setDisplayName(displayName);
        portalGroup.setDescription(StringUtils.trimToNull(description));

        this.service.update(portalGroup);
    }


    /**
     * Generate CN from display name.
     *
     * @param displayName display name
     * @return CN
     */
    private String generateCn(String displayName) {
        String name = displayName;

        // Lower case
        name = StringUtils.lowerCase(name);

        // Remove accents
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = name.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Remove special characters
        name = name.replaceAll("[^a-z0-9]", "-");

        // Remove "-" prefix
        while (StringUtils.startsWith(name, "-")) {
            name = StringUtils.removeStart(name, "-");
        }

        // Remove "-" suffix
        while (StringUtils.endsWith(name, "-")) {
            name = StringUtils.removeEnd(name, "-");
        }

        // Remove consecutive "-"
        while (StringUtils.contains(name, "--")) {
            name = StringUtils.replace(name, "--", "-");
        }

        return name;
    }

}
