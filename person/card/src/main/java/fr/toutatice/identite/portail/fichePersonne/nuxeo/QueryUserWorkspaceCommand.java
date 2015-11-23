package fr.toutatice.identite.portail.fichePersonne.nuxeo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class QueryUserWorkspaceCommand  implements INuxeoCommand{
	
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.identite");


	String login;
	

	public QueryUserWorkspaceCommand(String uid) {
		super();
		this.login = uid.toLowerCase();
	
	}
	
	
	

	/**
	 * execution d'une requete nuxéo permettant de récupérer les paramètres du formulaire de contact dans le document Nuxeo
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		Documents result = (Documents) automationSession.newRequest("Document.Query").setHeader(Constants.HEADER_NX_SCHEMAS, "*")
                .set("query",
                        "SELECT * FROM Workspace WHERE ttc:spaceID = '" + this.login.toLowerCase()
                                + "' AND ecm:isProxy=0 AND ecm:currentLifeCycleState <> 'deleted'").execute();
		 
		return result;
	
	}

	public String getId() {
		return "FetchNuxeoCommand";
	}
	
	
}
