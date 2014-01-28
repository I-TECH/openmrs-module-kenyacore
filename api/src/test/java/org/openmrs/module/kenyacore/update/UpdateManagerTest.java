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

package org.openmrs.module.kenyacore.update;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link UpdateManager}
 */
public class UpdateManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private UpdateManager updateManager;

	@Autowired
	private AdministrationService adminService;

	@Test
	public void integration() {
		Assert.assertThat(adminService.getGlobalProperty("update." + TestUpdate.class.getName() + ".ran"), nullValue());

		updateManager.refresh();

		Assert.assertThat(adminService.getGlobalProperty("update." + TestUpdate.class.getName() + ".ran"), is("true"));
		Assert.assertThat(Context.getPatientService().getPatient(6).isVoided(), is(true));
	}

	/**
	 * Update component for testing which voids patient 6
	 */
	@Component
	public static class TestUpdate implements MaintenanceUpdate {

		@Override
		public void run(PrintWriter output) throws Exception {
			Context.getPatientService().voidPatient(TestUtils.getPatient(6), "Testing");
		}
	}
}