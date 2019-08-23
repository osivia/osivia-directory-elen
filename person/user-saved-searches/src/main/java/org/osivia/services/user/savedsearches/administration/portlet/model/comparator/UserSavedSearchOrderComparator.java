package org.osivia.services.user.savedsearches.administration.portlet.model.comparator;

import org.osivia.portal.api.user.UserSavedSearch;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * User saved search order comparator.
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see UserSavedSearch
 */
@Component
public class UserSavedSearchOrderComparator implements Comparator<UserSavedSearch> {

    /**
     * Constructor.
     */
    public UserSavedSearchOrderComparator() {
        super();
    }

    @Override
    public int compare(UserSavedSearch savedSearch1, UserSavedSearch savedSearch2) {
        int result;

        if (savedSearch1 == null) {
            result = -1;
        } else if (savedSearch2 == null) {
            result = 1;
        } else if (savedSearch1.getOrder() == null) {
            if (savedSearch2.getOrder() == null) {
                // Identifier comparison
                result = Integer.compare(savedSearch1.getId(), savedSearch2.getId());
            } else {
                result = 1;
            }
        } else if (savedSearch2.getOrder() == null) {
            result = -1;
        } else {
            // Order comparison
            result = Integer.compare(savedSearch1.getOrder(), savedSearch2.getOrder());
        }

        return result;
    }

}
