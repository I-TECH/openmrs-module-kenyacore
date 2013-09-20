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

package org.openmrs.module.kenyacore.metadata.installer;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.GlobalProperty;
import org.openmrs.Program;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.customdatatype.SerializingCustomDatatype;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link org.openmrs.module.kenyacore.metadata.installer.CoreMetadataInstaller}
 */
public class CoreMetadataInstallerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private CoreMetadataInstaller installer;

	/**
	 * @see CoreMetadataInstaller#encounterType(String, String, String)
	 */
	@Test
	public void encounterType() throws Exception {
		// Check creating new
		installer.encounterType("Test Encounter", "Testing", "enc-type-uuid");

		EncounterType created = Context.getEncounterService().getEncounterTypeByUuid("enc-type-uuid");
		Assert.assertThat(created.getName(), is("Test Encounter"));
		Assert.assertThat(created.getDescription(), is("Testing"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.encounterType("New name", "New desc", "enc-type-uuid");

		EncounterType updated = Context.getEncounterService().getEncounterTypeByUuid("enc-type-uuid");
		Assert.assertThat(updated.getName(), is("New name"));
		Assert.assertThat(updated.getDescription(), is("New desc"));
	}

	/**
	 * @see CoreMetadataInstaller#form(String, String, String, String, String)
	 */
	@Test
	public void form() throws Exception {
		// Check creating new
		installer.encounterType("Test Encounter", "Testing", "enc-type1-uuid");
		installer.form("Test Form #1", "Testing", "enc-type1-uuid", "1.0", "form-uuid");

		Form created = Context.getFormService().getFormByUuid("form-uuid");
		Assert.assertThat(created.getName(), is("Test Form #1"));
		Assert.assertThat(created.getDescription(), is("Testing"));
		Assert.assertThat(created.getEncounterType(), is(Context.getEncounterService().getEncounterTypeByUuid("enc-type1-uuid")));
		Assert.assertThat(created.getVersion(), is("1.0"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.encounterType("Other Encounter", "Testing", "enc-type2-uuid");
		installer.form("New name", "New desc", "enc-type2-uuid", "2.0", "form-uuid");

		Form updated = Context.getFormService().getFormByUuid("form-uuid");
		Assert.assertThat(updated.getName(), is("New name"));
		Assert.assertThat(updated.getDescription(), is("New desc"));
		Assert.assertThat(updated.getEncounterType(), is(Context.getEncounterService().getEncounterTypeByUuid("enc-type2-uuid")));
		Assert.assertThat(updated.getVersion(), is("2.0"));
	}

	/**
	 * @see CoreMetadataInstaller#globalProperty(String, String, Class, Object, String)
	 */
	@Test
	public void globalProperty() throws Exception {
		// Check creating new
		installer.globalProperty("test.property", "Testing", null, "Value", "gp1-uuid");

		GlobalProperty created = Context.getAdministrationService().getGlobalPropertyObject("test.property");
		Assert.assertThat(created.getDescription(), is("Testing"));
		Assert.assertThat(created.getDatatypeClassname(), is(nullValue()));
		Assert.assertThat(created.getValue(), is((Object) "Value"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.globalProperty("test.property", "New desc", null, "New value", "gp1-uuid");

		GlobalProperty updated = Context.getAdministrationService().getGlobalPropertyObject("test.property");
		Assert.assertThat(updated.getDescription(), is("New desc"));
		Assert.assertThat(updated.getDatatypeClassname(), is(nullValue()));
		Assert.assertThat(updated.getValue(), is((Object) "New value"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing by property name
		installer.globalProperty("test.property", "Diff desc", null, "Diff value", "gp2-uuid");

		updated = Context.getAdministrationService().getGlobalPropertyObject("test.property");
		Assert.assertThat(updated.getDescription(), is("Diff desc"));
		Assert.assertThat(updated.getDatatypeClassname(), is(nullValue()));
		Assert.assertThat(updated.getValue(), is((Object) "Diff value"));
		Assert.assertThat(updated.getUuid(), is("gp2-uuid"));

		// Check with custom data type and null value
		GlobalProperty custom = installer.globalProperty("test.property2", "Testing", TestingDatatype.class, null, "gp3-uuid");
		Assert.assertThat(custom.getValue(), is(nullValue()));

		// Check with custom data type and non-null value
		EncounterType encType = installer.encounterType("Test Encounter", "Testing", "enc-type-uuid");
		custom = installer.globalProperty("test.property2", "Testing", TestingDatatype.class, encType, "gp3-uuid");
		Assert.assertThat(custom.getValue(), is((Object) encType));
	}

	/**
	 * @see CoreMetadataInstaller#program(String, String, String, String)
	 */
	@Test
	public void program() throws Exception {
		// Existing concepts in test data
		final String HIV_PROGRAM_UUID = "0a9afe04-088b-44ca-9291-0a8c3b5c96fa";
		final String MALARIA_PROGRAM_UUID = "f923524a-b90c-4870-a948-4125638606fd";

		// Check creating new
		installer.program("Test Program", "Testing", HIV_PROGRAM_UUID, "prog-type-uuid");

		Program created = Context.getProgramWorkflowService().getProgramByUuid("prog-type-uuid");
		Assert.assertThat(created.getName(), is("Test Program"));
		Assert.assertThat(created.getDescription(), is("Testing"));
		Assert.assertThat(created.getConcept(), is(Context.getConceptService().getConceptByUuid(HIV_PROGRAM_UUID)));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.program("New name", "New desc", MALARIA_PROGRAM_UUID, "prog-type-uuid");

		Program updated = Context.getProgramWorkflowService().getProgramByUuid("prog-type-uuid");
		Assert.assertThat(updated.getName(), is("New name"));
		Assert.assertThat(updated.getDescription(), is("New desc"));
		Assert.assertThat(updated.getConcept(), is(Context.getConceptService().getConceptByUuid(MALARIA_PROGRAM_UUID)));

		Context.flushSession();
		Context.clearSession();

		// Check update existing when name conflicts
		installer.program("New name", "Diff desc", MALARIA_PROGRAM_UUID, "prog-type2-uuid");
		updated = Context.getProgramWorkflowService().getProgramByUuid("prog-type2-uuid");
		Assert.assertThat(updated.getName(), is("New name"));
		Assert.assertThat(updated.getDescription(), is("Diff desc"));

		Program old = Context.getProgramWorkflowService().getProgramByUuid("prog-type-uuid");
		Assert.assertThat(old, is(nullValue()));
	}

	/**
	 * @see CoreMetadataInstaller#visitType(String, String, String)
	 */
	@Test
	public void visitType() throws Exception {
		// Check creating new
		installer.visitType("Test Visit", "Testing", "visit-type-uuid");

		VisitType type = Context.getVisitService().getVisitTypeByUuid("visit-type-uuid");
		Assert.assertThat(type.getName(), is("Test Visit"));
		Assert.assertThat(type.getDescription(), is("Testing"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.visitType("New name", "New desc", "visit-type-uuid");

		type = Context.getVisitService().getVisitTypeByUuid("visit-type-uuid");
		Assert.assertThat(type.getName(), is("New name"));
		Assert.assertThat(type.getDescription(), is("New desc"));
	}

	/**
	 * Custom data type class for testing based on encounter types
	 */
	public static class TestingDatatype extends SerializingCustomDatatype<EncounterType> {

		@Override
		public String serialize(EncounterType typedValue) {
			return typedValue != null ? String.valueOf(typedValue.getId()) : "";
		}

		@Override
		public EncounterType deserialize(String serializedValue) {
			return StringUtils.isNotEmpty(serializedValue) ? Context.getEncounterService().getEncounterType(Integer.valueOf(serializedValue)) : null;
		}
	}
}