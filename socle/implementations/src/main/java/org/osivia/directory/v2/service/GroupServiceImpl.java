package org.osivia.directory.v2.service;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.directory.v2.dao.GroupDao;
import org.osivia.portal.api.directory.v2.model.Group;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.GroupService;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Group service implementation
 * 
 * @author Cédric Krommenhoek
 * @see LdapServiceImpl
 * @see GroupService
 */
@Service
@Primary
public class GroupServiceImpl extends LdapServiceImpl implements GroupService {

    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;

    /** Group DAO. */
    @Autowired
    private GroupDao dao;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /**
     * Constructor.
     */
    public GroupServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public Group getEmptyGroup() {
        return this.applicationContext.getBean(Group.class);
    }


    /**
     * {@inheritDoc}
     */
    public Group get(Name dn) {
        return this.dao.get(dn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Group get(String cn) {
        // Search criteria
        Group criteria = this.applicationContext.getBean(Group.class);
        criteria.setCn(cn);

        // Search results
        List<Group> results = this.dao.find(criteria);

        // LDAP group
        Group group;
        if ((results != null) && (results.size() == 1)) {
            group = results.get(0);
        } else {
            group = null;
        }

        return group;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> getMembers(Name dn) {
        // LDAP group
        Group group = this.dao.get(dn);

        // Group member names
        List<Name> names = group.getMembers();

        // Group member persons
        List<Person> persons;

        if (CollectionUtils.isEmpty(names)) {
            persons = new ArrayList<Person>(0);
        } else {
            persons = new ArrayList<Person>(names.size());

            for (Name name : names) {
                Person person = this.personService.getPerson(name);
                if (person != null) {
                    persons.add(person);
                }
            }
        }

        return persons;
    }


    /**
     * {@inheritDoc}
     */
    public List<Group> search(Group criteria) {
        return this.dao.find(criteria);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Name dn) {
        this.dao.delete(dn);
    }

}
