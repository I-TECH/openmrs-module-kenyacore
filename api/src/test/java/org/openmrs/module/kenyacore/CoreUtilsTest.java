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

package org.openmrs.module.kenyacore;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.calculation.ConfigurableCalculation;
import org.openmrs.calculation.InvalidCalculationException;
import org.openmrs.calculation.parameter.ParameterDefinitionSet;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CoreUtils}
 */
public class CoreUtilsTest {

	@Test
	public void integration() {
		new CoreUtils();
	}

	/**
	 * @see CoreUtils#merge(java.util.Collection[])
	 */
	@Test
	public void merge_shouldMergeByNaturalOrder() {
		List<Integer> list1 = Arrays.asList(2, 5, 9);
		List<Integer> list2 = Arrays.asList(1, 3, 10);

		Assert.assertThat(CoreUtils.merge(list1, list2), contains(1, 2, 3, 5, 9, 10));
	}

	/**
	 * @see CoreUtils#instantiateCalculation(Class, String)
	 */
	@Test
	public void instantiateCalculation() {
		// Without configuration
		Assert.assertNotNull(CoreUtils.instantiateCalculation(TestCalculation.class, null));

		// With configuration
		TestCalculation calc = (TestCalculation) CoreUtils.instantiateCalculation(TestCalculation.class, "test=123");
		Assert.assertThat(calc, is(notNullValue()));
		Assert.assertThat(calc.configuration, is("test=123"));
	}

	/**
	 * @see CoreUtils#instantiateCalculation(Class, String)
	 */
	@Test(expected = RuntimeException.class)
	public void instantiateCalculation_shouldThrowExceptionIfClassCantBeInstantiated() {
		CoreUtils.instantiateCalculation(FaultyCalculation.class, null);
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