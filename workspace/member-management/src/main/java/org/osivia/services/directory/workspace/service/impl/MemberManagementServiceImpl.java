package org.osivia.services.directory.workspace.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.directory.workspace.model.AddForm;
import org.osivia.services.directory.workspace.model.Member;
import org.osivia.services.directory.workspace.model.MembersContainer;
import org.osivia.services.directory.workspace.model.Role;
import org.osivia.services.directory.workspace.service.MemberManagementService;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Workspace member management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MemberManagementService
 */
@Service
public class MemberManagementServiceImpl implements MemberManagementService {

    /** Members window property. */
    private static final String MEMBERS_WINDOW_PROPERTY = "directory.workspace.members";


    /** Bundle factory. */
    private final IBundleFactory bundleFactory;
    /** LDAP context. */
    private final InitialLdapContext ldapContext;


    /**
     * Constructor.
     *
     * @throws NamingException
     */
    public MemberManagementServiceImpl() throws NamingException {
        super();

        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());

        // LDAP context
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.SECURITY_AUTHENTICATION, "simple");
        properties.put(Context.PROVIDER_URL, "ldap://localhost:1389");
        properties.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager");
        properties.put(Context.SECURITY_CREDENTIALS, "osivia");
        this.ldapContext = new InitialLdapContext(properties, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MembersContainer getMembersContainer(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        String property = window.getProperty(MEMBERS_WINDOW_PROPERTY);

        // Members container
        MembersContainer container;
        if (property == null) {
            container = new MembersContainer();
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                container = mapper.readValue(property, MembersContainer.class);
            } catch (IOException e) {
                window.setProperty(MEMBERS_WINDOW_PROPERTY, null);
                throw new PortletException(e);
            }
        }

        return container;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PortalControllerContext portalControllerContext, MembersContainer container) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Deleted member
        List<Member> deleted = new ArrayList<Member>();
        for (Member member : container.getMembers()) {
            if (member.isDeleted()) {
                deleted.add(member);
            }
        }
        container.getMembers().removeAll(deleted);

        String property;
        try {
            ObjectMapper mapper = new ObjectMapper();
            property = mapper.writeValueAsString(container);
        } catch (IOException e) {
            throw new PortletException(e);
        }

        window.setProperty(MEMBERS_WINDOW_PROPERTY, property);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray searchMembers(PortalControllerContext portalControllerContext, String filter) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // LDAP filter
        StringBuilder ldapFilter = new StringBuilder();
        ldapFilter.append("(&(objectClass=portalPerson)(|(uid=*");
        if (StringUtils.isNotBlank(filter)) {
            ldapFilter.append(filter);
            ldapFilter.append("*");
        }
        ldapFilter.append(")(displayName=*");
        if (StringUtils.isNotBlank(filter)) {
            ldapFilter.append(filter);
            ldapFilter.append("*");
        }
        ldapFilter.append(")))");


        // Results JSON array
        JSONArray array = new JSONArray();

        try {
            LdapName ldapName = new LdapName("ou=users,dc=osivia,dc=org");
            NamingEnumeration<SearchResult> results = this.ldapContext.search(ldapName, ldapFilter.toString(), null);
            while (results.hasMore()) {
                SearchResult result = results.next();
                Attributes attributes = result.getAttributes();

                JSONObject object = new JSONObject();

                // Identifier
                Attribute uidAttribute = attributes.get("uid");
                String uid = (String) uidAttribute.get();
                object.put("id", uid);

                // Display name
                Attribute displayNameAttribute = attributes.get("displayName");
                if (displayNameAttribute != null) {
                    object.put("displayName", displayNameAttribute.get());
                } else {
                    object.put("displayName", uid);
                }

                // Avatar
                try {
                    Link avatarLink = nuxeoController.getUserAvatar(uid);
                    if (avatarLink != null) {
                        object.put("avatar", avatarLink.getUrl());
                    }
                } catch (CMSException e) {
                    // Do nothing
                }

                // Mail
                Attribute mailAttribute = attributes.get("mail");
                if (mailAttribute != null) {
                    object.put("mail", mailAttribute.get());
                }

                array.add(object);
            }
        } catch (NamingException e) {
            throw new PortletException(e);
        }


        // Create user
        JSONObject create = new JSONObject();
        create.put("id", filter);
        create.put("displayName", bundle.getString("CREATE_NEW_MEMBER", filter));
        create.put("extra", bundle.getString("CREATE_NEW_MEMBER_HELP"));
        create.put("create", true);
        array.add(create);

        return array;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, MembersContainer container, String name) throws PortletException {
        List<Member> members = container.getMembers();
        if (CollectionUtils.isNotEmpty(members)) {
            for (Member member : members) {
                if (StringUtils.equals(member.getName(), name)) {
                    member.setDeleted(true);
                    break;
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void add(PortalControllerContext portalControllerContext, MembersContainer container, AddForm form) throws PortletException {
        if (CollectionUtils.isNotEmpty(form.getNames())) {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            // Members
            List<Member> members = container.getMembers();
            if (members == null) {
                members = new ArrayList<Member>();
                container.setMembers(members);
            }

            Map<String, Member> map = new HashMap<String, Member>(members.size());
            for (Member member : members) {
                map.put(member.getName(), member);
            }

            for (String name : form.getNames()) {
                // Check if current member should be added
                boolean add = true;
                if (map.containsKey(name)) {
                    Role previousRole = map.get(name).getRole();
                    if (form.getRole().getWeight() < previousRole.getWeight()) {
                        add = false;
                    } else {
                        Member member = new Member();
                        member.setName(name);

                        members.remove(member);
                    }
                }

                if (add) {
                    try {
                        // Member
                        Member member = new Member();
                        member.setName(name);
                        member.setRole(form.getRole());

                        // LDAP name
                        LdapName ldapName = new LdapName("ou=users,dc=osivia,dc=org");


                        NamingEnumeration<SearchResult> results = this.ldapContext.search(ldapName, "(&(objectClass=portalPerson)(uid=" + name + "))", null);
                        if (results.hasMore()) {
                            ldapName.add("uid=" + name);

                            // Attributes
                            Attributes attributes = this.ldapContext.getAttributes(ldapName);

                            // Display name
                            Attribute displayNameAttribute = attributes.get("displayName");
                            if (displayNameAttribute != null) {
                                String displayName = (String) displayNameAttribute.get();
                                member.setDisplayName(displayName);
                            } else {
                                member.setDisplayName(name);
                            }

                            // Avatar
                            try {
                                Link avatarLink = nuxeoController.getUserAvatar(name);
                                if (avatarLink != null) {
                                    member.setAvatar(avatarLink.getUrl());
                                }
                            } catch (CMSException e) {
                                // Do nothing
                            }

                            // Mail
                            Attribute mailAttribute = attributes.get("mail");
                            if (mailAttribute != null) {
                                String mail = (String) mailAttribute.get();
                                member.setMail(mail);
                            }
                        } else {
                            // Create user
                            member.setDisplayName(name);

                            // Avatar
                            try {
                                Link avatarLink = nuxeoController.getUserAvatar(name);
                                if (avatarLink != null) {
                                    member.setAvatar(avatarLink.getUrl());
                                }
                            } catch (CMSException e) {
                                // Do nothing
                            }
                        }

                        members.add(member);
                    } catch (NamingException e) {
                        throw new PortletException(e);
                    }
                }
            }

            this.update(portalControllerContext, container);
        }
    }

}
