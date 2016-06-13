/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.directory.v2;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Transient;

/**
 * Utility class to add some hints to the ODM
 * @author Loïc Billon
 * @since 4.4
 */
public class MappingHelper {

	/**
	 * Static class
	 */
	private MappingHelper() {
		
	}
	
	/**
	 * Read the first objectclass of an entity and generate a filter
	 * @param odm object entity
	 * @return filter
	 */
	public static AndFilter getBasicFilter(Object odm) {

		Entry entry = odm.getClass().getAnnotation(Entry.class);
		if (entry != null) {

			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter("objectclass", entry.objectClasses()[0]));

			return filter;
		} else
			return null;
	}

	/**
	 * Read the LDAP field name of an attribute (defined in the mapping)
	 * @param odm object entity
	 * @param attribute the class attribute
	 * @return field name
	 */
	public static String getLdapFieldName(Object odm, String attribute) {

		if (odm.getClass().isAnnotationPresent(Entry.class)) {
			try {
				Field field = odm.getClass().getDeclaredField(attribute);

				if (field != null) {
					Attribute attrAnnotation = field
							.getAnnotation(Attribute.class);
					if (StringUtils.isNotEmpty(attrAnnotation.name())) {
						return attrAnnotation.name();
					} else
						return attribute;
				} else
					return null;

			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else
			return null;

	}

	/**
	 * Build an Or filter with all fields that are not empty in the object
	 * - Use the annotation @Attribute and the name of the field if not setted
	 * - Ignore @Transiant attribute
	 * - check if an attribute is a collection and generate a logicial test for each value
	 * @param  odm object entity
	 * @return the Or filter
	 */
	public static OrFilter generateOrFilter(Object odm) {

		OrFilter or = new OrFilter();
		if (odm.getClass().isAnnotationPresent(Entry.class)) {

			try {

				for (PropertyDescriptor pd : Introspector.getBeanInfo(
						odm.getClass()).getPropertyDescriptors()) {
					String name = pd.getName();
					if (pd.getReadMethod() != null && !"class".equals(name)) {

						Attribute attrAnnotation = odm.getClass()
								.getDeclaredField(name)
								.getAnnotation(Attribute.class);
						Transient transAnnotation = odm.getClass()
								.getDeclaredField(name)
								.getAnnotation(Transient.class);

						if (attrAnnotation != null && transAnnotation == null) {
							if (StringUtils.isNotEmpty(attrAnnotation.name())) {
								name = attrAnnotation.name();
							}

							Object getterResult = pd.getReadMethod()
									.invoke(odm);

							if (getterResult != null) {
								if (getterResult instanceof Collection<?>) {
									Collection<Object> collection = (Collection<Object>) getterResult;
									for (Object o : collection) {
										or.or(new LikeFilter(name, o
												.toString()));

									}
								} else {

									or.or(new LikeFilter(name, getterResult
											.toString()));
								}
							}
						}
					}
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return or;
	}

	/**
	 * Build an And filter with all fields that are not empty in the object
	 * - Use the annotation @Attribute and the name of the field if not setted
	 * - Ignore @Transiant attribute
	 * - check if an attribute is a collection and generate a logicial test for each value
	 * @param  odm object entity
	 * @return the And filter
	 */
	public static AndFilter generateAndFilter(Object odm) {

		AndFilter and = new AndFilter();

		if (odm.getClass().isAnnotationPresent(Entry.class)) {

			try {

				for (PropertyDescriptor pd : Introspector.getBeanInfo(
						odm.getClass()).getPropertyDescriptors()) {
					String name = pd.getName();
					if (pd.getReadMethod() != null && !"class".equals(name)) {

						Attribute attrAnnotation = odm.getClass()
								.getDeclaredField(name)
								.getAnnotation(Attribute.class);
						Transient transAnnotation = odm.getClass()
								.getDeclaredField(name)
								.getAnnotation(Transient.class);

						if (attrAnnotation != null && transAnnotation == null) {
							if (StringUtils.isNotEmpty(attrAnnotation.name())) {
								name = attrAnnotation.name();
							}

							Object getterResult = pd.getReadMethod()
									.invoke(odm);

							if (getterResult != null) {
								if (getterResult instanceof Collection<?>) {
									Collection<Object> collection = (Collection<Object>) getterResult;
									for (Object o : collection) {
										and.and(new LikeFilter(name, o
												.toString()));

									}
								} else {

									and.and(new LikeFilter(name, getterResult
											.toString()));
								}
							}
						}
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return and;
	}
}
