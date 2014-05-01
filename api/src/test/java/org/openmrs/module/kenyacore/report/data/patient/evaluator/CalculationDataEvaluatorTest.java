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

package org.openmrs.module.kenyacore.report.data.patient.evaluator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.calculation.parameter.ParameterDefinitionSet;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.test.ReportingTestUtils;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link org.openmrs.module.kenyacore.report.data.patient.evaluator.CalculationDataEvaluator}
 */
public class CalculationDataEvaluatorTest extends BaseModuleContextSensitiveTest {

	private EvaluationContext context;

	private CalculationDataEvaluator evaluator;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() throws Exception {
		context = ReportingTestUtils.reportingContext(Arrays.asList(2, 6, 7, 8, 999), TestUtils.date(2012, 1, 1), TestUtils.date(2012, 1, 31));
		evaluator = new CalculationDataEvaluator();
	}

	/**
	 * @see CalculationDataEvaluator#evaluate(org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition, org.openmrs.module.reporting.evaluation.EvaluationContext)
	 */
	@Test
	public void evaluate() throws EvaluationException {
		TestCalculation calculation = new TestCalculation();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("test", 123);

		CalculationDataDefinition def = new CalculationDataDefinition("test", calculation);
		def.setCalculationParameters(params);

		EvaluatedPatientData data = evaluator.evaluate(def, context);
		Assert.assertThat((Integer)((CalculationResult) data.getData().get(2)).getValue(), is(123));
		Assert.assertThat((Integer)((CalculationResult) data.getData().get(6)).getValue(), is(123));
	}

	/**
	 * Test calculation which returns the value of the test parameter for each patient
	 */
	public class TestCalculation implements PatientCalculation {

		@Override
		public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
			CalculationResultMap ret = new CalculationResultMap();
			for (Integer ptId :  cohort) {
				ret.put(ptId, new SimpleResult(params.get("test"), this, context));
			}
			return ret;
		}

		@Override
		public ParameterDefinitionSet getParameterDefinitionSet() {
			return null;
		}
	}
}