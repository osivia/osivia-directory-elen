package org.osivia.services.directory.workspace.portlet.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.directory.workspace.portlet.model.AddForm;
import org.osivia.services.directory.workspace.portlet.model.MembersContainer;
import org.osivia.services.directory.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    
    @Autowired
    private PersonService personService;
    
    @Autowired
    private WorkspaceService workspaceService;

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
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MembersContainer getMembersContainer(PortalControllerContext portalControllerContext, String workspaceid) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        String property = window.getProperty(MEMBERS_WINDOW_PROPERTY);

        // Members container
        MembersContainer container;
        if (property == null) {
            container = new MembersContainer(workspaceid);
            
            List<WorkspaceMember> allMembers = workspaceService.getAllMembers(workspaceid);
            container.setMembers(allMembers);
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().set(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS , false);
            String membersAsString;
			try {
				membersAsString = mapper.writeValueAsString(container);
			} catch (JsonGenerationException e) {
				throw new PortletException(e);
			} catch (JsonMappingException e) {
				throw new PortletException(e);
			} catch (IOException e) {
				throw new PortletException(e);
			}
            
            window.setProperty(MEMBERS_WINDOW_PROPERTY, membersAsString);
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
        List<WorkspaceMember> deleted = new ArrayList<WorkspaceMember>();
        for (WorkspaceMember member : container.getMembers()) {
            if (member.isDeleted()) {
                deleted.add(member);
                workspaceService.removeMember(container.getWorkspaceId(), member.getMember().getDn());
            }
            else {
            	workspaceService.addOrModifyMember(container.getWorkspaceId(), member.getMember().getDn(), member.getRole());
            }
        }
        container.getMembers().removeAll(deleted);

        String property;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().set(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS , false);
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

        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        Person search = personService.getEmptyPerson();
        search.setUid(filter + "*");
        search.setDisplayName(filter + "*");
        search.setSn(filter + "*");
        search.setGivenName(filter + "*");
        List<Person> persons = personService.findByCriteria(search);


        // Results JSON array
        JSONArray array = new JSONArray();

        for(Person p : persons) {
        	JSONObject object = new JSONObject();
        	object.put("id", p.getUid());
        	object.put("displayName", p.getDisplayName());
        	object.put("mail", p.getMail());
        	object.put("avatar", p.getAvatar().getUrl());
        	
        	array.add(object);
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
        List<WorkspaceMember> members = container.getMembers();
        if (CollectionUtils.isNotEmpty(members)) {
            for (WorkspaceMember member : members) {
                if (StringUtils.equals(member.getMember().getUid(), name)) {
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

           
            for (String name : form.getNames()) {
            	                    	
            	Person person = personService.getPerson(name);
            	
            	if(person == null) {
            		person = personService.getEmptyPerson();
            		person.setUid(name);
            		person.setSn(name);
            		person.setCn(name);
            		person.setDisplayName(name);
            		person.setMail(name);
            		personService.create(person);
            	}
            	
            	WorkspaceMember newMember = workspaceService.addOrModifyMember(container.getWorkspaceId(), person.getDn(), form.getRole() );
            	
            	boolean add = true;
            	
            	// Synchronisation avec la liste en session
            	for(WorkspaceMember currentMember : container.getMembers()) {
            		if(currentMember.getMember().getUid().equals(newMember.getMember().getUid())) {
            			// Si changement de rôle, on retire le membre pour le rajouter avec son nouveau rôle
            			currentMember.setRole(form.getRole());
            			add = false;
            		}
            	}
            	if(add) {
            		container.getMembers().add(newMember);
            	}
            }

            this.update(portalControllerContext, container);
        }
    }


	/* (non-Javadoc)
	 * @see org.osivia.services.directory.workspace.portlet.service.MemberManagementService#getAllowedRoles(java.lang.String)
	 */
	@Override
	public List<WorkspaceRole> getAllowedRoles(String workspaceId) {
		
		List<WorkspaceRole> roles = new ArrayList<WorkspaceRole>();
			
		List<CollabProfile> profiles = workspaceService.findByWorkspaceId(workspaceId);
		for(CollabProfile cp : profiles) {
			if(cp.getType() == WorkspaceGroupType.security_group) {
				roles.add(cp.getRole());
			}
		}
		
		return roles;
	}

}
