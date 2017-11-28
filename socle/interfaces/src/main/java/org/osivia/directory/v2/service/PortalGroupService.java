package org.osivia.directory.v2.service;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.portal.api.directory.v2.service.GroupService;

public interface PortalGroupService extends GroupService {

	@Override
	PortalGroup get(Name name);

	
	PortalGroup getEmpty();
	
	
	List<PortalGroup> search(PortalGroup criteria);
	
}
