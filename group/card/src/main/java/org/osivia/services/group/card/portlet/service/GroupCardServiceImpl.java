package org.osivia.services.group.card.portlet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Name;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.directory.v2.service.PortalGroupService;
import org.osivia.directory.v2.service.RoleService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.group.card.portlet.model.Group;
import org.osivia.services.group.card.portlet.model.GroupCard;
import org.osivia.services.group.card.portlet.model.GroupCardOptions;
import org.osivia.services.group.card.portlet.model.GroupEditionForm;
import org.osivia.services.group.card.portlet.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class GroupCardServiceImpl implements GroupCardService {

    /** Administrator role system property. */
    private static final String ADMINISTRATOR_ROLE_PROPERTY = "groupcard.roles.roleAdministrator";

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    PortalGroupService service;

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Role service. */
    @Autowired
    private RoleService roleService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;

    /** Administrator role. */
    private final String administratorRole;

    public GroupCardServiceImpl() {
        super();

        // Administrator role
        this.administratorRole = System.getProperty(ADMINISTRATOR_ROLE_PROPERTY);
    }

    @Override
    public GroupCardOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        GroupCardOptions options = this.applicationContext.getBean(GroupCardOptions.class);

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // User connected UID
        String cn = window.getProperty(GROUP_CN_WINDOW_PROPERTY);

        if (!StringUtils.isEmpty(cn))
        {
            options.setCn(cn);

            Name dn = this.service.buildDn(cn);

            //            // PortalGroup
            PortalGroup portalGroup = this.service.get(dn);
            options.setGroup(portalGroup);


            // Editable group card indicator
            boolean editable = this.isEditable(portalControllerContext);
            options.setEditable(editable);

            // Deletable group indicator
            boolean deletable = this.isDeletable(portalControllerContext);
            options.setDeletable(deletable);

        }
        return options;
    }

    @Override
    public GroupCard getGroupCard(PortalControllerContext portalControllerContext) throws PortletException {
        // Group card options
        GroupCardOptions options = this.getOptions(portalControllerContext);

        // portalGroup
        //PortalGroup portalGroup = this.service.get(options.getDn());
        PortalGroup portalGroup = options.getGroup();

        // portalGroup card
        GroupCard card;
        if (portalGroup == null) {
            card = null;
        } else {
            card = this.applicationContext.getBean(GroupCard.class);

            Group group = new Group();
            group.setDisplayName(portalGroup.getDisplayName());
            group.setDescription(portalGroup.getDescription());

            List<Member> members = setMemberList(portalGroup.getMembers());
            group.setMembers(members);
            //            //TODO pour tests
            //            Member member1 = new Member();
            //            member1.setDisplayName("Personne 1");
            //            Member member2 = new Member();
            //            member2.setDisplayName("Personne 2");
            //            ArrayList<Member> memberList = new ArrayList<>();
            //            memberList.add(member1);
            //            memberList.add(member2);
            //            group.setMembers(memberList);

            card.setGroup(group);
        }

        return card;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject searchPersons(PortalControllerContext portalControllerContext, GroupCardOptions options, String filter) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Member identifiers
        Set<String> memberIdentifiers = this.getEditionForm(portalControllerContext).getMemberIdentifiers();

        // JSON objects
        List<JSONObject> objects = new ArrayList<>();
        // Total results
        int total = 0;

        String[] parts;
        if (StringUtils.isBlank(filter)) {
            parts = new String[]{StringUtils.EMPTY};
        } else {
            parts = StringUtils.split(filter, ",;");
        }
        for (String part : parts) {
            // Persons
            Person pCriteria = getSearchCriteria(portalControllerContext, part);

            List<Person> persons = this.personService.findByCriteria(pCriteria);
            for (Person person : persons) {
                // Already member indicator
                boolean alreadyMember = memberIdentifiers.contains(person.getUid());
                // Search result
                JSONObject object = this.getSearchResult(person, alreadyMember, bundle);

                objects.add(object);
                total++;
            }
        }

        // Results JSON object
        JSONObject results = new JSONObject();
        // Items JSON array
        JSONArray items = new JSONArray();
        items.addAll(objects);
        results.put("items", items);
        results.put("total", total);

        return results;
    }

    /**
     * Get search criteria.
     *
     * @param portalControllerContext portal controller context
     * @param filters search filters
     * @return search criteria
     */
    private Person getSearchCriteria(PortalControllerContext portalControllerContext, String filter) {
        // Criteria
        Person criteria = this.personService.getEmptyPerson();

        if (StringUtils.isNotBlank(filter)) {
            // Stripped filter
            String strippedFilter = StringUtils.strip(StringUtils.trim(filter), "*");
            // Tokenized filter
            String tokenizedFilter = strippedFilter + "*";
            // Tokenized filter substring
            String tokenizedFilterSubstring = "*" + tokenizedFilter;

            criteria.setUid(tokenizedFilter);
            criteria.setSn(tokenizedFilter);
            criteria.setGivenName(tokenizedFilter);
            criteria.setMail(tokenizedFilter);
            criteria.setDisplayName(tokenizedFilterSubstring);
        }

        return criteria;
    }

    /**
     * Get search result JSON Object.
     * 
     * @param person person
     * @param alreadyMember already member indicator
     * @param existingRequest existing request indicator
     * @param bundle internationalization bundle
     * @return JSON object
     */
    protected JSONObject getSearchResult(Person person, boolean alreadyMember, Bundle bundle) {
        JSONObject object = new JSONObject();
        object.put("id", person.getUid());

        // Display name
        String displayName;
        // Extra
        String extra;

        if (StringUtils.isEmpty(person.getDisplayName())) {
            displayName = person.getUid();
            extra = null;
        } else {
            displayName = person.getDisplayName();

            extra = person.getUid();
            if (StringUtils.isNotBlank(person.getMail()) && !StringUtils.equals(person.getUid(), person.getMail())) {
                extra += " – " + person.getMail();
            }
        }

        if (alreadyMember) {
            displayName += " " + bundle.getString("GROUP_CARD_ALREADY_MEMBER_INDICATOR");
            object.put("disabled", true);
        }

        object.put("displayName", displayName);
        object.put("extra", extra);

        object.put("avatar", person.getAvatar().getUrl());
        object.put("uid", person.getUid());

        return object;
    }
    
    public void updateMemberList(GroupEditionForm form)
    {
        String[] parts;
        if (StringUtils.isBlank(form.getListMemberToDelete())) {
            parts = new String[]{};
        } else {
            parts = StringUtils.split(form.getListMemberToDelete(), ",");
        }
        for (String part : parts) {
            form.getMembers().remove(Integer.parseInt(part));
        }
        form.setListMemberToDelete("");
    }
    
    public void addMember(PortalControllerContext portalControllerContext, GroupEditionForm form, PortalGroup portalGroup)
    {
        if (form.getAddedMember().size() ==1)
        {
            
            String uid = form.getAddedMember().get(0).getId();
            if (!StringUtils.isEmpty(uid))
            {
                Person person = this.personService.getPerson(uid);
                Member member = new Member(person);
                member.setExtra(person.getMail());
                form.getMembers().add(member);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGroup(PortalControllerContext portalControllerContext, GroupCardOptions options) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if (options.isDeletable()) {
            // Group
            PortalGroup group = options.getGroup();

            if (group != null) {
                this.service.delete(group.getDn());

                // Update options
                options.setGroup(null);

                // Notification
                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("GROUP_CARD_DELETE_SUCCESS"),
                        NotificationsType.SUCCESS);
            }
        } else {
            // Forbidden
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("GROUP_CARD_DELETE_FORBIDDEN"), NotificationsType.ERROR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveGroup(PortalControllerContext portalControllerContext, GroupCardOptions options, GroupEditionForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if (options.isEditable()) {
            // portalGroup
            PortalGroup group = options.getGroup();

            if (group != null) {
                
                this.updateMemberList(form);
                // LDAP properties
                this.setLdapProperties(form, group);

                //TODO commenté pour tests
                // Update group
                this.service.update(group);

                // Notification
                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("GROUP_CARD_EDITION_SUCCESS"),
                        NotificationsType.SUCCESS);
            }
        } else {
            // Forbidden
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("GROUP_CARD_EDITION_FORBIDDEN"),
                    NotificationsType.ERROR);
        }
    }

    /* (non-Javadoc)
     * @see org.osivia.services.group.card.portlet.service.GroupCardService#getEditionForm(org.osivia.portal.api.context.PortalControllerContext)
     */
    @Override
    public GroupEditionForm getEditionForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Group card options
        GroupCardOptions options = this.getOptions(portalControllerContext);

        // Edition form
        GroupEditionForm form = this.applicationContext.getBean(GroupEditionForm.class);

        // Group
        PortalGroup portalGroup = options.getGroup();
        //TODO à décommenter une fois les implémentations faite
        //        PortalGroup group = this.service.get(options.getDn());

        this.fillForm(form, portalGroup);


        return form;
    }

    /**
     * Check if the group card can be edited by the current user.
     *
     * @param portalControllerContext portal controller context
     * @return true if the portalGroup card is editable
     */
    protected boolean isEditable(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Current user UID
        String uid = request.getRemoteUser();

        // Editable group indicator
        boolean editable;
        if (StringUtils.isEmpty(uid)) {
            editable = false;
        } else
        {
            // Current user DN
            Name dn = this.personService.getEmptyPerson().buildDn(uid);

            // Check if the current user has administration role
            //            editable =  StringUtils.isNotEmpty(this.administratorRole) && this.roleService.hasRole(dn, this.administratorRole);
            editable = true;
        }
        return editable;
    }

    /**
     * Check if the group can be deleted by the current user.
     *
     * @param portalControllerContext portal controller context
     * @return true if the portalGroup can be deleted
     */
    protected boolean isDeletable(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current user UID
        String uid = request.getRemoteUser();

        // Deletable person indicator
        boolean deletable;

        if (StringUtils.isEmpty(uid)) {
            deletable = false;
        } else {
            // Current user DN
            Name dn = this.personService.getEmptyPerson().buildDn(uid);

            // Check if the current user has administration role
            //            deletable = StringUtils.isNotEmpty(this.administratorRole) && this.roleService.hasRole(dn, this.administratorRole);
            deletable = true;
        }

        return deletable;
    }

    /**
     * Set LDAP properties.
     *
     * @param form person edition form
     * @param person person
     * @throws PortletException
     */
    protected void setLdapProperties(GroupEditionForm form, PortalGroup group) throws PortletException {
        group.setDisplayName(StringUtils.trimToNull(form.getDisplayName()));
        group.setDescription(StringUtils.trim(form.getDescription()));
        List<Name> listName = new ArrayList<>();
        for (Member member : form.getMembers())
        {
            listName.add(member.getDn());
        }
        group.setMembers(listName);
    }

    /**
     * Fill LDAP properties.
     *
     * @param form group edition form
     * @param group group
     * @throws PortletException
     */
    protected void fillForm(GroupEditionForm form, PortalGroup group) throws PortletException {

        // Display name
        String displayName = group.getDisplayName();
        form.setDisplayName(displayName);

        // Last name
        String description = group.getDescription();
        form.setDescription(description);

        List<Member> members = setMemberList(group.getMembers());
        form.setMembers(members);

        // Member identifiers
        Set<String> identifiers = new HashSet<>();
        for (Member member : members) {
            identifiers.add(member.getId());
        }
        form.setMemberIdentifiers(identifiers);


    }

    private List<Member> setMemberList(List<Name> names)
    {
        ArrayList<Member> members = new ArrayList<>();
        for (Name name : names)
        {
            Person person = this.personService.getPerson(name);
            Member member = new Member(person);
            member.setExtra(person.getMail());
            members.add(member);
        }
        return members;
    }

}
