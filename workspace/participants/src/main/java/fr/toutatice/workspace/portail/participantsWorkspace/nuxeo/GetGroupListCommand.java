package fr.toutatice.workspace.portail.participantsWorkspace.nuxeo;

import net.sf.json.JSONArray;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.PathRef;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class GetGroupListCommand implements INuxeoCommand{

	protected static final Log logger = LogFactory.getLog("fr.toutatice.identite");

	private String path;

	
	public GetGroupListCommand(String path) {
		super();
		this.path = path;
	}

	/**
	 * execution d'une requete nuxeo permettant de récupérer les ACL d'un document
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		Blob result = null;
		OperationRequest request = automationSession.newRequest("Document.GetUsersAndGroupsDocument");
		PathRef path = new PathRef(this.path);
		request.setInput(path);
		result = (Blob) request.execute();
		
	
		if (result != null)
		{
			
			String content = IOUtils.toString(result.getStream());
			JSONArray rows = JSONArray.fromObject(content);
			return rows;
			
		}else{
			return null;
		}
			

	}
	
	public String getId() {
		return "GetGroupsDocumentCommand";
	}
	
}
