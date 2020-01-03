package org.osivia.directory.v2.repository.preferences;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Update user preferences Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpdateUserPreferencesCommand implements INuxeoCommand {

    /**
     * Terms of service xpath.
     */
    public static final String TERMS_OF_SERVICE = "ttc_userprofile:termsOfService";

    /**
     * Folder preferences xpath.
     */
    public static final String METADATA_FOLDERS_PREFS = "ttc_userprofile:folders_prefs";
    /**
     * Folder identifier.
     */
    public static final String METADATA_FOLDERS_PREFS_ID = "webid";
    /**
     * Folder display value.
     */
    public static final String METADATA_FOLDERS_PREFS_VALUE = "display_style";

    /**
     * Saved searches xpath.
     */
    public static final String SAVED_SEARCHES_XPATH = "ttc_userprofile:savedSearches";
    
    /**
     * Properties preferences xpath.
     */
    public static final String METADATA_PROPERTIES_PREFS = "ttc_userprofile:propertys_prefs";    
    
    /**
     * Properties preferences name.
     */
    public static final String METADATA_PROPERTIES_PREFS_NAME = "name";       
    /**
     * Properties preferences value.
     */
    public static final String METADATA_PROPERTIES_PREFS_VALUE = "value";         
    /**
     * Saved search identifier.
     */
    public static final String SAVED_SEARCH_ID = "searchId";
    /**
     * Saved search display name.
     */
    public static final String SAVED_SEARCH_DISPLAY_NAME = "displayName";
    /**
     * Saved search order.
     */
    public static final String SAVED_SEARCH_ORDER = "order";
    /**
     * Saved search data.
     */
    public static final String SAVED_SEARCH_DATA = "data";


    // User preferences
    private final UserPreferences preferences;


    /**
     * Constructor.
     *
     * @param preferences user preferences
     */
    public UpdateUserPreferencesCommand(UserPreferences preferences) {
        super();
        this.preferences = preferences;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Properties
        PropertyMap properties = this.getProperties();

        // Document reference
        DocRef docRef = new IdRef(this.preferences.getDocumentId());

        return documentService.update(docRef, properties);
    }


    /**
     * Get properties.
     *
     * @return properties
     */
    protected PropertyMap getProperties() {
        PropertyMap properties = new PropertyMap();
        properties.set(TERMS_OF_SERVICE, this.preferences.getTermsOfService());
        properties.set(METADATA_FOLDERS_PREFS, this.convertFolders(this.preferences.getFolderDisplays()));
        properties.set(SAVED_SEARCHES_XPATH, this.convertSavedSearches(this.preferences.getSavedSearches()));
        properties.set(METADATA_PROPERTIES_PREFS, this.convertProperties(this.preferences.getUserProperties()));
        return properties;
    }


    /**
     * Convert folders to JSON.
     *
     * @param folders folders
     * @return JSON
     */
    protected String convertFolders(Map<String, String> folders) {
        JSONArray array = new JSONArray();

        for (Map.Entry<String, String> entry : folders.entrySet()) {
            JSONObject object = new JSONObject();
            object.put(METADATA_FOLDERS_PREFS_ID, entry.getKey());
            object.put(METADATA_FOLDERS_PREFS_VALUE, entry.getValue());

            array.add(object);
        }

        return array.toString();
    }

    
    /**
     * Convert properties to JSON.
     *
     * @param folders folders
     * @return JSON
     */
    protected String convertProperties(Map<String, String> folders) {
        JSONArray array = new JSONArray();

        for (Map.Entry<String, String> entry : folders.entrySet()) {
            JSONObject object = new JSONObject();
            object.put(METADATA_PROPERTIES_PREFS_NAME, entry.getKey());
            object.put(METADATA_PROPERTIES_PREFS_VALUE, entry.getValue());

            array.add(object);
        }

        return array.toString();
    }
    

    /**
     * Convert saved searches to JSON.
     *
     * @param savedSearches saved searches
     * @return JSON
     */
    protected String convertSavedSearches(List<UserSavedSearch> savedSearches) {
        String result;

        if (CollectionUtils.isEmpty(savedSearches)) {
            result = null;
        } else {
            JSONArray array = new JSONArray();

            for (UserSavedSearch savedSearch : savedSearches) {
                JSONObject object = new JSONObject();
                object.put(SAVED_SEARCH_ID, savedSearch.getId());
                object.put(SAVED_SEARCH_DISPLAY_NAME, savedSearch.getDisplayName());
                object.put(SAVED_SEARCH_ORDER, savedSearch.getOrder());
                object.put(SAVED_SEARCH_DATA, savedSearch.getData());

                array.add(object);
            }

            result = array.toString();
        }

        return result;
    }


    @Override
    public String getId() {
        return null;
    }

}
