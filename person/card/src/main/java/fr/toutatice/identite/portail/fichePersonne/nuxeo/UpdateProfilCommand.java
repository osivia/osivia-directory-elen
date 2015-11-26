package fr.toutatice.identite.portail.fichePersonne.nuxeo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;

import fr.toutatice.identite.portail.fichePersonne.ProfilNuxeo;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

public class UpdateProfilCommand implements INuxeoCommand {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");


	private NuxeoController nxController;

	private String nuxeoPath;
	private ProfilNuxeo profilNx;
	private File newAvatar;



	public ProfilNuxeo getProfilNx() {
		return this.profilNx;	}
	public void setProfilNx(ProfilNuxeo profilNx) {
		this.profilNx = profilNx;	}




	public UpdateProfilCommand(NuxeoController nxController, String nuxeoPath, ProfilNuxeo prf, File avatar) {
		super();
		this.nxController = nxController;
		this.nuxeoPath = nuxeoPath;
		this.profilNx = prf;
		this.newAvatar = avatar;

	}




	public File getNewAvatar() {
		return this.newAvatar;
	}
	public void setNewAvatar(File newAvatar) {
		this.newAvatar = newAvatar;
	}
	/**
	 * execution d'une requete nuxeo permettant de créér un document contenant la participation au projet refondons l'école
	 * @return
	 */
	@Override
    public Object execute(Session automationSession) throws Exception {


		Document doc = (Document) automationSession.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", this.nuxeoPath).execute();

		if(this.getNewAvatar()!=null){
			DocumentService rs = automationSession.getAdapter(DocumentService.class);
			DocRef docRef = new DocRef(doc.getId());

			Blob blob = new FileBlob(this.getNewAvatar());
			rs.setBlob(docRef, blob, "userprofile:avatar");
		}

		Map<String, String> propertiesToUpdate = new HashMap<String, String>();

        propertiesToUpdate.put("ttc_userprofile:bio", this.profilNx.getBio());
        propertiesToUpdate.put("userprofile:phonenumber", this.profilNx.getTelFixe());
        propertiesToUpdate.put("ttc_userprofile:mobile", this.profilNx.getTelMobile());
        propertiesToUpdate.put("ttc_userprofile:profession", this.profilNx.getProfession());



		OperationRequest majFicheProfil = automationSession.newRequest("Document.Update").setInput(doc);


        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> property : propertiesToUpdate.entrySet()) {
        	if(StringUtils.isNotBlank(property.getValue())) {
	            sb.append(property.getKey());
	            sb.append("=");
	            sb.append(property.getValue());
	            sb.append("\n");
        	}
        }

		majFicheProfil.set("properties",sb.toString());


		return majFicheProfil.execute();

	}

	@Override
    public String getId() {
		return "MonProfilNuxeoCommand";
	}
}
