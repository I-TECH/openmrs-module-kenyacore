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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.module.ModuleClassLoader;
import org.openmrs.module.ModuleFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractContentConfiguration}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ModuleFactory.class)
public class AbstractContentConfigurationTest {

	@Test
	public void getClassLoader_shouldGetModuleLoaderIfModuleSpecified() throws Exception {

		PowerMockito.mockStatic(ModuleFactory.class);

		ModuleClassLoader testClassLoader = Mockito.mock(ModuleClassLoader.class);
		Mockito.when(ModuleFactory.getModuleClassLoader("kenyacore")).thenReturn(testClassLoader);

		// Test with a moduleId - should return the module class loader
		TestContentConfiguration config = new TestContentConfiguration();
		config.setModuleId("kenyacore");
		Assert.assertThat(config.getModuleId(), is("kenyacore"));
		Assert.assertThat(config.getClassLoader(), is((ClassLoader) testClassLoader));
	}

	@Test
	public void getClassLoader_shouldGetSystemLoaderIfNoModuleSpecified() {
		TestContentConfiguration config = new TestContentConfiguration();
		Assert.assertThat(config.getModuleId(), is(nullValue()));
		Assert.assertThat(config.getClassLoader(), is(getClass().getClassLoader()));
	}

	private class TestContentConfiguration extends AbstractContentConfiguration {
	}
}