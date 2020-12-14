package org.osivia.directory.v2.repository;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CleanWorkspaceParticipationCommand  implements INuxeoCommand{

	private final String userId;
	
	private String workspaceId;
	
	public CleanWorkspaceParticipationCommand(String userId, String workspaceId) {
		this.userId = userId;
		this.workspaceId = workspaceId;
	}
		
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		OperationRequest newRequest = nuxeoSession.newRequest("Services.WorkspaceUnsubscribe");
		newRequest.set("userId", userId);
		newRequest.set("workspaceId", workspaceId);
		
        newRequest.execute();
        
        return null;
	}

	@Override
	public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append(this.userId);
        builder.append("/");
        builder.append(this.workspaceId);
        return builder.toString();
	}

}
