package org.osivia.services.firstconnection.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserForm {

	/** Title */
	private String title;
    /** Identifier. */
    private String id;
    /** First name. */
    private String firstName;
    /** Last name. */
    private String lastName;
    /** Email. */
    private String email;
    /** Password. */
    private String password;
    /** Passord confirmation. */
    private String passwordConfirmation;
    /** Flag if ssers must change their password */
    private boolean mustChangePassword = false;


    /**
     * Constructor.
     */
    public UserForm() {
        super();
    }

    

    /**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}



	/**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for firstName.
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName.
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName.
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName.
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for passwordConfirmation.
     * 
     * @return the passwordConfirmation
     */
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    /**
     * Setter for passwordConfirmation.
     * 
     * @param passwordConfirmation the passwordConfirmation to set
     */
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }



	public boolean isMustChangePassword() {
		return mustChangePassword;
	}



	public void setMustChangePassword(boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}
    
    

}
