package org.osivia.directory.v2.dao;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.PortalGroup;

/**
 * Portal group DAO interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface PortalGroupDao {

    /**
     * Get portal groups base DN
     * 
     * @return DN
     */
    Name getBaseDn();


    /**
     * Find portal group.
     * 
     * @param dn portal group DN
     * @return portal group
     */
    PortalGroup find(Name dn);


    /**
     * Find portal groups.
     * 
     * @param criteria search criteria
     * @return portal groups
     */
    List<PortalGroup> find(PortalGroup criteria);


    /**
     * Create portal group.
     * 
     * @param portalGroup portal group to create
     */
    void create(PortalGroup portalGroup);


    /**
     * Update portal group.
     * 
     * @param portalGroup portal group to update
     */
    void update(PortalGroup portalGroup);


    /**
     * Delete portal group.
     * 
     * @param portalGroup portal group to delete
     */
    void delete(PortalGroup portalGroup);

}
