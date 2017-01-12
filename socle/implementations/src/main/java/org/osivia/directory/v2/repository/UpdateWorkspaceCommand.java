package org.osivia.directory.v2.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.core.cms.CMSException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Update workspace Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateWorkspaceCommand implements INuxeoCommand {

    /** Workspace identifier. */
    private final String workspaceId;
    /** User identifier. */
    private final String user;
    /** Attach member indicator. */
    private final boolean attach;
    
    /**
     * Constructor.
     * 
     * @param workspaceId workspace identifier
     * @param user user identifier
     * @param attach attach member indicator
     */
    public UpdateWorkspaceCommand(String workspaceId, String user, boolean attach) {
        super();
        this.workspaceId = workspaceId;
        this.user = user;
        this.attach = attach;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Workspace Nuxeo document
        Document workspace = this.getWorkspace(nuxeoSession);

        // Members
        PropertyList members = workspace.getProperties().getList("ttcs:spaceMembers");

        // Updated properties
        PropertyMap properties = new PropertyMap();
        properties.set("ttcs:spaceMembers", this.generateUpdatedMembersJSON(members));

        documentService.update(workspace, properties);

        return null;
    }


    /**
     * Get workspace Nuxeo document.
     * 
     * @param nuxeoSession Nuxeo session
     * @return Nuxeo document
     * @throws Exception
     */
    protected Document getWorkspace(Session nuxeoSession) throws Exception {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document WHERE ");
        query.append("ecm:primaryType = 'Workspace' ");
        query.append("AND webc:url = '").append(this.workspaceId).append("' ");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.Query");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice, toutatice_space");
        request.set("query", query.toString());

        // Documents
        Documents documents = (Documents) request.execute();

        // Workspace Nuxeo document
        Document workspace;
        if (documents.size() == 1) {
            workspace = documents.get(0);
        } else {
            throw new CMSException(CMSException.ERROR_NOTFOUND);
        }

        return workspace;
    }


    /**
     * Generate updated members JSON content.
     *
     * @param members members
     * @return JSON
     */
    protected String generateUpdatedMembersJSON(PropertyList members) {
        JSONArray array = new JSONArray();

        for (int i = 0; i < members.size(); i++) {
            PropertyMap member = members.getMap(i);
            String login = member.getString("login");

            if (StringUtils.equals(this.user, login)) {
                // Do nothing
            } else {
                JSONObject object = new JSONObject();
                for (Entry<String, Object> entry : member.getMap().entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    object.put(key, value);
                }
                array.add(object);
            }
        }
        
        if (this.attach) {
            // Date format
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            JSONObject object = new JSONObject();
            object.put("login", this.user);
            object.put("joinedDate", dateFormat.format(new Date()));
            array.add(object);
        }
        
        return array.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
