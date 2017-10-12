package org.osivia.services.usersettings.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get user workspaces Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetUserWorkspacesCommand implements INuxeoCommand {

    /** Current user name. */
    private final String user;


    /**
     * Constructor.
     * 
     * @param user current user name
     */
    public GetUserWorkspacesCommand(String user) {
        super();
        this.user = user;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'Workspace' ");
        clause.append("AND ttcs:spaceMembers/*/login = '").append(this.user).append("' ");
        clause.append("ORDER BY dc:title ASC");

        // Query filter
        NuxeoQueryFilterContext filter = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_LIVE,
                InternalConstants.PORTAL_CMS_REQUEST_FILTERING_POLICY_LOCAL);

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(filter, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice, toutatice_space");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.user);
        return builder.toString();
    }

}
