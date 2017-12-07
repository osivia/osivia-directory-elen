package org.osivia.directory.v2.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Name;
import javax.naming.NameNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.dao.PortalGroupDao;
import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

/**
 * Portal group service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see PortalGroupService
 */
@Service
public class PortalGroupServiceImpl implements PortalGroupService {

    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;
    
    /** Person service. */
    @Autowired
    private PersonUpdateService personService;

    /** Portal group DAO. */
    @Autowired
    private PortalGroupDao dao;
    
    /** Portal group sample. */
    @Autowired
    private PortalGroup sample;


    /** Log. */
    private final Log log;


    /**
     * Constructor.
     */
    public PortalGroupServiceImpl() {
        super();
        this.log = LogFactory.getLog(this.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name buildDn(String cn) {
        LdapNameBuilder builder = LdapNameBuilder.newInstance(this.dao.getBaseDn());
        builder.add("cn", cn);
        return builder.build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PortalGroup get(Name dn) {
        PortalGroup group;
        try {
            group = this.dao.find(dn);
        } catch (NameNotFoundException e) {
            group = null;

            this.log.warn("Group with dn " + dn + " not found");
        }

        return group;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PortalGroup getEmpty() {
        return this.applicationContext.getBean(this.sample.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PortalGroup> search(PortalGroup criteria) {
        return this.dao.find(criteria);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PortalGroup create(String cn) {
        PortalGroup portalGroup = this.getEmpty();
        portalGroup.setDn(this.buildDn(cn));
        portalGroup.setCn(cn);

        this.dao.create(portalGroup);

        return portalGroup;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PortalGroup update(PortalGroup portalGroup) {
        // Portal group DN
        Name group = portalGroup.getDn();
        
        // Original portal group
        PortalGroup original = this.get(group);

        if (original != null) {
            // Removed members
            Set<Name> removedMembers;
            if (CollectionUtils.isEmpty(original.getMembers())) {
                removedMembers = new HashSet<>(0);
            } else {
                removedMembers = new HashSet<>(original.getMembers());
            }

            if (CollectionUtils.isNotEmpty(portalGroup.getMembers())) {
                for (Name member : portalGroup.getMembers()) {
                    if (removedMembers.contains(member)) {
                        removedMembers.remove(member);
                    } else {
                        this.addMember(group, member);
                    }
                }
            }
            for (Name member : removedMembers) {
                this.removeMember(group, member);
            }

            this.dao.update(portalGroup);
        }

        return portalGroup;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Name dn) {
        PortalGroup portalGroup = this.get(dn);

        if (CollectionUtils.isNotEmpty(portalGroup.getMembers())) {
            for (Name member : portalGroup.getMembers()) {
                this.removeMember(dn, member);
            }
        }

        this.dao.delete(portalGroup);
    }


    /**
     * Add member to portal group.
     * 
     * @param group portal group DN
     * @param member member DN
     */
    private void addMember(Name group, Name member) {
        Person person = this.personService.getPersonNoCache(member);
        if (!person.getProfiles().contains(group)) {
            person.getProfiles().add(group);
            this.personService.update(person);
        }
    }


    /**
     * Remove member to portal group.
     * 
     * @param group portal group DN
     * @param member member DN
     */
    private void removeMember(Name group, Name member) {
        Person person = this.personService.getPersonNoCache(member);
        if (person.getProfiles().contains(group)) {
            person.getProfiles().remove(group);
            this.personService.update(person);
        }
    }

}
