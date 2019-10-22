package org.osivia.directory.v2.model.preferences;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * User saved search implementation.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserSavedSearchImpl implements UserSavedSearch {

    /**
     * Identifier.
     */
    private final int id;

    /**
     * Display name.
     */
    private String displayName;
    /**
     * Order.
     */
    private Integer order;
    /**
     * Data.
     */
    private String data;


    /**
     * Constructor.
     *
     * @param id identifier
     */
    public UserSavedSearchImpl(int id) {
        super();
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSavedSearchImpl that = (UserSavedSearchImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public int getId() {
        return this.id;
    }


    @Override
    public String getDisplayName() {
        return this.displayName;
    }


    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public Integer getOrder() {
        return this.order;
    }


    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }


    @Override
    public String getData() {
        return this.data;
    }


    @Override
    public void setData(String data) {
        this.data = data;
    }

}
