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

package org.openmrs.module.kenyacore.form;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.kenyacore.test.TestMetadata;
import org.openmrs.module.kenyacore.program.ProgramManager;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link FormManager}
 */
public class FormManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private TestMetadata testMetadata;

	@Autowired
	@Qualifier("test.app.app1")
	private AppDescriptor testApp1;

	@Autowired
	private FormManager formManager;

	@Autowired
	private ProgramManager programManager;

	@Autowired
	@Qualifier("test.form.registration")
	private FormDescriptor registrationForm;

	@Autowired
	@Qualifier("test.form.progressnote")
	private FormDescriptor progressNoteForm;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() throws Exception {
		testMetadata.install();

		programManager.refresh();
		formManager.refresh();
	}

	/**
	 * @see FormManager#getCommonFormsForPatient(org.openmrs.module.appframework.domain.AppDescriptor, org.openmrs.Patient)
	 */
	@Test
	public void getCommonFormsForPatient() {
		Assert.assertNotNull(testApp1);

		Patient patient = TestUtils.getPatient(7);

		Assert.assertThat(formManager.getCommonFormsForPatient(testApp1, patient), contains(registrationForm));
	}

	/**
	 * @see FormManager#getAllUncompletedFormsForVisit(org.openmrs.module.appframework.domain.AppDescriptor, org.openmrs.Visit)
	 */
	@Test
	public void getAllUncompletedFormsForVisit() {
		Assert.assertNotNull(testApp1);

		Patient patient = TestUtils.getPatient(7);
		VisitType initialHiv = MetadataUtils.existing(VisitType.class, StandardTestData._VisitType.INITIAL_HIV);
		Visit visit = TestUtils.saveVisit(patient, initialHiv, TestUtils.date(2012, 1, 1, 9, 0, 0), TestUtils.date(2012, 1, 1, 11, 0, 0));

		Assert.assertThat(formManager.getAllUncompletedFormsForVisit(testApp1, visit), contains(progressNoteForm));
	}
}