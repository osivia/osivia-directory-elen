package fr.toutatice.workspace.portail.gestionWorkspace.nuxeo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class RefreshPrincipalCommand implements INuxeoCommand{

		protected static final Log logger = LogFactory.getLog("fr.toutatice.services.nuxeo");


		
		private Document doc;

		public RefreshPrincipalCommand(Document doc) {
			super();
			this.doc = doc;
		
		}
		
		
		

		/**
		 * execution d'une requete nuxéo permettant de récupérer les paramètres du formulaire de contact dans le document Nuxeo
		 * @return 
		 */
		public Object execute(Session automationSession) throws Exception {
			
			Document result = (Document) automationSession.newRequest("Document.RefreshPrincipal").setInput(doc).execute();
			 
			return result;
		
		}

		public String getId() {
			return "RefreshPrincipalCommand";
		}
		

		
	}
