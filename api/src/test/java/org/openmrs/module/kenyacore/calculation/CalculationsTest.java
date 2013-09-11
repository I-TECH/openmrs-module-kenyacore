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

package org.openmrs.module.kenyacore.calculation;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Calculations}
 */
public class CalculationsTest extends BaseModuleContextSensitiveTest {

	/**
	 * @see Calculations#allEncounters(org.openmrs.EncounterType, java.util.Collection, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Test
	public void allEncounters_shouldReturnAllEncountersForPatients() throws Exception {
		List<Integer> cohort = Arrays.asList(6, 7);

		// Get total encounters
		CalculationResultMap resultMap = Calculations.allEncounters(null, cohort, Context.getService(PatientCalculationService.class).createCalculationContext());

		Assert.assertThat(((Collection) resultMap.get(6).getValue()).size(), is(0)); // patient #6 has no encounters
		Assert.assertThat(((Collection) resultMap.get(7).getValue()).size(), is(3)); // patient #7 has 3 encounters

		// Get 'Scheduled' encounters
		EncounterType scheduledEncType = Context.getEncounterService().getEncounterType("Scheduled");
		resultMap = Calculations.allEncounters(scheduledEncType, cohort, Context.getService(PatientCalculationService.class).createCalculationContext());

		Assert.assertThat(((Collection) resultMap.get(6).getValue()).size(), is(0)); // patient #6 has no encounters of type 'Scheduled'
		Assert.assertThat(((Collection) resultMap.get(7).getValue()).size(), is(2)); // patient #7 has 2 encounters of type 'Scheduled'
	}

	/**
	 * @see Calculations#lastEncounter(org.openmrs.EncounterType, java.util.Collection, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Test
	public void lastEncounter_shouldReturnLastEncountersForPatients() throws Exception {
		List<Integer> cohort = Arrays.asList(6, 7);

		// Get last encounter
		CalculationResultMap resultMap = Calculations.lastEncounter(null, cohort, Context.getService(PatientCalculationService.class).createCalculationContext());

		Assert.assertThat(resultMap.get(6), is(nullValue())); // patient #6 has no encounters
		Assert.assertThat(resultMap.get(7), is(notNullValue())); // patient #7 has an encounter
		Assert.assertThat(((Encounter) resultMap.get(7).getValue()).getEncounterDatetime(), is(TestUtils.date(2008, 8, 19)));

		// Get last 'Emergency' encounter
		EncounterType emergencyEncType = Context.getEncounterService().getEncounterType("Emergency");
		resultMap = Calculations.lastEncounter(emergencyEncType, cohort, Context.getService(PatientCalculationService.class).createCalculationContext());

		Assert.assertThat(resultMap.get(6), is(nullValue())); // patient #6 has no encounters
		Assert.assertThat(resultMap.get(7), is(notNullValue())); // patient #7 has an emergency encounter on 1-Aug-2008
		Assert.assertThat(((Encounter) resultMap.get(7).getValue()).getEncounterDatetime(), is(TestUtils.date(2008, 8, 1)));
	}
}