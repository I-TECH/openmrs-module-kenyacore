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
import org.openmrs.api.APIException;
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
	private TestChore1 testChore1;

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
	public void performChores_shouldPerformChoresAndItsDependencies() {
		updateManager.performChores(Collections.<Chore>singleton(testChore2));

		Assert.assertThat(adminService.getGlobalProperty("test.chore1.done"), is("true"));
		Assert.assertThat(adminService.getGlobalProperty("test.chore2.done"), is("true"));

		Assert.assertThat(Context.getPatientService().getPatient(6).isVoided(), is(true));
	}

	/**
	 * @see ChoreManager#performChores(java.util.Collection)
	 */
	@Test(expected = APIException.class)
	public void performChores_shouldThrowAPIExceptionIfChoreThrowsException() {
		updateManager.performChores(Collections.<Chore>singleton(new TestChore3()));
	}

	/**
	 * @see ChoreManager#performChores(java.util.Collection)
	 */
	@Test
	public void performChores_shouldNotMarkChoreAsRunIfItThrowsException() {
		try {
			updateManager.performChores(Collections.<Chore>singleton(new TestChore3()));
		}
		catch (Exception ex) {}

		Assert.assertThat(adminService.getGlobalProperty("test.chore3.done"), nullValue());
	}

	/**
	 * @see ChoreManager#isChorePerformed(Chore)
	 */
	@Test
	public void isChorePerformed_shouldGetIfChoreHasBeenPerfomed() {
		Assert.assertThat(updateManager.isChorePerformed(testChore1), is(false));
		Assert.assertThat(updateManager.isChorePerformed(testChore2), is(false));

		updateManager.performChores(Collections.<Chore>singleton(testChore2));

		Assert.assertThat(updateManager.isChorePerformed(testChore1), is(true));
		Assert.assertThat(updateManager.isChorePerformed(testChore2), is(true));
	}

	/**
	 * Chore component for testing which voids patient 6
	 */
	@Component("test.chore1")
	public static class TestChore1 extends AbstractChore {

		@Override
		public void perform(PrintWriter output) {
			Context.getPatientService().voidPatient(TestUtils.getPatient(6), "Testing");

			// Fail if chore has already been performed - in which we shouldn't be in here
			String gp = Context.getAdministrationService().getGlobalProperty("test.chore1.done");
			if (gp != null && gp.equals("true")) {
				Assert.fail();
			}
		}
	}

	/**
	 * Chore component for testing
	 */
	@Component("test.chore2")
	@Requires(TestChore1.class)
	public static class TestChore2 extends AbstractChore {

		@Override
		public void perform(PrintWriter output) {
		}
	}

	/**
	 * Chore for testing which throws exception. Not a component so that it doesn't break ChoreManager.refresh
	 */
	public static class TestChore3 extends AbstractChore {

		public TestChore3() {
			setId("test.chore3");
		}

		@Override
		public void perform(PrintWriter output) {
			throw new NullPointerException();
		}
	}
}