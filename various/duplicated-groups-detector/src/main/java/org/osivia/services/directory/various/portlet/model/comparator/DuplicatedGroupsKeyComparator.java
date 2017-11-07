package org.osivia.services.directory.various.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.services.directory.various.portlet.model.DuplicatedGroupsKey;
import org.springframework.stereotype.Component;

/**
 * Duplicated groups key comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see DuplicatedGroupsKey
 */
@Component
public class DuplicatedGroupsKeyComparator implements Comparator<DuplicatedGroupsKey> {

    /**
     * Constructor.
     */
    public DuplicatedGroupsKeyComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(DuplicatedGroupsKey key1, DuplicatedGroupsKey key2) {
        int result;

        if (key1 == null) {
            result = -1;
        } else if (key2 == null) {
            result = 1;
        } else {
            String workspaceId1 = key1.getWorkspaceId();
            String workspaceId2 = key2.getWorkspaceId();

            if (workspaceId1 == null) {
                result = -1;
            } else if (workspaceId2 == null) {
                result = 1;
            } else if (workspaceId1.equals(workspaceId2)) {
                String groupLabel1 = key1.getGroupLabel();
                String groupLabel2 = key2.getGroupLabel();

                if (groupLabel1 == null) {
                    result = -1;
                } else if (groupLabel2 == null) {
                    result = 1;
                } else {
                    result = groupLabel1.compareTo(groupLabel2);
                }
            } else {
                result = workspaceId1.compareTo(workspaceId2);
            }
        }

        return result;
    }

}
