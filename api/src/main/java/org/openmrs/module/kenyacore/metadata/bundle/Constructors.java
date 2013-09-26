/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyacore.metadata.bundle;

import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.GlobalProperty;
import org.openmrs.LocationAttributeType;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttributeType;
import org.openmrs.Program;
import org.openmrs.VisitAttributeType;
import org.openmrs.VisitType;
import org.openmrs.customdatatype.CustomDatatype;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.openmrs.customdatatype.datatype.FreeTextDatatype;
import org.openmrs.module.kenyacore.metadata.MetadataUtils;
import org.openmrs.patient.IdentifierValidator;

/**
 * Constructors for different metadata classes
 */
public class Constructors {

	/**
	 * Constructs an encounter type
	 * @param name the name
	 * @param description the description
	 * @param uuid the UUID
	 * @return the encounter type
	 */
	public static EncounterType encounterType(String name, String description, String uuid) {
		EncounterType obj = new EncounterType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setUuid(uuid);

		return obj;
	}

	/**
	 * Constructs a form
	 * @param name the name
	 * @param description the description
	 * @param encTypeUuid the encounter type UUID
	 * @param uuid the UUID
	 * @return the form
	 */
	public static Form form(String name, String description, String encTypeUuid, String version, String uuid) {
		Form obj = new Form();
		obj.setName(name);
		obj.setDescription(description);
		obj.setEncounterType(MetadataUtils.getEncounterType(encTypeUuid));
		obj.setVersion(version);
		obj.setUuid(uuid);
		return obj;
	}

	/**
	 * Constructs a global property
	 * @param property the property
	 * @param description the description
	 * @param value the value (can be null)
	 * @return the global property
	 */
	public static GlobalProperty globalProperty(String property, String description, String value, String uuid) {
		return globalProperty(property, description, FreeTextDatatype.class, null, value, uuid);
	}

	/**
	 * Constructs a global property
	 * @param property the property
	 * @param description the description
	 * @param datatype the custom data type (can be null)
	 * @param datatypeConfig the data type config (can be null)
	 * @param value the value (can be null)
	 * @return the global property
	 */
	public static <T, H extends CustomDatatype<T>> GlobalProperty globalProperty(String property,
																		  String description,
																		  Class<H> datatype,
																		  String datatypeConfig,
																		  T value,
																		  String uuid) {
		GlobalProperty obj = new GlobalProperty();
		obj.setProperty(property);
		obj.setDescription(description);
		obj.setDatatypeClassname(datatype.getName());
		obj.setDatatypeConfig(datatypeConfig);
		obj.setUuid(uuid);

		if (value != null) {
			obj.setValue(value);
		}

		return obj;
	}

	/**
	 * Constructs a location attribute type
	 * @param name the name
	 * @param description the description
	 * @param datatype the datatype class
	 * @param datatypeConfig the data type config (can be null)
	 * @param minOccurs the minimum allowed occurrences
	 * @param maxOccurs the maximum allowed occurrences
	 * @param uuid the UUID
	 * @return the program
	 */
	public static LocationAttributeType locationAttributeType(String name, String description, Class<?> datatype, String datatypeConfig, int minOccurs, int maxOccurs, String uuid) {
		LocationAttributeType obj = new LocationAttributeType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setDatatypeClassname(datatype.getName());
		obj.setDatatypeConfig(datatypeConfig);
		obj.setMinOccurs(minOccurs);
		obj.setMaxOccurs(maxOccurs);
		obj.setUuid(uuid);
		return obj;
	}

	/**
	 * Constructs a patient identifier type
	 * @param name the name
	 * @param description the description
	 * @param format the format regex
	 * @param uuid the UUID
	 * @return the program
	 */
	public static PatientIdentifierType patientIdentifierType(String name,
													   String description,
													   String format,
													   String formatDescription,
													   Class<? extends IdentifierValidator> validator,
													   PatientIdentifierType.LocationBehavior locationBehavior,
													   boolean required,
													   String uuid) {

		PatientIdentifierType obj = new PatientIdentifierType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setFormat(format);
		obj.setFormatDescription(formatDescription);
		obj.setValidator(validator != null ? validator.getName() : null);
		obj.setLocationBehavior(locationBehavior);
		obj.setRequired(required);
		obj.setUuid(uuid);
		return obj;
	}

	/**
	 * Constructs a person attribute type
	 * @param name the name
	 * @param description the description
	 * @param format the format class
	 * @param foreignKey the foreign key (can be null)
	 * @param searchable whether attribute is searchable
	 * @param sortWeight the sort weight
	 * @param uuid the UUID
	 * @return the program
	 */
	public static PersonAttributeType personAttributeType(String name,
												   String description,
												   Class<?> format,
												   Integer foreignKey,
												   boolean searchable,
												   double sortWeight,
												   String uuid) {

		PersonAttributeType obj = new PersonAttributeType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setFormat(format.getName());
		obj.setForeignKey(foreignKey);
		obj.setSearchable(searchable);
		obj.setSortWeight(sortWeight);
		obj.setUuid(uuid);
		return obj;
	}

	/**
	 * Constructs a program
	 * @param name the name
	 * @param description the description
	 * @param concept the concept identifier
	 * @param uuid the UUID
	 * @return the program
	 */
	public static Program program(String name, String description, String concept, String uuid) {
		Program obj = new Program();
		obj.setName(name);
		obj.setDescription(description);
		obj.setConcept(MetadataUtils.getConcept(concept));
		obj.setUuid(uuid);
		return obj;
	}

	/**
	 * Constructs a visit attribute type
	 * @param name the name
	 * @param description the description
	 * @param datatype the datatype class
	 * @param datatypeConfig the data type config (can be null)
	 * @param minOccurs the minimum allowed occurrences
	 * @param maxOccurs the maximum allowed occurrences
	 * @param uuid the UUID
	 * @return the program
	 */
	public static VisitAttributeType visitAttributeType(String name, String description, Class<?> datatype, String datatypeConfig, int minOccurs, int maxOccurs, String uuid) {
		VisitAttributeType obj = new VisitAttributeType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setDatatypeClassname(datatype.getName());
		obj.setDatatypeConfig(datatypeConfig);
		obj.setMinOccurs(minOccurs);
		obj.setMaxOccurs(maxOccurs);
		obj.setUuid(uuid);
		return obj;
	}

	/**
	 * Constructs a visit type
	 * @param name the name
	 * @param description the description
	 * @param uuid the UUID
	 * @return the visit type
	 */
	public static VisitType visitType(String name, String description, String uuid) {
		VisitType obj = new VisitType();
		obj.setName(name);
		obj.setDescription(description);
		obj.setUuid(uuid);
		return obj;
	}
}