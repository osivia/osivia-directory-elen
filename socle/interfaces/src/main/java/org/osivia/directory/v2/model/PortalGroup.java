package org.osivia.directory.v2.model;

import java.util.List;

import javax.naming.Name;

/**
 * Portal group interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface PortalGroup {
	
    /**
     * Get DN.
     * 
     * @return DN
     */
    Name getDn();


    /**
     * Set DN.
     * 
     * @param dn DN
     */
    void setDn(Name dn);


    /**
     * Get CN.
     * 
     * @return CN
     */
    String getCn();


    /**
     * Set CN.
     * 
     * @param cn CN
     */
    void setCn(String cn);


    /**
     * Get display name.
     * 
     * @return display name
     */
    String getDisplayName();


    /**
     * Set display name.
     * 
     * @param displayName display name
     */
    void setDisplayName(String displayName);


    /**
     * Get description.
     * 
     * @return description
     */
    String getDescription();


    /**
     * Set description.
     * 
     * @param description description
     */
    void setDescription(String description);


    /**
     * Get members.
     * 
     * @return members
     */
    List<Name> getMembers();


    /**
     * Set members.
     * 
     * @param members members
     */
    void setMembers(List<Name> members);

}
