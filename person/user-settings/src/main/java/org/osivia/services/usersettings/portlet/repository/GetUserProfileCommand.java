package org.osivia.services.usersettings.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get user profile Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetUserProfileCommand implements INuxeoCommand {

    /** Current user name. */
    private final String user;


    /**
     * Constructor.
     * 
     * @param user current user name
     */
    public GetUserProfileCommand(String user) {
        super();
        this.user = user;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Services.GetToutaticeUserProfile");
        request.set("username", this.user);

        // User profile document
        Document document = (Document) request.execute();

        // Get full document
        request = nuxeoSession.newRequest("Document.Fetch");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("value", document.getPath());

        return (Document) request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.user);
        return builder.toString();
    }

}
