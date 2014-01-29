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

package org.openmrs.module.kenyacore.chore;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.Collections;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ChoreManager}
 */
public class ChoreManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private TestChore2 testChore2;

	@Autowired
	private ChoreManager updateManager;

	@Autowired
	private AdministrationService adminService;

	/**
	 * @see ChoreManager#performChores(java.util.Collection)
	 */
	@Test
	public void runUpdates_shouldRunUpdateAndItsDependencies() {
		Assert.assertThat(adminService.getGlobalProperty("test.chore1.done"), nullValue());
		Assert.assertThat(adminService.getGlobalProperty("test.chore2.done"), nullValue());

		updateManager.performChores(Collections.<Chore>singleton(testChore2));

		Assert.assertThat(adminService.getGlobalProperty("test.chore1.done"), is("true"));
		Assert.assertThat(adminService.getGlobalProperty("test.chore2.done"), is("true"));

		Assert.assertThat(Context.getPatientService().getPatient(6).isVoided(), is(true));
	}

	/**
	 * Chore component for testing which voids patient 6
	 */
	@Component("test.chore1")
	public static class TestChore1 extends AbstractChore {

		@Override
		public void perform(PrintWriter output) throws Exception {
			Context.getPatientService().voidPatient(TestUtils.getPatient(6), "Testing");

		}
	}

	/**
	 * Chore component for testing
	 */
	@Component("test.chore2")
	@Requires(TestChore1.class)
	public static class TestChore2 extends AbstractChore {

		@Override
		public void perform(PrintWriter output) throws Exception {
		}
	}
}