package org.osivia.services.directory.workspace.portlet.model;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

/**
 * Member comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Member
 */
public class MemberComparator implements Comparator<Member> {

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
    public int compare(Member member1, Member member2) {
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
            String name1 = StringUtils.defaultIfBlank(member1.getDisplayName(), member1.getName());
            String name2 = StringUtils.defaultIfBlank(member2.getDisplayName(), member2.getName());
            result = name1.compareToIgnoreCase(name2);
        }

        if (alt) {
            result = -result;
        }

        return result;
    }

}
