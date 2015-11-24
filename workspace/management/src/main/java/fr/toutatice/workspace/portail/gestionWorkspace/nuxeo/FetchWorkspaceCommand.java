package fr.toutatice.workspace.portail.gestionWorkspace.nuxeo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.GestionWorkspacesConfig;
import fr.toutatice.workspace.portail.gestionWorkspace.Workspace;

public class FetchWorkspaceCommand  implements INuxeoCommand{
	
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.identite");

	ApplicationContext springCtx;
	private String nuxeoPath;
	

	public FetchWorkspaceCommand(String nuxeoPath, ApplicationContext springCtx) {
		super();
		this.nuxeoPath = nuxeoPath;
		this.springCtx = springCtx;
	}
	
	/**
	 * execution d'une requete nuxéo permettant de récupérer les paramètres du formulaire de contact dans le document Nuxeo
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		
		Document doc = (Document) automationSession.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", nuxeoPath).execute();
		 
		return convertNuxeoDocToWorkspace(doc, automationSession);
	
	}

	private Object convertNuxeoDocToWorkspace(Document doc, Session automationSession) throws Exception {
		
		Workspace wks = (Workspace)springCtx.getBean("workspace");

		
		if ("Workspace".equals(doc.getType())) {
			
			GestionWorkspacesConfig config = springCtx.getBean(GestionWorkspacesConfig.class);
			
			wks.setNom(doc.getString("dc:title"));
			wks.setDescription(doc.getString("dc:description"));
			//wks.setAuteur(doc.getString("acr:auteur"));
			wks.setSourceOrganisationnelle(doc.getString("dc:publisher"));
			wks.setTheme(doc.getString("ttc:theme"));
			wks.setPath(doc.getPath());
			wks.setShortname(doc.getString(config.getWsShortName()));
			
			
			return wks;
		}
		else{
			return doc;
		}

	
	}

	




	public String getId() {
		return "FetchNuxeoCommand";
	}


}
