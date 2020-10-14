package org.osivia.directory.v2.model.preferences;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
     * Terms of service.
     */
    private String termsOfService;
    /**
     * Folder displays.
     */
    private Map<String, String> folderDisplays;
    /**
     * Categorized saved searches.
     */
    private Map<String, List<UserSavedSearch>> categorizedSavedSearches;

    /**
     * User properties
     */
    private Map<String, String> userProperties;

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
    public String getTermsOfService() {
        return this.termsOfService;
    }


    @Override
    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
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
        return this.getSavedSearches(StringUtils.EMPTY);
    }


    @Override
    public void setSavedSearches(List<UserSavedSearch> savedSearches) {
        this.setSavedSearches(StringUtils.EMPTY, savedSearches);
    }


    @Override
    public List<UserSavedSearch> getSavedSearches(String categoryId) {
        List<UserSavedSearch> savedSearches;
        if (MapUtils.isEmpty(this.categorizedSavedSearches)) {
            savedSearches = null;
        } else {
            savedSearches = this.categorizedSavedSearches.get(categoryId);
        }
        return savedSearches;
    }


    @Override
    public void setSavedSearches(String categoryId, List<UserSavedSearch> savedSearches) {
        if (this.categorizedSavedSearches == null) {
            this.categorizedSavedSearches = new HashMap<>();
        }
        if (CollectionUtils.isEmpty(savedSearches)) {
            this.categorizedSavedSearches.remove(categoryId);
        } else {
            this.categorizedSavedSearches.put(categoryId, savedSearches);
        }
    }


    @Override
    public Map<String, List<UserSavedSearch>> getCategorizedSavedSearches() {
        return categorizedSavedSearches;
    }


    public void setCategorizedSavedSearches(Map<String, List<UserSavedSearch>> categorizedSavedSearches) {
        this.categorizedSavedSearches = categorizedSavedSearches;
    }


    @Override
    public boolean areUpdated() {
        return this.updated;
    }


    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }


    @Override
    public Map<String, String> getUserProperties() {
        return userProperties;
    }


    @Override
    public void setUserProperties(Map<String, String> properties) {
        userProperties = properties;
    }

}
