package fr.toutatice.identite.portail.fichePersonne.nuxeo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.identite.portail.fichePersonne.ProfilNuxeo;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

public class FetchDocumentCommand  implements INuxeoCommand{
	
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.identite");


	private NuxeoController nxController;
	
	private String nuxeoPath;
	

	public FetchDocumentCommand(NuxeoController nxController, String nuxeoPath) {
		super();
		this.nxController = nxController;
		this.nuxeoPath = nuxeoPath;
	
	}
	
	
	

	/**
	 * execution d'une requete nuxéo permettant de récupérer les paramètres du formulaire de contact dans le document Nuxeo
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		
		Document doc = (Document) automationSession.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", nuxeoPath).execute();
		 
		return doc;
	
	}

	public String getId() {
		return "FetchNuxeoCommand";
	}
	
	
	

}
