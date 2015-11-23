package fr.toutatice.identite.portail.fichePersonne.nuxeo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

public class QueryDocumentCommand implements INuxeoCommand{

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.organisation");

	String requete;


	public QueryDocumentCommand(String requete) {
		super();
		this.requete = requete;
	}

	/**
	 * execution d'une requete nuxéo permettant de récupérer les paramètres du formulaire de contact dans le document Nuxeo
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		
		Documents result = (Documents) automationSession.newRequest("Document.Query").setHeader(Constants.HEADER_NX_SCHEMAS, "*")
				.set("query", "SELECT * FROM Document WHERE "+this.requete).execute();
		 
		return result;
	
	}

	public String getId() {
		return "QueryNuxeoCommand";
	}
	

	
}
