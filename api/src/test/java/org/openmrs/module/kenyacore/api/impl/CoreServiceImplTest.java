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

package org.openmrs.module.kenyacore.api.impl;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.kenyacore.api.CoreService;
import org.openmrs.module.kenyacore.chore.AbstractChore;
import org.openmrs.module.kenyacore.chore.Requires;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link CoreServiceImpl}
 */
public class CoreServiceImplTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private CoreService coreService;

	@Autowired
	private AdministrationService adminService;

	@Autowired
	private TestChore testChore;

	/**
	 * @see CoreServiceImpl#performChore(org.openmrs.module.kenyacore.chore.Chore)
	 */
	@Test
	public void performChore() {
		coreService.performChore(testChore);

		Assert.assertThat(testChore.done, is(true));
		Assert.assertThat(adminService.getGlobalProperty("test.chore.done"), is("true"));
	}

	/**
	 * Chore component for testing
	 */
	@Component("test.chore")
	public static class TestChore extends AbstractChore {

		public boolean done = false;

		@Override
		public void perform(PrintWriter output) {
			done = true;
		}
	}
}