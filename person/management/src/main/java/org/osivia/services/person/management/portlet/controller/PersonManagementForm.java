/**
 * 
 */
package org.osivia.services.person.management.portlet.controller;

/**
 * @author Loïc Billon
 *
 */
public class PersonManagementForm {

	/** Search field */
	private String filter;
	
	/** currently selected person (hidden field) */
	private String selectedPerson;

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the selectedPerson
	 */
	public String getSelectedPerson() {
		return selectedPerson;
	}

	/**
	 * @param selectedPerson the selectedPerson to set
	 */
	public void setSelectedPerson(String selectedPerson) {
		this.selectedPerson = selectedPerson;
	}
	
	
}
