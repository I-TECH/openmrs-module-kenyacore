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
import org.junit.Before;
import org.junit.Test;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Program;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractMetadataInstaller}
 */
public class AbstractMetadataInstallerTest extends BaseModuleContextSensitiveTest {

	private AbstractMetadataInstaller installer;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		installer = new AbstractMetadataInstaller() {
			@Override
			public void install() {
			}
		};
	}

	/**
	 * @see AbstractMetadataInstaller#installEncounterType(String, String, String)
	 */
	@Test
	public void installEncounterType() throws Exception {
		// Check creating new
		installer.installEncounterType("Test Encounter", "Testing", "enc-type-uuid");

		EncounterType type = Context.getEncounterService().getEncounterTypeByUuid("enc-type-uuid");
		Assert.assertThat(type.getName(), is("Test Encounter"));
		Assert.assertThat(type.getDescription(), is("Testing"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.installEncounterType("New name", "New desc", "enc-type-uuid");

		type = Context.getEncounterService().getEncounterTypeByUuid("enc-type-uuid");
		Assert.assertThat(type.getName(), is("New name"));
		Assert.assertThat(type.getDescription(), is("New desc"));
	}

	/**
	 * @see AbstractMetadataInstaller#installForm(String, String, String, String, String)
	 */
	@Test
	public void installForm() throws Exception {
		// Check creating new
		installer.installEncounterType("Test Encounter", "Testing", "enc-type1-uuid");
		installer.installForm("Test Form #1", "Testing", "enc-type1-uuid", "1.0", "form-uuid");

		Form form = Context.getFormService().getFormByUuid("form-uuid");
		Assert.assertThat(form.getName(), is("Test Form #1"));
		Assert.assertThat(form.getDescription(), is("Testing"));
		Assert.assertThat(form.getEncounterType(), is(Context.getEncounterService().getEncounterTypeByUuid("enc-type1-uuid")));
		Assert.assertThat(form.getVersion(), is("1.0"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.installEncounterType("Other Encounter", "Testing", "enc-type2-uuid");
		installer.installForm("New name", "New desc", "enc-type2-uuid", "2.0", "form-uuid");

		form = Context.getFormService().getFormByUuid("form-uuid");
		Assert.assertThat(form.getName(), is("New name"));
		Assert.assertThat(form.getDescription(), is("New desc"));
		Assert.assertThat(form.getEncounterType(), is(Context.getEncounterService().getEncounterTypeByUuid("enc-type2-uuid")));
		Assert.assertThat(form.getVersion(), is("2.0"));
	}

	/**
	 * @see AbstractMetadataInstaller#installProgram(String, String, String, String)
	 */
	@Test
	public void installProgram() throws Exception {
		// Existing concepts in test data
		final String HIV_PROGRAM_UUID = "0a9afe04-088b-44ca-9291-0a8c3b5c96fa";
		final String MALARIA_PROGRAM_UUID = "f923524a-b90c-4870-a948-4125638606fd";

		// Check creating new
		installer.installProgram("Test Program", "Testing", HIV_PROGRAM_UUID, "prog-type-uuid");

		Program prog = Context.getProgramWorkflowService().getProgramByUuid("prog-type-uuid");
		Assert.assertThat(prog.getName(), is("Test Program"));
		Assert.assertThat(prog.getDescription(), is("Testing"));
		Assert.assertThat(prog.getConcept(), is(Context.getConceptService().getConceptByUuid(HIV_PROGRAM_UUID)));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.installProgram("New name", "New desc", MALARIA_PROGRAM_UUID, "prog-type-uuid");

		prog = Context.getProgramWorkflowService().getProgramByUuid("prog-type-uuid");
		Assert.assertThat(prog.getName(), is("New name"));
		Assert.assertThat(prog.getDescription(), is("New desc"));
		Assert.assertThat(prog.getConcept(), is(Context.getConceptService().getConceptByUuid(MALARIA_PROGRAM_UUID)));

		Context.flushSession();
		Context.clearSession();

		// Check update existing when name conflicts
		installer.installProgram("New name", "Diff desc", MALARIA_PROGRAM_UUID, "prog-type2-uuid");
		prog = Context.getProgramWorkflowService().getProgramByUuid("prog-type2-uuid");
		Assert.assertThat(prog.getName(), is("New name"));
		Assert.assertThat(prog.getDescription(), is("Diff desc"));

		prog = Context.getProgramWorkflowService().getProgramByUuid("prog-type-uuid");
		Assert.assertThat(prog, is(nullValue()));
	}

	/**
	 * @see AbstractMetadataInstaller#installVisitType(String, String, String)
	 */
	@Test
	public void installVisitType() throws Exception {
		// Check creating new
		installer.installVisitType("Test Visit", "Testing", "visit-type-uuid");

		VisitType type = Context.getVisitService().getVisitTypeByUuid("visit-type-uuid");
		Assert.assertThat(type.getName(), is("Test Visit"));
		Assert.assertThat(type.getDescription(), is("Testing"));

		Context.flushSession();
		Context.clearSession();

		// Check updating existing
		installer.installVisitType("New name", "New desc", "visit-type-uuid");

		type = Context.getVisitService().getVisitTypeByUuid("visit-type-uuid");
		Assert.assertThat(type.getName(), is("New name"));
		Assert.assertThat(type.getDescription(), is("New desc"));
	}
}