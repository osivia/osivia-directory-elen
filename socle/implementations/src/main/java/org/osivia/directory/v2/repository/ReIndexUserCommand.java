/**
 * 
 */
package org.osivia.directory.v2.repository;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReIndexUserCommand implements INuxeoCommand  {

	private Document userProfile;

	public ReIndexUserCommand(Document userProfile) {
		this.userProfile = userProfile;
		
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		OperationRequest reindex = nuxeoSession.newRequest("Document.ReIndexES");
		reindex.set("repositoryName", userProfile.getRepository());
		reindex.set("type", "QUERY");
		reindex.set("query", "SELECT * FROM Document where ecm:uuid = '"
				+ userProfile.getId() + "'");
		reindex.execute();
		
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return null;
	}

	
}
