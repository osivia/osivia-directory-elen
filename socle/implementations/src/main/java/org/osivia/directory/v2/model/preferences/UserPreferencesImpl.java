package org.osivia.directory.v2.model.preferences;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * User preferences implementation.
 *
 * @author Cédric Krommenhoek
 * @see UserPreferences
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserPreferencesImpl implements UserPreferences {

    /**
     * Document identifier.
     */
    private final String documentId;

    /**
     * Folder displays.
     */
    private Map<String, String> folderDisplays;
    /**
     * Saved searches.
     */
    private List<UserSavedSearch> savedSearches;
    /**
     * Updated user preferences indicator.
     */
    private boolean updated;


    /**
     * Constructor.
     *
     * @param documentId document identifier
     */
    public UserPreferencesImpl(String documentId) {
        super();
        this.documentId = documentId;
    }


    @Override
    public String getDocumentId() {
        return this.documentId;
    }


    @Override
    public Map<String, String> getFolderDisplays() {
        return this.folderDisplays;
    }


    @Override
    public void setFolderDisplays(Map<String, String> folderDisplays) {
        this.folderDisplays = folderDisplays;
    }


    @Override
    public List<UserSavedSearch> getSavedSearches() {
        return this.savedSearches;
    }


    @Override
    public void setSavedSearches(List<UserSavedSearch> savedSearches) {
        this.savedSearches = savedSearches;
    }


    @Override
    public boolean areUpdated() {
        return this.updated;
    }


    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

}
