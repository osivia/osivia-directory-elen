package org.osivia.directory.v2.model;

import org.osivia.portal.api.directory.v2.model.Group;

public interface PortalGroup extends Group {
	
	String getDisplayName();
	
	void setDisplayName(String displayName);
	
	String getDescription();
	
	void setDescription(String description);
	
}
