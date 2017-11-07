package org.osivia.services.directory.various.portlet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.various.portlet.model.DuplicatedGroups;
import org.osivia.services.directory.various.portlet.model.DuplicatedGroupsKey;
import org.osivia.services.directory.various.portlet.model.comparator.DuplicatedGroupsKeyComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Duplicated groups detector portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see DuplicatedGroupsDetectorService
 */
@Service
public class DuplicatedGroupsDetectorServiceImpl implements DuplicatedGroupsDetectorService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Comparator. */
    @Autowired
    private DuplicatedGroupsKeyComparator comparator;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;


    /**
     * Constructor.
     */
    public DuplicatedGroupsDetectorServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DuplicatedGroups getDuplicatedGroups(PortalControllerContext portalControllerContext) throws PortletException {
        // Profiles search criteria
        CollabProfile criteria = this.workspaceService.getEmptyProfile();
        criteria.setType(WorkspaceGroupType.local_group);

        // Profiles
        List<CollabProfile> profiles = this.workspaceService.findByCriteria(criteria);

        // Duplicated groups
        DuplicatedGroups duplicatedGroups = this.applicationContext.getBean(DuplicatedGroups.class, this.comparator);
        Map<DuplicatedGroupsKey, List<CollabProfile>> map = duplicatedGroups.getMap();

        for (CollabProfile profile : profiles) {
            String workspaceId = profile.getWorkspaceId();
            String groupLabel = profile.getDisplayName();

            // Duplicated groups key
            DuplicatedGroupsKey key = this.applicationContext.getBean(DuplicatedGroupsKey.class, workspaceId, groupLabel);

            // Groups
            List<CollabProfile> groups = map.get(key);
            if (groups == null) {
                groups = new ArrayList<>();
                map.put(key, groups);
            }

            groups.add(profile);
        }

        return duplicatedGroups;
    }

}
