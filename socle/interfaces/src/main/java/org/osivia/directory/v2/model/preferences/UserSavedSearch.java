package org.osivia.directory.v2.model.preferences;

/**
 * User saved search interface.
 *
 * @author Cédric Krommenhoek
 */
public interface UserSavedSearch {

    /**
     * Get search identifier.
     *
     * @return identifier
     */
    int getId();


    /**
     * Get search display name.
     *
     * @return display name
     */
    String getDisplayName();


    /**
     * Set search display name.
     *
     * @param displayName display name
     */
    void setDisplayName(String displayName);


    /**
     * Get search order.
     *
     * @return order
     */
    Integer getOrder();


    /**
     * Set search order.
     *
     * @param order order
     */
    void setOrder(Integer order);


    /**
     * Get search data.
     *
     * @return data
     */
    String getData();


    /**
     * Set search data.
     *
     * @param data data
     */
    void setData(String data);

}
