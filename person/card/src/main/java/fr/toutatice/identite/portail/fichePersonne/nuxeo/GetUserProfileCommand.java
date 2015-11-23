package fr.toutatice.identite.portail.fichePersonne.nuxeo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;



public class GetUserProfileCommand  implements INuxeoCommand{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");

	private String username;

	public GetUserProfileCommand() {

	}

	public GetUserProfileCommand(String username) {
		this.username = username;
	}

	/**
	 * execution d'une requete nuxéo permettant de créér un document contenant la participation au projet refondons l'école
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
	
		OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
		if (username != null) {
			newRequest.set("username", username);
		}

		return newRequest.execute();
		
	}

	public String getId() {
		return "GetUserProfileCommand";
	}
	
	
	
	

}
