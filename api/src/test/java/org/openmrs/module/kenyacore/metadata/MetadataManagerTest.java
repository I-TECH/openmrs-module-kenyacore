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
import org.openmrs.module.kenyacore.test.TestMetadata;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link MetadataManager}
 */
public class MetadataManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private MetadataManager metadataManager;

	/**
	 * @see MetadataManager#refresh()
	 */
	@Test
	public void refresh_shouldInstallAnyBundleComponents() {
		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid(TestMetadata._EncounterType.CONSULTATION), nullValue());

		metadataManager.refresh();

		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid(TestMetadata._EncounterType.CONSULTATION), notNullValue());
	}

	/**
	 * @see MetadataManager#refresh()
	 */
	@Test
	public void refresh_shouldNotInstallWhenSkipPropertyIsTrue() {
		System.setProperty("skipMetadataRefresh", "true");

		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid(TestMetadata._EncounterType.CONSULTATION), nullValue());

		metadataManager.refresh();

		Assert.assertThat(Context.getEncounterService().getEncounterTypeByUuid(TestMetadata._EncounterType.CONSULTATION), nullValue());
	}

}