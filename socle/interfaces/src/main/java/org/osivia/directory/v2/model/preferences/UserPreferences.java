package org.osivia.directory.v2.model.preferences;

import java.util.List;
import java.util.Map;

/**
 * User preferences interface.
 *
 * @author Cédric Krommenhoek
 */
public interface UserPreferences {

    /**
     * Get user preferences document identifier.
     *
     * @return document identifier
     */
    String getDocumentId();


    /**
     * Get terms of service.
     *
     * @return terms of service
     */
    String getTermsOfService();


    /**
     * Set terms of service.
     *
     * @param termsOfService
     */
    void setTermsOfService(String termsOfService);


    /**
     * Get user saved folder displays.
     *
     * @return folder displays
     */
    Map<String, String> getFolderDisplays();


    /**
     * Set user saved folder displays.
     *
     * @param folderDisplays folder displays
     */
    void setFolderDisplays(Map<String, String> folderDisplays);


    /**
     * Get user saved searches.
     *
     * @return saved searches
     */
    List<UserSavedSearch> getSavedSearches();


    /**
     * Set user saved searches.
     *
     * @param savedSearches saved searches
     */
    void setSavedSearches(List<UserSavedSearch> savedSearches);


    /**
     * Check if user preferences have been updated.
     *
     * @return true if user preferences have been updated
     */
    boolean areUpdated();


    /**
     * Set updated user preferences indicator.
     *
     * @param updated updated indicator
     */
    void setUpdated(boolean updated);

}
