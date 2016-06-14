package org.osivia.services.directory.workspace.portlet.model;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.WorkspaceMember;

/**
 * Member comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Member
 */
public class MemberComparator implements Comparator<WorkspaceMember> {

    /** Comparator sort field. */
    private final String sort;
    /** Comparator alternative sort indicator. */
    private final boolean alt;


    /**
     * Constructor.
     * 
     * @param sort comparator sort field
     * @param alt comparator alternative sort indicator
     */
    public MemberComparator(String sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(WorkspaceMember member1, WorkspaceMember member2) {
        int result;

        if (member1 == null) {
            result = -1;
        } else if (member2 == null) {
            result = 1;
        } else if ("role".equals(this.sort)) {
            // Role
            Integer role1 = member1.getRole().getWeight();
            Integer role2 = member2.getRole().getWeight();

            result = role1.compareTo(role2);
        } else {
            // Name
            String name1 = StringUtils.defaultIfBlank(member1.getMember().getDisplayName(), member1.getMember().getUid());
            String name2 = StringUtils.defaultIfBlank(member2.getMember().getDisplayName(), member2.getMember().getUid());
            result = name1.compareToIgnoreCase(name2);
        }

        if (alt) {
            result = -result;
        }

        return result;
    }

}
