package org.osivia.services.directory.workspace.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.directory.workspace.model.LocalGroup;
import org.osivia.services.directory.workspace.model.LocalGroups;
import org.osivia.services.directory.workspace.model.Member;
import org.osivia.services.directory.workspace.repository.LocalGroupManagementRepository;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Workspace local group management repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see LocalGroupManagementRepository
 */
@Repository
public class LocalGroupManagementRepositoryImpl implements LocalGroupManagementRepository {

    /** Local groups window property. */
    private static final String LOCAL_GROUPS_WINDOW_PROPERTY = "directory.workspace.localGroups";


    /** LDAP context. */
    private final InitialLdapContext ldapContext;


    /**
     * Constructor.
     *
     * @throws NamingException
     */
    public LocalGroupManagementRepositoryImpl() throws NamingException {
        super();

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
    public LocalGroups getLocalGroups(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        String property = window.getProperty(LOCAL_GROUPS_WINDOW_PROPERTY);

        // Local groups
        LocalGroups localGroups;
        if (property == null) {
            localGroups = new LocalGroups();
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                localGroups = mapper.readValue(property, LocalGroups.class);
            } catch (IOException e) {
                window.setProperty(LOCAL_GROUPS_WINDOW_PROPERTY, null);
                throw new PortletException(e);
            }
        }

        return localGroups;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Deleted local groups
        List<LocalGroup> deleted = new ArrayList<LocalGroup>();
        for (LocalGroup group : localGroups.getGroups()) {
            if (group.isDeleted()) {
                deleted.add(group);
            }
        }
        localGroups.getGroups().removeAll(deleted);

        String property;
        try {
            ObjectMapper mapper = new ObjectMapper();
            property = mapper.writeValueAsString(localGroups);
        } catch (IOException e) {
            throw new PortletException(e);
        }

        window.setProperty(LOCAL_GROUPS_WINDOW_PROPERTY, property);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroup getLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException {
        LocalGroup localGroup = null;

        LocalGroups localGroups = this.getLocalGroups(portalControllerContext);
        if ((localGroups != null) && CollectionUtils.isNotEmpty(localGroups.getGroups())) {
            for (LocalGroup group : localGroups.getGroups()) {
                if (StringUtils.equals(id, group.getId())) {
                    localGroup = group;
                    break;
                }
            }
        }

        return localGroup;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalGroup(PortalControllerContext portalControllerContext, LocalGroup localGroup) throws PortletException {
        LocalGroups localGroups = this.getLocalGroups(portalControllerContext);
        if (localGroups == null) {
            localGroups = new LocalGroups();
        }
        List<LocalGroup> groups = localGroups.getGroups();
        if (groups == null) {
            groups = new ArrayList<LocalGroup>();
            localGroups.setGroups(groups);
        }

        groups.remove(localGroup);
        groups.add(localGroup);

        this.setLocalGroups(portalControllerContext, localGroups);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException {
        LocalGroups localGroups = this.getLocalGroups(portalControllerContext);
        if ((localGroups != null) && CollectionUtils.isNotEmpty(localGroups.getGroups())) {
            LocalGroup localGroup = new LocalGroup();
            localGroup.setId(id);

            localGroups.getGroups().remove(localGroup);

            this.setLocalGroups(portalControllerContext, localGroups);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Create new local group
        LocalGroup localGroup = new LocalGroup();
        localGroup.setId(UUID.randomUUID().toString());
        localGroup.setDisplayName(form.getDisplayName());

        // Update local groups
        List<LocalGroup> groups = localGroups.getGroups();
        if (groups == null) {
            groups = new ArrayList<LocalGroup>();
            localGroups.setGroups(groups);
        }
        groups.add(localGroup);

        String property;
        try {
            ObjectMapper mapper = new ObjectMapper();
            property = mapper.writeValueAsString(localGroups);
        } catch (IOException e) {
            throw new PortletException(e);
        }

        window.setProperty(LOCAL_GROUPS_WINDOW_PROPERTY, property);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Member getMember(PortalControllerContext portalControllerContext, String id) throws PortletException {
        try {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            Member member = new Member();
            member.setId(id);

            // LDAP name
            LdapName ldapName = new LdapName("ou=users,dc=osivia,dc=org");
            ldapName.add("uid=" + id);

            // Attributes
            Attributes attributes = this.ldapContext.getAttributes(ldapName);

            // Display name
            Attribute displayNameAttribute = attributes.get("displayName");
            if (displayNameAttribute != null) {
                String displayName = (String) displayNameAttribute.get();
                member.setDisplayName(displayName);
            } else {
                member.setDisplayName(id);
            }

            // Avatar
            try {
                Link avatarLink = nuxeoController.getUserAvatar(id);
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

            return member;
        } catch (NamingException e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Member> getAllMembers(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        List<Member> members;

        try {
            LdapName ldapName = new LdapName("ou=users,dc=osivia,dc=org");
            NamingEnumeration<SearchResult> results = this.ldapContext.search(ldapName, "(objectClass=portalPerson)", null);

            members = new ArrayList<Member>();
            while (results.hasMore()) {
                SearchResult result = results.next();
                Attributes attributes = result.getAttributes();

                Member member = new Member();

                // Identifier
                Attribute uidAttribute = attributes.get("uid");
                String uid = (String) uidAttribute.get();
                member.setId(uid);

                // Display name
                Attribute displayNameAttribute = attributes.get("displayName");
                String displayName;
                if (displayNameAttribute != null) {
                    displayName = (String) displayNameAttribute.get();
                } else {
                    displayName = uid;
                }
                member.setDisplayName(displayName);

                // Avatar
                try {
                    Link avatarLink = nuxeoController.getUserAvatar(uid);
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

                members.add(member);
            }
        } catch (NamingException e) {
            throw new PortletException(e);
        }

        return members;
    }

}
