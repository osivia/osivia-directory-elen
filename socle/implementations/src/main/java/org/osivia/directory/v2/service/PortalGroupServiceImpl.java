package org.osivia.directory.v2.service;

import java.util.List;

import javax.naming.Name;

import org.osivia.directory.v2.dao.PortalGroupDao;
import org.osivia.directory.v2.model.PortalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service("portalGroupService")
public class PortalGroupServiceImpl extends GroupServiceImpl implements PortalGroupService {

    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;
    
    @Autowired
    @Qualifier("portalGroupDao")
    private PortalGroupDao dao;
    
    public PortalGroupServiceImpl() {
        super();
    }

    @Override
    public PortalGroup get(Name name) {
        return this.dao.get(name);
    }

    @Override
    public PortalGroup getEmpty() {
        return this.applicationContext.getBean(PortalGroup.class);
    }

    @Override
    public List<PortalGroup> search(PortalGroup criteria) {
        return this.dao.find(criteria);
    }

}
