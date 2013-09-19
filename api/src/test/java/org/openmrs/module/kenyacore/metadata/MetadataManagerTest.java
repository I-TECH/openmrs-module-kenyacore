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
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link org.openmrs.module.kenyacore.metadata.MetadataManager}
 */
public class MetadataManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private MetadataManager metadataManager;

	@Autowired
	private TestInstaller1 testInstaller1;

	@Autowired
	private TestInstaller2 testInstaller2;

	@Autowired
	private TestInstaller3 testInstaller3;

	/**
	 * @see MetadataManager#ensurePackageInstalled(String, String, ClassLoader)
	 */
	@Test
	public void ensurePackageInstalled_shouldInstallPackagesOnlyIfNecessary() throws Exception {

		// Test package contains visit type { name: "Outpatient", uuid: "3371a4d4-f66f-4454-a86d-92c7b3da990c" }
		final String TEST_PACKAGE_GROUP_UUID = "5c7fd8e7-e9a5-43a2-8ba5-c7694fc8db4a";
		final String TEST_PACKAGE_FILENAME = "test-package-1.zip";

		try {
			// Check data isn't there
			MetadataUtils.getVisitType("3371a4d4-f66f-4454-a86d-92c7b3da990c");
			Assert.fail();
		}
		catch (IllegalArgumentException ex) {
		}

		// Simulate first time startup
		Assert.assertThat(metadataManager.ensurePackageInstalled(TEST_PACKAGE_GROUP_UUID, TEST_PACKAGE_FILENAME, null), is(true));
		Assert.assertThat(MetadataUtils.getVisitType("3371a4d4-f66f-4454-a86d-92c7b3da990c"), is(notNullValue()));

		// Simulate starting a second time
		Assert.assertThat(metadataManager.ensurePackageInstalled(TEST_PACKAGE_GROUP_UUID, TEST_PACKAGE_FILENAME, null), is(false));
		Assert.assertThat(MetadataUtils.getVisitType("3371a4d4-f66f-4454-a86d-92c7b3da990c"), is(notNullValue()));
	}

	/**
	 * @see MetadataManager#processInstallers(java.util.List)
	 */
	@Test
	public void processInstallers() {
		metadataManager.processInstallers(Arrays.asList(testInstaller3, testInstaller2, testInstaller1));

		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid("enc-type-uuid"), is(notNullValue()));
		Assert.assertThat(Context.getFormService().getFormByUuid("form1-uuid"), is(notNullValue()));
		Assert.assertThat(Context.getFormService().getFormByUuid("form2-uuid"), is(notNullValue()));
	}

	@Component("test.installer.1")
	public static class TestInstaller1 extends AbstractMetadataInstaller {
		@Override
		public void install() {
			installEncounterType("Test Encounter", "Testing", "enc-type-uuid");
		}
	}

	@Component("test.installer.2")
	@Requires({ "test.installer.1" })
	public static class TestInstaller2 extends AbstractMetadataInstaller {
		@Override
		public void install() {
			installForm("Test Form #1", "Testing", "enc-type-uuid", "1", "form1-uuid");
		}
	}

	@Component("test.installer.3")
	@Requires({ "test.installer.1" })
	public static class TestInstaller3 extends AbstractMetadataInstaller {
		@Override
		public void install() {
			installForm("Test Form #2", "Testing", "enc-type-uuid", "1", "form2-uuid");
		}
	}
}