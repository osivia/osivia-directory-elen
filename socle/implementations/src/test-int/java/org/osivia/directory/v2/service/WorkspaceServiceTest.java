/**
 * 
 */
package org.osivia.directory.v2.service;

import java.util.List;

import javax.naming.Name;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author Loïc Billon

 */
@ContextConfiguration("/testApplicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkspaceServiceTest extends AbstractJUnit4SpringContextTests {


	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private PersonService persService;
	
	@Autowired
	private WorkspaceService service;
	
	private String workspaceId;

	
	public static class WorkspaceNameGenerator {
		
		public static WorkspaceNameGenerator instance;
		
		public final String id;
		
		public static WorkspaceNameGenerator getInstance() {
			if(instance == null) {
				instance = new WorkspaceNameGenerator();
			}
			return instance;
			
			
		}
		
		private WorkspaceNameGenerator() {
			id = "ws"+RandomUtils.nextInt();
		}
		
	}
	
	@Before
	public void setUp() {
		
		System.setProperty("ldap.base", "dc=osivia,dc=org");
		
		workspaceId = WorkspaceNameGenerator.getInstance().id;
	}
	
	@Test
	public void test01Creation() {
		

		Person owner = persService.getPerson("gracicot");
		
		service.create(workspaceId, owner);
		
		CollabProfile searchProfile = context.getBean(CollabProfile.class);
		searchProfile.setWorkspaceId(workspaceId);
		List<CollabProfile> profiles = service.findByCriteria(searchProfile);
		
		System.out.println("=== testCreation ===");
		for(CollabProfile cp : profiles) {
			System.out.println(cp.getDn());
		}
		
		for(WorkspaceMember wm : service.getAllMembers(workspaceId)) {
			
			System.out.println(wm.getMember().getDisplayName() + " " + wm.getRole() + " " + wm.getLocalGroups());
		}
	}
	
	
	
	@Test
	public void test02AddMembers() {
		
		Person editor1 = persService.getPerson("hfortier");
		
		service.addOrModifyMember(workspaceId, editor1.getDn(), WorkspaceRole.WRITER);
		
		Person editor2 = persService.getPerson("jbeaudoin");
		
		service.addOrModifyMember(workspaceId, editor2.getDn(), WorkspaceRole.WRITER);
		
		Person reader = persService.getPerson("jcharbonneau");
		
		service.addOrModifyMember(workspaceId, reader.getDn(), WorkspaceRole.READER);
		
		System.out.println("=== testAddMembers ===");
		for(WorkspaceMember wm : service.getAllMembers(workspaceId)) {
			
			System.out.println(wm.getMember().getDisplayName() + " " + wm.getRole() + " " + wm.getLocalGroups());
		}
	}
	
	@Test
	public void test03ModifyMembers() {
		
		Person editorToreader = persService.getPerson("hfortier");
		
		service.addOrModifyMember(workspaceId, editorToreader.getDn(), WorkspaceRole.READER);
		
		Person editorToContributor = persService.getPerson("jbeaudoin");
		
		service.addOrModifyMember(workspaceId, editorToContributor.getDn(), WorkspaceRole.CONTRIBUTOR);
		
		Person readerToContributor = persService.getPerson("jcharbonneau");
		
		service.addOrModifyMember(workspaceId, readerToContributor.getDn(), WorkspaceRole.CONTRIBUTOR);
		

		System.out.println("=== testModifyMembers ===");
		for(WorkspaceMember wm : service.getAllMembers(workspaceId)) {
			
			System.out.println(wm.getMember().getDisplayName() + " " + wm.getRole() + " " + wm.getLocalGroups());
		}
	}
	
	
	@Test
	public void test04RemoveMembers() {
		
		Person reader = persService.getPerson("hfortier");
		
		service.removeMember(workspaceId, reader.getDn());
		
		Person contributor = persService.getPerson("jbeaudoin");
		
		service.removeMember(workspaceId, contributor.getDn());
		
		System.out.println("=== testRemoveMembers ===");
		for(WorkspaceMember wm : service.getAllMembers(workspaceId)) {
			
			System.out.println(wm.getMember().getDisplayName() + " " + wm.getRole() + " " + wm.getLocalGroups());
		}
	}
	
	@Test
	public void test05CreateLocalGroup() {
		
		service.createLocalGroup(workspaceId, "Groupe 1", "Mon groupe local 1");
		
		service.createLocalGroup(workspaceId, "Groupe 2", "Mon groupe local 2");
		
		CollabProfile searchProfile = context.getBean(CollabProfile.class);
		searchProfile.setWorkspaceId(workspaceId);
		List<CollabProfile> profiles = service.findByCriteria(searchProfile);
		System.out.println("=== testCreateLocalGroup ===");
		for(CollabProfile cp : profiles) {
			System.out.println(cp.getDn());
		}
	}
	
	@Test
	public void test06AddMemberToLocalGroup() {
		
		Person localPerson = persService.getPerson("mlaux");
		Person localPerson2 = persService.getPerson("hfortier");
		
		service.addOrModifyMember(workspaceId, localPerson.getDn(), WorkspaceRole.READER);
		
		CollabProfile sample = context.getBean(CollabProfile.class);
		Name localGroupDn = sample.buildDn(workspaceId + "_1");
		
		service.addMemberToLocalGroup(workspaceId, localGroupDn, localPerson.getDn());
		service.addMemberToLocalGroup(workspaceId, localGroupDn, localPerson2.getDn());
		
		Name localGroup2Dn = sample.buildDn(workspaceId + "_2");
		
		service.addMemberToLocalGroup(workspaceId, localGroup2Dn, localPerson.getDn());
		
		System.out.println("=== testAddMemberToLocalGroup ===");
		for(WorkspaceMember wm : service.getAllMembers(workspaceId)) {
			
			System.out.println(wm.getMember().getDisplayName() + " " + wm.getRole() + " " + wm.getLocalGroups());
		}
	}
	
	@Test
	public void test07RemoveMemberFromLocalGroup() {
		
		Person localPerson = persService.getPerson("mlaux");
		
		CollabProfile sample = context.getBean(CollabProfile.class);
		Name localGroupDn = sample.buildDn(workspaceId + "_1");
		
		service.removeMemberFromLocalGroup(workspaceId, localGroupDn, localPerson.getDn());
		
		
		System.out.println("=== testRemoveMemberFromLocalGroup ===");
		for(WorkspaceMember wm : service.getAllMembers(workspaceId)) {
			
			System.out.println(wm.getMember().getDisplayName() + " " + wm.getRole() + " " + wm.getLocalGroups());
		}
	}
	
	@Test
	public void test08RemoveLocalGroup() {
		
		CollabProfile sample = context.getBean(CollabProfile.class);
		Name localGroupDn = sample.buildDn(workspaceId + "_2");
		
		service.removeLocalGroup(workspaceId, localGroupDn);
		
		CollabProfile searchProfile = context.getBean(CollabProfile.class);
		searchProfile.setWorkspaceId(workspaceId);
		List<CollabProfile> profiles = service.findByCriteria(searchProfile);
		System.out.println("=== testRemoveLocalGroup ===");
		for(CollabProfile cp : profiles) {
			System.out.println(cp.getDn());
		}
		
	}
	
	@Test
	public void test09Delete() {
		
		service.delete(workspaceId);
		
		CollabProfile searchProfile = context.getBean(CollabProfile.class);
		searchProfile.setWorkspaceId(workspaceId);
		List<CollabProfile> profiles = service.findByCriteria(searchProfile);
		System.out.println("=== testDelete ===");
		for(CollabProfile cp : profiles) {
			System.out.println(cp.getDn());
		}
	}
}
