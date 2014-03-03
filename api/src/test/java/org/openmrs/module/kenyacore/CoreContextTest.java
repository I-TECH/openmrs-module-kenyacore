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

package org.openmrs.module.kenyacore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.kenyacore.form.FormManager;
import org.openmrs.module.kenyacore.test.TestMetadata;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CoreContext}
 */
public class CoreContextTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private TestMetadata testMetadata;

	@Autowired
	private CoreContext core;

	@Autowired
	private FormManager formManager;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		testMetadata.install();

		core.refresh();
	}

	@Test
	public void getInstance_shouldReturnSingletonInstance() {
		Assert.assertThat(core, is(CoreContext.getInstance()));
	}

	@Test
	public void getManager_shouldReturnManagerInstance() {
		FormManager fromMethod = core.getManager(FormManager.class);
		Assert.assertThat(fromMethod, is(formManager));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getManager_shouldThrowExceptionForInvalidManagerClass() {
		core.getManager(TestInvalidManager.class);
	}

	@Test
	public void isRefreshed_shouldReturnTrue() {
		// We don't currently test case when refresh fails
		Assert.assertThat(core.isRefreshed(), is(true));
	}

	@Test
	public void getSingletonComponent_shouldReturnComponent() {
		FormManager fromMethod = CoreContext.getSingletonComponent(FormManager.class);
		Assert.assertThat(fromMethod, is(formManager));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getSingletonComponent_shouldThrowExceptionIfNoComponentExists() {
		CoreContext.getSingletonComponent(Integer.class);
	}

	private class TestInvalidManager implements ContentManager {

		@Override
		public int getPriority() {
			return 123;
		}

		@Override
		public void refresh() {}
	}
}