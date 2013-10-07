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

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.metadata.bundle.AbstractMetadataBundle;
import org.openmrs.module.kenyacore.metadata.bundle.MetadataBundle;
import org.openmrs.module.kenyacore.metadata.bundle.Requires;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.openmrs.module.kenyacore.metadata.bundle.CoreConstructors.*;

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
		List<MetadataBundle> bundles = new ArrayList<MetadataBundle>();
		bundles.addAll(Arrays.asList(testBundle3, testBundle2, testBundle1));

		metadataManager.installMetadataBundles(bundles);

		Assert.assertThat(Context.getUserService().getPrivilegeByUuid(uuid("priv-uuid")), is(notNullValue()));
		Assert.assertThat(Context.getUserService().getRoleByUuid(uuid("role1-uuid")), is(notNullValue()));
		Assert.assertThat(Context.getUserService().getRoleByUuid(uuid("role2-uuid")), is(notNullValue()));
		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid(uuid("enc-type-uuid")), is(notNullValue()));
		Assert.assertThat(Context.getFormService().getFormByUuid(uuid("form1-uuid")), is(notNullValue()));
		Assert.assertThat(Context.getFormService().getFormByUuid(uuid("form2-uuid")), is(notNullValue()));
	}

	/**
	 * @see MetadataManager#installMetadataBundles(java.util.List)
	 */
	@Test(expected = RuntimeException.class)
	public void processInstallers_shouldThrowExceptionIfFindBrokenRequirement() {
		List<MetadataBundle> bundles = new ArrayList<MetadataBundle>();
		bundles.addAll(Arrays.asList(testBundle1, new TestBundle4()));

		metadataManager.installMetadataBundles(bundles);
	}

	@Component
	public static class TestBundle1 extends AbstractMetadataBundle {
		@Override
		public void install() {
			install(privilege("Test Privilege", "Testing", uuid("priv-uuid")));

			install(role("Test Role 1", "Testing", null, idSet(uuid("priv-uuid")), uuid("role1-uuid")));
			install(role("Test Role 2", "Inherits from role 1", idSet(uuid("role1-uuid")), null, uuid("role2-uuid")));

			install(encounterType("Test Encounter", "Testing", uuid("enc-type-uuid")));
		}
	}

	@Component
	@Requires({ TestBundle1.class })
	public static class TestBundle2 extends AbstractMetadataBundle {
		@Override
		public void install() {
			install(form("Test Form #1", "Testing", uuid("enc-type-uuid"), "1", uuid("form1-uuid")));
		}
	}

	@Component
	@Requires({ TestBundle1.class })
	public static class TestBundle3 extends AbstractMetadataBundle {
		@Override
		public void install() {
			install(form("Test Form #2", "Testing", uuid("enc-type-uuid"), "1", uuid("form2-uuid")));
		}
	}

	/**
	 * Has broken requirement because TestBundle5 isn't instantiated as a component
	 */
	@Requires({ TestBundle5.class })
	public static class TestBundle4 extends AbstractMetadataBundle {
		@Override
		public void install() { }
	}

	public static class TestBundle5 extends AbstractMetadataBundle {
		@Override
		public void install() { }
	}

	/**
	 * Converts a simple identifier to a valid UUID (at least by our standards)
	 * @return the UUID
	 */
	protected static String uuid(String name) {
		return StringUtils.rightPad(name, 36, 'x');
	}
}