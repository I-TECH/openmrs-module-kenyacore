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

package org.openmrs.module.kenyacore.metadata;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.LocationAttributeType;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttributeType;
import org.openmrs.Program;
import org.openmrs.VisitAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link MetadataUtils}
 */
public class MetadataUtilsTest extends BaseModuleContextSensitiveTest {

	private static final String INVALID_UUID = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";

	@Test
	public void integration() {
		new MetadataUtils();
	}

	/**
	 * @see MetadataUtils#getConcept(String)
	 */
	@Test
	public void getConcept_shouldFetchByUuid() {
		// Test non-numeric concept
		Concept yes = Context.getConceptService().getConcept(7);
		Assert.assertThat(MetadataUtils.getConcept("b055abd8-a420-4a11-8b98-02ee170a7b54"), is(yes));

		// Test numeric concept
		Concept cd4 = Context.getConceptService().getConcept(5497);
		Concept fetched = MetadataUtils.getConcept("a09ab2c5-878e-4905-b25d-5784167d0216");
		Assert.assertThat(fetched, is(cd4));
		Assert.assertThat(fetched, is(instanceOf(ConceptNumeric.class)));
	}

	/**
	 * @see MetadataUtils#getConcept(String)
	 */
	@Test
	public void getConcept_shouldFetchByMapping() {
		Concept cd4 = Context.getConceptService().getConcept(5497);
		Assert.assertThat(MetadataUtils.getConcept("SSTRM:CD41003"), is(cd4));
	}

	/**
	 * @see MetadataUtils#getConcept(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getConcept_shouldThrowExceptionForNonExistentMapping() {
		MetadataUtils.getConcept("SSTRM:XXXXXX");
	}

	/**
	 * @see MetadataUtils#getConcept(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getConcept_shouldThrowExceptionForNonExistentUuid() {
		MetadataUtils.getConcept(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getConcept(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getConcept_shouldThrowExceptionForNumericConceptWithNoConceptNumeric() {
		MetadataUtils.getConcept("11716f9c-1434-4f8d-b9fc-9aa14c4d6129");
	}

	/**
	 * @see MetadataUtils#getEncounterType(String)
	 */
	@Test
	public void getEncounterType_shouldFetchByUuid() {
		EncounterType emergency = Context.getEncounterService().getEncounterType(2);
		Assert.assertThat(MetadataUtils.getEncounterType("07000be2-26b6-4cce-8b40-866d8435b613"), is(emergency));
	}

	/**
	 * @see MetadataUtils#getEncounterType(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getEncounterType_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getEncounterType(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getForm(String)
	 */
	@Test
	public void getForm_shouldFetchByUuid() {
		Form basic = Context.getFormService().getForm(1);
		Assert.assertThat(MetadataUtils.getForm("d9218f76-6c39-45f4-8efa-4c5c6c199f50"), is(basic));
	}

	/**
	 * @see MetadataUtils#getForm(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getForm_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getForm(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getLocation(String)
	 */
	@Test
	public void getLocation_shouldFetchByUuid() {
		Location unknown = Context.getLocationService().getLocation(1);
		Assert.assertThat(MetadataUtils.getLocation("dc5c1fcc-0459-4201-bf70-0b90535ba362"), is(unknown));
	}

	/**
	 * @see MetadataUtils#getLocation(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getLocation_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getLocation(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getLocationAttributeType(String)
	 */
	@Test
	public void getLocationAttributeType_shouldFetchByUuid() {
		// No location attribute type in the standard test data so make one..
		LocationAttributeType phoneAttrType = new LocationAttributeType();
		phoneAttrType.setName("Facility Phone");
		phoneAttrType.setMinOccurs(0);
		phoneAttrType.setMaxOccurs(1);
		phoneAttrType.setDatatypeClassname("org.openmrs.customdatatype.datatype.FreeTextDatatype");
		Context.getLocationService().saveLocationAttributeType(phoneAttrType);
		String savedUuid = phoneAttrType.getUuid();

		Assert.assertThat(MetadataUtils.getLocationAttributeType(savedUuid), is(phoneAttrType));
	}

	/**
	 * @see MetadataUtils#getLocationAttributeType(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getLocationAttributeType_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getLocationAttributeType(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getPatientIdentifierType(String)
	 */
	@Test
	public void getPatientIdentifierType_shouldFetchByUuid() {
		PatientIdentifierType openmrs = Context.getPatientService().getPatientIdentifierType(1);
		Assert.assertThat(MetadataUtils.getPatientIdentifierType("1a339fe9-38bc-4ab3-b180-320988c0b968"), is(openmrs));
	}

	/**
	 * @see MetadataUtils#getPatientIdentifierType(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getPatientIdentifierType_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getPatientIdentifierType(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getPersonAttributeType(String)
	 */
	@Test
	public void getPersonAttributeType_shouldFetchByUuid() {
		PersonAttributeType civil = Context.getPersonService().getPersonAttributeType(8);
		Assert.assertThat(MetadataUtils.getPersonAttributeType("a0f5521c-dbbd-4c10-81b2-1b7ab18330df"), is(civil));
	}

	/**
	 * @see MetadataUtils#getPersonAttributeType(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getPersonAttributeType_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getPersonAttributeType(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getProgram(String)
	 */
	@Test
	public void getProgram_shouldFetchByUuid() {
		Program hiv = Context.getProgramWorkflowService().getProgram(1);
		Assert.assertThat(MetadataUtils.getProgram("da4a0391-ba62-4fad-ad66-1e3722d16380"), is(hiv));
	}

	/**
	 * @see MetadataUtils#getProgram(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getProgram_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getProgram(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getVisitAttributeType(String)
	 */
	@Test
	public void getVisitAttributeType_shouldFetchByUuid() {
		// No location attribute type in the standard test data so make one..
		VisitAttributeType visitAttrType = new VisitAttributeType();
		visitAttrType.setName("Test Type");
		visitAttrType.setMinOccurs(0);
		visitAttrType.setMaxOccurs(1);
		visitAttrType.setDatatypeClassname("org.openmrs.customdatatype.datatype.FreeTextDatatype");
		Context.getVisitService().saveVisitAttributeType(visitAttrType);
		String savedUuid = visitAttrType.getUuid();

		Assert.assertThat(MetadataUtils.getVisitAttributeType(savedUuid), is(visitAttrType));
	}

	/**
	 * @see MetadataUtils#getVisitAttributeType(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getVisitAttributeType_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getVisitAttributeType(INVALID_UUID);
	}

	/**
	 * @see MetadataUtils#getVisitType(String)
	 */
	@Test
	public void getVisitType_shouldFetchByUuid() {
		Assert.assertNotNull(MetadataUtils.getVisitType("c0c579b0-8e59-401d-8a4a-976a0b183519")); // Initial
	}

	/**
	 * @see MetadataUtils#getVisitType(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getVisitType_shouldThrowExceptionForNonExistent() {
		MetadataUtils.getVisitType(INVALID_UUID);
	}
}