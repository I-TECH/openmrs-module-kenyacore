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
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Filters}
 */
public class FiltersTest extends BaseModuleContextSensitiveTest {

	private List<Integer> cohort = Arrays.asList(2, 6, 7, 8);

	private PatientCalculationContext context;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		context = Context.getService(PatientCalculationService.class).createCalculationContext();
		context.setNow(TestUtils.date(2012, 6, 1));
	}

	@Test
	public void integration() {
		new Filters();
	}

	/**
	 * @see Filters#alive(java.util.Collection, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Test
	public void alive() {
		TestUtils.getPatient(6).setDead(true);
		TestUtils.getPatient(6).setDeathDate(TestUtils.date(2012, 1, 1));

		Assert.assertThat(Filters.alive(cohort, context), contains(2, 7, 8));
	}

	/**
	 * @see Filters#female(java.util.Collection, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Test
	public void female() {
		Assert.assertThat(Filters.female(cohort, context), contains(7, 8));
	}

	/**
	 * @see Filters#inProgram(org.openmrs.Program, java.util.Collection, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Test
	public void inProgram() {
		Program hiv = MetadataUtils.existing(Program.class, StandardTestData._Program.HIV);

		// Patient #2 is enrolled in standardTesDataset.xml, we enroll #6 and #7 here...
		TestUtils.enrollInProgram(TestUtils.getPatient(6), hiv, TestUtils.date(2012, 1, 1), TestUtils.date(2012, 5, 1));
		TestUtils.enrollInProgram(TestUtils.getPatient(7), hiv, TestUtils.date(2012, 1, 1));

		Assert.assertThat(Filters.inProgram(hiv, cohort, context), contains(2, 7));
	}
}