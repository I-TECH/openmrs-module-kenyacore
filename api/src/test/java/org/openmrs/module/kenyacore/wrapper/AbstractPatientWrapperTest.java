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
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
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
	 * @see AbstractPatientWrapper#allEncounters(org.openmrs.EncounterType)
	 */
	@Test
	public void allEncounters_shouldFindAllEncountersWithType() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		EncounterType emergency = MetadataUtils.existing(EncounterType.class, StandardTestData._EncounterType.EMERGENCY);
		EncounterType scheduled = MetadataUtils.existing(EncounterType.class, StandardTestData._EncounterType.SCHEDULED);

		// Test with no saved encounters
		Assert.assertThat(wrapper.allEncounters(emergency), hasSize(0));

		Encounter enc1 = TestUtils.saveEncounter(wrapper.getTarget(), emergency, TestUtils.date(2012, 3, 1));
		Encounter enc2 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 2, 1));
		Encounter enc3 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 1, 1));

		Assert.assertThat(wrapper.allEncounters(scheduled), contains(enc3, enc2));
	}

	/**
	 * @see AbstractPatientWrapper#allEncounters(org.openmrs.Form)
	 */
	@Test
	public void allEncounters_shouldFindAllEncountersWithForm() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		Form basic = MetadataUtils.existing(Form.class, StandardTestData._Form.BASIC);

		// Test with no saved encounters
		Assert.assertThat(wrapper.allEncounters(basic), hasSize(0));

		Encounter enc1 = TestUtils.saveEncounter(wrapper.getTarget(), basic, TestUtils.date(2012, 3, 1));
		Encounter enc2 = TestUtils.saveEncounter(wrapper.getTarget(), basic, TestUtils.date(2012, 4, 1));

		Assert.assertThat(wrapper.allEncounters(basic), contains(enc1, enc2));
	}

	/**
	 * @see AbstractPatientWrapper#firstEncounter(org.openmrs.EncounterType)
	 */
	@Test
	public void firstEncounter_shouldFindFirstEncounterWithType() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		EncounterType emergency = MetadataUtils.existing(EncounterType.class, StandardTestData._EncounterType.EMERGENCY);
		EncounterType scheduled = MetadataUtils.existing(EncounterType.class, StandardTestData._EncounterType.SCHEDULED);

		// Test with no saved encounters
		Assert.assertThat(wrapper.firstEncounter(emergency), nullValue());

		Encounter enc1 = TestUtils.saveEncounter(wrapper.getTarget(), emergency, TestUtils.date(2012, 1, 1));
		Encounter enc2 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 2, 1));
		Encounter enc3 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 3, 1));

		Assert.assertThat(wrapper.firstEncounter(scheduled), is(enc2));
	}

	/**
	 * @see AbstractPatientWrapper#lastEncounter(org.openmrs.EncounterType)
	 */
	@Test
	public void lastEncounter_shouldFindLastEncounterWithType() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		EncounterType emergency = MetadataUtils.existing(EncounterType.class, StandardTestData._EncounterType.EMERGENCY);
		EncounterType scheduled = MetadataUtils.existing(EncounterType.class, StandardTestData._EncounterType.SCHEDULED);

		// Test with no saved encounters
		Assert.assertThat(wrapper.lastEncounter(emergency), nullValue());

		Encounter enc1 = TestUtils.saveEncounter(wrapper.getTarget(), emergency, TestUtils.date(2012, 3, 1));
		Encounter enc2 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 2, 1));
		Encounter enc3 = TestUtils.saveEncounter(wrapper.getTarget(), scheduled, TestUtils.date(2012, 1, 1));

		Assert.assertThat(wrapper.lastEncounter(scheduled), is(enc2));
	}

	/**
	 * @see AbstractPatientWrapper#getAsIdentifier(String)
	 */
	@Test
	public void getAsIdentifier_shouldGetIdentifierIfExists() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		Assert.assertThat(wrapper.getAsIdentifier(StandardTestData._PatientIdentifierType.OPENMRS), nullValue());
		Assert.assertThat(wrapper.getAsIdentifier(StandardTestData._PatientIdentifierType.OLD), is("12345K"));
	}

	/**
	 * @see AbstractPatientWrapper#setAsIdentifier(String, String, org.openmrs.Location)
	 */
	@Test
	public void setAsIdentifier_settingBlankValueShouldVoidIdentifier() {
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));

		PatientIdentifier oldId = wrapper.getTarget().getPatientIdentifier(MetadataUtils.existing(PatientIdentifierType.class, StandardTestData._PatientIdentifierType.OLD));

		PatientIdentifier pt1 = wrapper.setAsIdentifier(StandardTestData._PatientIdentifierType.OPENMRS, null, null); // Should do nothing
		PatientIdentifier pt2 = wrapper.setAsIdentifier(StandardTestData._PatientIdentifierType.OLD, null, null); // Should void existing

		Assert.assertThat(pt1, nullValue());
		Assert.assertThat(pt2, notNullValue());

		Context.getPatientService().savePatientIdentifier(pt2);

		Assert.assertThat(oldId.isVoided(), is(true));
		Assert.assertThat(oldId.getDateVoided(), notNullValue());
		Assert.assertThat(oldId.getVoidedBy(), notNullValue());
		Assert.assertThat(oldId.getVoidReason(), notNullValue());
	}

	/**
	 * @see AbstractPatientWrapper#setAsIdentifier(String, String, org.openmrs.Location)
	 */
	@Test
	public void setIdentifier_shouldUpdateOrCreateIdentifier() {
		Location xanadu = MetadataUtils.existing(Location.class, StandardTestData._Location.XANADU);
		PatientWrapper wrapper = new PatientWrapper(TestUtils.getPatient(6));
		PatientIdentifierType openmrsIdType = MetadataUtils.existing(PatientIdentifierType.class, StandardTestData._PatientIdentifierType.OPENMRS);
		PatientIdentifierType oldIdType = MetadataUtils.existing(PatientIdentifierType.class, StandardTestData._PatientIdentifierType.OLD);

		PatientIdentifier oldId = wrapper.getTarget().getPatientIdentifier(oldIdType);

		PatientIdentifier pt1 = wrapper.setAsIdentifier(StandardTestData._PatientIdentifierType.OPENMRS, "123456-G", xanadu);
		PatientIdentifier pt2 = wrapper.setAsIdentifier(StandardTestData._PatientIdentifierType.OLD, "54321M", xanadu);

		Assert.assertThat(pt1, notNullValue());
		Assert.assertThat(pt2, notNullValue());

		Context.getPatientService().savePatientIdentifier(pt1);
		Context.getPatientService().savePatientIdentifier(pt2);

		Assert.assertThat(wrapper.getTarget().getPatientIdentifier(oldIdType), is(oldId)); // Check same identifier object
		Assert.assertThat(wrapper.getTarget().getPatientIdentifier(oldIdType).getIdentifier(), is("54321M"));
		Assert.assertThat(wrapper.getTarget().getPatientIdentifier(openmrsIdType).getIdentifier(), is("123456-G"));

		Assert.assertThat(wrapper.getTarget().getIdentifiers(), hasSize(2));
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