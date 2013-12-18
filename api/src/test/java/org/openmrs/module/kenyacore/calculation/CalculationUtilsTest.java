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
import org.openmrs.calculation.ConfigurableCalculation;
import org.openmrs.calculation.InvalidCalculationException;
import org.openmrs.calculation.parameter.ParameterDefinitionSet;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CalculationUtils}
 */
public class CalculationUtilsTest {

	@Test
	public void integration() {
		new CalculationUtils();
	}

	/**
	 * @see CalculationUtils#instantiateCalculation(Class, String)
	 */
	@Test
	public void instantiateCalculation() {
		// Without configuration
		Assert.assertNotNull(CalculationUtils.instantiateCalculation(TestCalculation.class, null));

		// With configuration
		TestCalculation calc = (TestCalculation) CalculationUtils.instantiateCalculation(TestCalculation.class, "test=123");
		Assert.assertThat(calc, is(notNullValue()));
		Assert.assertThat(calc.configuration, is("test=123"));
	}

	/**
	 * @see CalculationUtils#instantiateCalculation(Class, String)
	 */
	@Test(expected = RuntimeException.class)
	public void instantiateCalculation_shouldThrowExceptionIfClassCantBeInstantiated() {
		CalculationUtils.instantiateCalculation(FaultyCalculation.class, null);
	}

	/**
	 * @see CalculationUtils#extractResultValues(org.openmrs.calculation.result.ListResult)
	 */
	@Test
	public void extractResultValues_shouldExtractListResultValues() {
		ListResult ints = new ListResult();
		ints.add(new SimpleResult(100, null));
		ints.add(new SimpleResult(200, null));

		List<Integer> intValues = CalculationUtils.extractResultValues(ints);
		Assert.assertThat(intValues, contains(100, 200));

		ListResult strings = new ListResult();
		strings.add(new SimpleResult("a", null));
		strings.add(new SimpleResult("b", null));

		List<String> stringValues = CalculationUtils.extractResultValues(strings);
		Assert.assertThat(stringValues, contains("a", "b"));
	}

	/**
	 * Dummy patient calculation class for testing
	 */
	public static class TestCalculation implements PatientCalculation, ConfigurableCalculation {

		protected String configuration;

		@Override
		public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
			return new CalculationResultMap();
		}

		@Override
		public ParameterDefinitionSet getParameterDefinitionSet() {
			return null;
		}

		@Override
		public void setConfiguration(String configuration) throws InvalidCalculationException {
			this.configuration = configuration;
		}
	}

	/**
	 * Dummy patient calculation class that can't be instantiated
	 */
	public static class FaultyCalculation implements PatientCalculation {

		public FaultyCalculation() {
			throw new RuntimeException("This class can't be instantiated");

		}
		@Override
		public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
			return new CalculationResultMap();
		}

		@Override
		public ParameterDefinitionSet getParameterDefinitionSet() {
			return null;
		}
	}
}