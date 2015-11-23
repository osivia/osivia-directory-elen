/**
 * 
 */
package fr.toutatice.outils.ldap.util;

import java.util.Comparator;

import fr.toutatice.outils.ldap.entity.Person;

/**
 * @author lbillon
 *
 */
public class PersonComparator implements Comparator<Person> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Person p1, Person p2) {

		int compareTo = p1.getSn().compareTo(p2.getSn());
		
		// Cas du même nom
		if(compareTo == 0){
			compareTo = p1.getGivenName().compareTo(p2.getGivenName());
		}
		
		return compareTo;
	}

}
