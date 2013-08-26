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
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.kenyacore.program.ProgramManager;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link FormManager}
 */
public class FormManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	@Qualifier("test.app.app1")
	private AppDescriptor testApp1;

	@Autowired
	private FormManager formManager;

	@Autowired
	private ProgramManager programManager;

	@Autowired
	@Qualifier("test.form.basic")
	private FormDescriptor basicForm;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() throws Exception {
		programManager.refresh();
		formManager.refresh();
	}

	@Test
	public void getFormsForPatient() {
		Assert.assertNotNull(testApp1);

		Patient patient = TestUtils.getPatient(7);

		Assert.assertThat(formManager.getFormsForPatient(testApp1, patient), contains(basicForm));
	}
}