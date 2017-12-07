package org.osivia.services.group.creation.plugin.service;

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

        // DisplayName
        String displayNameVariableName = context.getParamValue(executor, DISPLAYNAME_FILTER_VARIABLE_NAME);
        String displayName = StringUtils.trim(variables.get(displayNameVariableName));
        
        String descriptionVariableName = context.getParamValue(executor, DESCRIPTION_FILTER_VARIABLE_NAME);
        String description = StringUtils.trim(variables.get(descriptionVariableName));

        String cn = StringUtils.deleteWhitespace(displayName);
        
        if (cn.length()==0)
        {
            String message = bundle.getString("GROUP_CREATION_FORM_FILTER_MESSAGE_EMPTY_DISPLAYNAME");
            throw new FormFilterException(message);
        } else
        {
            int i = 1;
            boolean created = false;
            PortalGroup portalGroup;
            // PortalGroup
            while (!created)
            {
                Name dn = this.service.buildDn(cn+(i==1? "" : Integer.toString(i)));
                portalGroup = this.service.get(dn);
        
                if (portalGroup == null) {
                    create(cn+(i==1? "" : Integer.toString(i)), displayName, description);
                    created = true;
                }
                i++;
            }
        }
    }
    
    private void create(String cn, String displayName, String description)
    {
        PortalGroup portalGroup = this.service.create(cn);

        portalGroup.setDisplayName(displayName);
        portalGroup.setDescription(description);

        this.service.update(portalGroup);
    }

}
