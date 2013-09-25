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
import org.openmrs.module.kenyacore.metadata.bundle.AbstractMetadataBundle;
import org.openmrs.module.kenyacore.metadata.bundle.Requires;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.openmrs.module.kenyacore.metadata.bundle.Constructors.*;

/**
 * Tests for {@link org.openmrs.module.kenyacore.metadata.MetadataManager}
 */
public class MetadataManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private MetadataManager metadataManager;

	@Autowired
	private TestBundle1 testBundle1;

	@Autowired
	private TestBundle2 testBundle2;

	@Autowired
	private TestBundle3 testBundle3;

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
	 * @see MetadataManager#installMetadataBundles(java.util.List)
	 */
	@Test
	public void installMetadataBundles() {
		metadataManager.installMetadataBundles(Arrays.asList(testBundle3, testBundle2, testBundle1));

		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid("enc-type-uuid"), is(notNullValue()));
		Assert.assertThat(Context.getFormService().getFormByUuid("form1-uuid"), is(notNullValue()));
		Assert.assertThat(Context.getFormService().getFormByUuid("form2-uuid"), is(notNullValue()));
	}

	/**
	 * @see MetadataManager#installMetadataBundles(java.util.List)
	 */
	@Test(expected = RuntimeException.class)
	public void processInstallers_shouldThrowExceptionIfFindBrokenRequirement() {
		metadataManager.installMetadataBundles(Arrays.asList(testBundle1, new TestBundle4()));
	}

	@Component("test.bundle.1")
	public static class TestBundle1 extends AbstractMetadataBundle {
		@Override
		public void install() {
			install(encounterType("Test Encounter", "Testing", "enc-type-uuid"));
		}
	}

	@Component
	@Requires({ "test.bundle.1" })
	public static class TestBundle2 extends AbstractMetadataBundle {
		@Override
		public void install() {
			install(form("Test Form #1", "Testing", "enc-type-uuid", "1", "form1-uuid"));
		}
	}

	@Component
	@Requires({ "test.bundle.1" })
	public static class TestBundle3 extends AbstractMetadataBundle {
		@Override
		public void install() {
			install(form("Test Form #2", "Testing", "enc-type-uuid", "1", "form2-uuid"));
		}
	}

	/**
	 * Has broken requirement
	 */
	@Requires({ "test.bundle.xxx" })
	public static class TestBundle4 extends AbstractMetadataBundle {
		@Override
		public void install() { }
	}
}