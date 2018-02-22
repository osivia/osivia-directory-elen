package org.osivia.directory.v2.repository;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.DocRefs;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Purge command
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PurgeWorkspaceCommand implements INuxeoCommand {
    
	/** Id of workspaces to purge */
    private String id;

    /**
     * Constructor
     * @param id of workspace to purge
     */
    public PurgeWorkspaceCommand(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        // Inputs
    	DocRefs references = new DocRefs(1);
	    DocRef reference = new DocRef(id);
	    references.add(reference);
        
        return nuxeoSession.newRequest("Services.PurgeDocuments").setInput(references).setHeader("nx_es_sync", "true").execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Services.PurgeDocuments: " + id;
    }

}
