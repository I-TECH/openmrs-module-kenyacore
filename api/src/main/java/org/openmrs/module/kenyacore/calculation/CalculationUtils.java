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

import org.openmrs.calculation.ConfigurableCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ResultUtil;
import org.openmrs.util.OpenmrsUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Calculation utility methods
 */
public class CalculationUtils {

	/**
	 * Instantiates and configures a patient calculation
	 * @param clazz the calculation class
	 * @param configuration the configuration
	 * @return the calculation instance
	 */
	public static PatientCalculation instantiateCalculation(Class<? extends PatientCalculation> clazz, String configuration) {
		try {
			PatientCalculation calc = clazz.newInstance();

			if (configuration != null && ConfigurableCalculation.class.isAssignableFrom(clazz)) {
				((ConfigurableCalculation) calc).setConfiguration(configuration);
			}

			return calc;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Extracts patients from calculation result map with non-false/empty results
	 * @param results calculation result map
	 * @return the extracted patient ids
	 */
	public static Set<Integer> patientsThatPass(CalculationResultMap results) {
		return patientsThatPass(results, null);
	}

	/**
	 * Extracts patients from calculation result map with matching results
	 * @param results calculation result map
	 * @param requiredResult the required result value
	 * @return the extracted patient ids
	 */
	public static Set<Integer> patientsThatPass(CalculationResultMap results, Object requiredResult) {
		Set<Integer> ret = new HashSet<Integer>();
		for (Map.Entry<Integer, CalculationResult> e : results.entrySet()) {
			CalculationResult result = e.getValue();

			// If there is no required result, just check trueness of result, otherwise check result matches required result
			if ((requiredResult == null && ResultUtil.isTrue(result)) || (result != null && result.getValue().equals(requiredResult))) {
				ret.add(e.getKey());
			}
		}
		return ret;
	}
}