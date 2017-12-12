package org.osivia.services.group.card.portlet.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.osivia.services.group.card.portlet.model.GroupCardSettings;
import org.osivia.services.group.card.portlet.model.GroupEditionForm;
import org.osivia.services.group.card.portlet.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Group card portlet service implementation.
 * 
 * @see GroupCardService
 */
@Service
public class GroupCardServiceImpl implements GroupCardService {

    private static final String ROLE_ADMIN = "role_admin";

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


    /**
     * Constructor.
     */
    public GroupCardServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String doView(PortalControllerContext portalControllerContext, GroupCardOptions options) throws PortletException {
//        // Portlet request
//        PortletRequest request = portalControllerContext.getRequest();
//
//        // Portlet settings
//        GroupCardSettings settings = this.getSettings(portalControllerContext);
//
//        if (settings.isStub()) {
//            request.setAttribute("osivia.emptyResponse", "1");
//        }

        // View path
        String path;
        if (options.getGroup() == null) {
            path = "deleted";
        } else {
            path = "view";
        }

        return path;
    }


    /**
     * {@inheritDoc}
     */
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
            options.setDn(dn);
            options.setGroup(portalGroup);


            // Editable group card indicator
            boolean editable = this.isEditable(portalControllerContext);
            options.setEditable(editable);
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

            List<Member> members = buildMemberList(portalGroup.getMembers());
            group.setMembers(members);

            card.setGroup(group);
        }

        return card;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject searchPersons(PortalControllerContext portalControllerContext, GroupCardOptions options, GroupEditionForm form, String filter) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Member identifiers
        // Member identifiers
        Map<String, Member> memberIdentifiers = new HashMap<String, Member>();
        for (Member member : form.getMembers()) {
            memberIdentifiers.put(member.getId(),member);
        }

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
                boolean alreadyMember = memberIdentifiers.containsKey(person.getUid());
                boolean added = (alreadyMember? memberIdentifiers.get(person.getUid()).isAdded() : false); 
                // Search result
                JSONObject object = this.getSearchResult(person, alreadyMember, added, bundle);

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
    protected JSONObject getSearchResult(Person person, boolean alreadyMember, boolean added, Bundle bundle) {
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
            displayName += " " + (added ? bundle.getString("GROUP_CARD_ALREADY_ADDED_INDICATOR") : bundle.getString("GROUP_CARD_ALREADY_MEMBER_INDICATOR"));
            object.put("disabled", true);
        }

        object.put("displayName", displayName);
        object.put("extra", extra);

        object.put("avatar", person.getAvatar().getUrl());
        object.put("uid", person.getUid());

        return object;
    }

    /**
     * Update member list removing member that where removed before searching for other members
     * 
     * @param form
     * @return
     */
    private List<Member> getMemberListToSave(GroupEditionForm form)
    {
        List<Member> membersToSave = new ArrayList<>();
        for(Member member : form.getMembers())
        {
            if (!member.isDeleted()) membersToSave.add(member);
        }
        return membersToSave;
    }

    /**
     * {@inheritDoc}
     */
    public void updateMemberList(GroupEditionForm form)
    {
        List<Member> newList = new ArrayList<>();
        for(Member member : form.getMembers())
        {
            if (!member.isAdded() || !member.isDeleted()) newList.add(member);
        }
        form.setMembers(newList);
    }

    public void addMember(PortalControllerContext portalControllerContext, GroupEditionForm form, PortalGroup portalGroup) throws PortletException
    {

        String uid = form.getAddedMember().getId();
        if (!StringUtils.isEmpty(uid))
        {
            Person person = this.personService.getPerson(uid);
            Member member = new Member(person);
            member.setExtra(person.getMail());
            member.setIndex((form.getMembers() != null)? Integer.toString(form.getMembers().size()) : "0");
            member.setAdded(true);
            form.getMembers().add(member);
            //Sort list
            Collections.sort(form.getMembers());
            this.getEditionForm(portalControllerContext).setMembers(form.getMembers());;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGroup(PortalControllerContext portalControllerContext, GroupCardOptions options) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if (options.isEditable()) {
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

                List<Member> membersToSave = this.getMemberListToSave(form);
                // LDAP properties
                this.setLdapProperties(form, group, membersToSave);

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
        PortalGroup portalGroup = this.service.get(options.getDn());
        options.setGroup(portalGroup);

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
            editable = this.roleService.hasRole(dn, ROLE_ADMIN);
        }
        return editable;
    }

    /**
     * Set LDAP properties.
     *
     * @param form person edition form
     * @param person person
     * @throws PortletException
     */
    protected void setLdapProperties(GroupEditionForm form, PortalGroup group, List<Member> membersToSave) throws PortletException {
        group.setDisplayName(StringUtils.trimToNull(form.getDisplayName()));
        group.setDescription(StringUtils.trimToNull(form.getDescription()));
        List<Name> listName = new ArrayList<>();
        for (Member member : membersToSave)
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

        List<Member> members = buildMemberList(group.getMembers());
        form.setMembers(members);

        // Member identifiers
        Set<String> identifiers = new HashSet<>();
        for (Member member : members) {
            identifiers.add(member.getId());
        }


    }

    /**
     * Build member list and sort it
     * @param names
     * @return
     */
    private List<Member> buildMemberList(List<Name> names)
    {
        ArrayList<Member> members = new ArrayList<>();
        int i = 0;
        for (Name name : names)
        {
            Person person = this.personService.getPerson(name);
            Member member = new Member(person);
            member.setExtra(person.getMail());
            member.setIndex(Integer.toString(i));
            i++;
            members.add(member);
        }
        Collections.sort(members);;
        return members;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public GroupCardSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException {
//        // Window
//        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Settings
        GroupCardSettings settings = this.applicationContext.getBean(GroupCardSettings.class);

//        // Resource loader stub indicator
//        boolean stub = BooleanUtils.toBoolean(window.getProperty(STUB_WINDOW_PROPERTY));
//        settings.setStub(stub);

        return settings;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings(PortalControllerContext portalControllerContext, GroupCardSettings settings) {
//        // Window
//        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
//
//        // Resource loader stub indicator
//        window.setProperty(STUB_WINDOW_PROPERTY, String.valueOf(settings.isStub()));
    }

}
