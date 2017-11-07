package org.osivia.services.directory.various.portlet.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.services.directory.various.portlet.model.comparator.DuplicatedGroupsKeyComparator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Duplicated groups java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DuplicatedGroups {

    /** Map. */
    private final Map<DuplicatedGroupsKey, List<CollabProfile>> map;
    
    
    /**
     * Constructor.
     * 
     * @param comparator comparator
     */
    public DuplicatedGroups(DuplicatedGroupsKeyComparator comparator) {
        super();
        this.map = new TreeMap<>(comparator);
    }


    /**
     * Getter for map.
     * 
     * @return the map
     */
    public Map<DuplicatedGroupsKey, List<CollabProfile>> getMap() {
        return map;
    }

}
