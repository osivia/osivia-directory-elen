package org.osivia.directory.v2.service;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.portal.api.directory.v2.IDirService;

/**
 * Portal group service interface.
 * 
 * @author Cédric Krommenhoek
 * @see IDirService
 */
public interface PortalGroupService extends IDirService {

    /**
     * Build portal group DN.
     * 
     * @param cn portal group CN
     * @return DN
     */
    Name buildDn(String cn);


    /**
     * Get portal group.
     * 
     * @param dn portal group DN
     * @return portal group
     */
    PortalGroup get(Name dn);
	

    /**
     * Get empty portal group.
     * 
     * @return portal group
     */
	PortalGroup getEmpty();
	
	
    /**
     * Search portal groups.
     * 
     * @param criteria search criteria
     * @return portal groups
     */
	List<PortalGroup> search(PortalGroup criteria);
	

    /**
     * Create portal group.
     * 
     * @param cn CN of portal group to create
     * @return created portal group
     */
    PortalGroup create(String cn);


    /**
     * Update portal group.
     * 
     * @param portalGroup portal group to update
     * @return updated portal group
     */
    PortalGroup update(PortalGroup portalGroup);


    /**
     * Delete portal group.
     * 
     * @param dn portal group DN
     */
    void delete(Name dn);

}
