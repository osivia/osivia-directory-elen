package org.osivia.directory.v2.dao;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.model.PortalGroup;

public interface PortalGroupDao extends GroupDao{

    @Override
    PortalGroup get(Name dn);
    
    List<PortalGroup> find(PortalGroup criteria);
}
