package org.osivia.directory.v2.dao;

import java.util.List;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Group;

/**
 * LDAP group DAO interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface GroupDao {

    /**
     * Get group.
     * 
     * @param dn group DN
     * @return group
     */
    Group get(Name dn);


    /**
     * Find groups.
     * 
     * @param criteria search criteria
     * @return groups
     */
    List<Group> find(Group criteria);


    /**
     * Delete group.
     * 
     * @param dn group DN
     */
    void delete(Name dn);

}
