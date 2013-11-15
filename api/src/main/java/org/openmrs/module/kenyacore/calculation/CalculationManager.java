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

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.Calculation;
import org.openmrs.calculation.CalculationProvider;
import org.openmrs.calculation.InvalidCalculationException;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.module.kenyacore.ContentManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Manager for calculations used by KenyaEMR
 */
@Component
public class CalculationManager implements ContentManager, CalculationProvider {
	
	protected static final Log log = LogFactory.getLog(CalculationManager.class);

	private Map<String, Class<? extends PatientCalculation>> calculationClasses = new HashMap<String, Class<? extends PatientCalculation>>();
	
	private List<Class<? extends PatientFlagCalculation>> flagCalculationClasses = new ArrayList<Class<? extends PatientFlagCalculation>>();

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#getPriority()
	 */
	@Override
	public int getPriority() {
		return 50;
	}

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#refresh()
	 */
	@Override
	public synchronized void refresh() {
		calculationClasses.clear();
		flagCalculationClasses.clear();

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(PatientCalculation.class));

		for (BeanDefinition bd : scanner.findCandidateComponents("org.openmrs.module")) {
			try {
				Class<? extends PatientCalculation> clazz = (Class<? extends PatientCalculation>) Context.loadClass(bd.getBeanClassName());
				calculationClasses.put(bd.getBeanClassName(), clazz);

				if (PatientFlagCalculation.class.isAssignableFrom(clazz)) {
					flagCalculationClasses.add((Class<? extends PatientFlagCalculation>) clazz);
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			log.debug("Registered calculation class " + bd.getBeanClassName());
		}
	}

	/**
	 * Gets all of the registered calculation classes
	 * @return the calculation classes
	 */
	public Collection<Class<? extends PatientCalculation>> getAllCalculationClasses() {
		return calculationClasses.values();
	}

	/**
	 * Gets new instances of all patient flag calculations in this module
	 * @return list of flag calculation instances
	 */
	public List<PatientFlagCalculation> getFlagCalculations() {
		List<PatientFlagCalculation> ret = new ArrayList<PatientFlagCalculation>();

		for (Class<? extends PatientFlagCalculation> calculationClass : flagCalculationClasses) {
			ret.add((PatientFlagCalculation) CalculationUtils.instantiateCalculation(calculationClass, null));
		}

		return ret;
	}
	
	/**
	 * @see org.openmrs.calculation.CalculationProvider#getCalculation(java.lang.String, java.lang.String)
	 */
	@Override
	public Calculation getCalculation(String calculationName, String configuration) throws InvalidCalculationException {
		Class<? extends PatientCalculation> clazz = calculationClasses.get(calculationName);
		if (clazz == null)
			throw new InvalidCalculationException("Not Found: " + calculationName + " (valid values are: " + calculationClasses.keySet() + ")");

		return CalculationUtils.instantiateCalculation(clazz, configuration);
	}
}