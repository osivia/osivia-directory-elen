package org.osivia.services.directory.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.springframework.stereotype.Component;

/**
 * Local group comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see LocalGroup
 */
@Component
public class LocalGroupsComparator implements Comparator<LocalGroup> {

    /**
     * Constructor.
     */
    public LocalGroupsComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(LocalGroup group1, LocalGroup group2) {
        return group1.getDisplayName().compareToIgnoreCase(group2.getDisplayName());
    }

}
