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

package org.openmrs.module.kenyacore.report.cohort.evaluator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.parameter.ParameterDefinitionSet;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.report.cohort.definition.DateCalculationCohortDefinition;
import org.openmrs.module.kenyacore.test.ReportingTestUtils;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Tests for {@link org.openmrs.module.kenyacore.report.cohort.evaluator.DateCalculationCohortDefinitionEvaluator}
 */
public class DateCalculationCohortDefinitionEvaluatorTest extends BaseModuleContextSensitiveTest {

	private EvaluationContext context;

	private DateCalculationCohortDefinitionEvaluator evaluator;

	@Before
	public void setup() throws Exception {
		context = ReportingTestUtils.reportingContext(Arrays.asList(2, 6, 7, 8), TestUtils.date(2012, 1, 1), TestUtils.date(2012, 1, 31));
		evaluator = new DateCalculationCohortDefinitionEvaluator();
	}

	/**
	 * Tests evaluation of the initialArtStartDate calculation
	 */
	@Test
	public void evaluate_dateCalculation() throws EvaluationException {
		DateCalculationCohortDefinition cohortDefinition = new DateCalculationCohortDefinition(new TestDateCalculation());
		cohortDefinition.setOnOrAfter(TestUtils.date(2012, 1, 6));
		cohortDefinition.setOnOrBefore(TestUtils.date(2012, 1, 7));

		EvaluatedCohort evaluated = evaluator.evaluate(cohortDefinition, context);

		ReportingTestUtils.assertCohortEquals(Arrays.asList(6, 7), evaluated);
	}

	/**
	 * Test calculation which returns a date for each patient based on their id, i.e. 2012-01-{id}
	 */
	public class TestDateCalculation implements PatientCalculation {

		@Override
		public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
			CalculationResultMap ret = new CalculationResultMap();
			for (Integer ptId :  cohort) {
				ret.put(ptId, new SimpleResult(TestUtils.date(2012, 1, ptId), this, context));
			}
			return ret;
		}

		@Override
		public ParameterDefinitionSet getParameterDefinitionSet() {
			return null;
		}
	}
}
