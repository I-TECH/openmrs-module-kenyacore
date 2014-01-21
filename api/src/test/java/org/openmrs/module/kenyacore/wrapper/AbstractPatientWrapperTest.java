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

package org.openmrs.module.kenyacore.wrapper;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractPersonWrapper}
 */
public class AbstractPatientWrapperTest extends BaseModuleContextSensitiveTest {

	/**
	 * @see AbstractPatientWrapper#lastEncounter(org.openmrs.EncounterType)
	 */
	@Test
	public void lastEncounter_shouldFindLastEncounterWithType() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		EncounterType emergency = MetadataUtils.getEncounterType(StandardTestData._EncounterType.EMERGENCY);
		EncounterType scheduled = MetadataUtils.getEncounterType(StandardTestData._EncounterType.SCHEDULED);

		// Test with no saved encounters
		Assert.assertThat(wrapper.lastEncounter(emergency), nullValue());

		Encounter enc1 = TestUtils.saveEncounter(wrapper.getTarget(), emergency, TestUtils.date(2012, 3, 1));
		Encounter enc2 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 2, 1));
		Encounter enc3 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 1, 1));

		Assert.assertThat(wrapper.lastEncounter(scheduled), is(enc2));
	}

	/**
	 * Patient wrapper class for testing
	 */
	private static class PatientWrapper extends AbstractPatientWrapper {

		public PatientWrapper(Patient target) {
			super(target);
		}
	}
}