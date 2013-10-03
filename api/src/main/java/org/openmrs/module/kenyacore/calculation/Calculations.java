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

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.CoreUtils;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.patient.definition.EncountersForPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ProgramEnrollmentsForPatientDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ObsForPersonDataDefinition;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * Utility class of common base calculations
 */
public class Calculations {

	/**
	 * Evaluates genders of each patient
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the genders in a calculation result map
	 */
	public static CalculationResultMap genders(Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		GenderDataDefinition def = new GenderDataDefinition();
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates ages of each patient
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the ages in a calculation result map
	 */
	public static CalculationResultMap ages(Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		AgeDataDefinition def = new AgeDataDefinition();
		def.setEffectiveDate(calculationContext.getNow());
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates all obs of a given type of each patient
	 * @param concept the obs' concept
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the obss in a calculation result map
	 */
	public static CalculationResultMap allObs(Concept concept, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		ObsForPersonDataDefinition def = new ObsForPersonDataDefinition(null, TimeQualifier.ANY, concept, calculationContext.getNow(), null);
		return CalculationUtils.ensureEmptyListResults(CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext), cohort);
	}

	/**
	 * Evaluates the first obs of a given type of each patient
	 * @param concept the obs' concept
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the obss in a calculation result map
	 */
	public static CalculationResultMap firstObs(Concept concept, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		ObsForPersonDataDefinition def = new ObsForPersonDataDefinition(null, TimeQualifier.FIRST, concept, calculationContext.getNow(), null);
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates the first obs of a given type of each patient
	 * @param concept the obs' concept
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the obss in a calculation result map
	 */
	public static CalculationResultMap firstObsOnOrAfter(Concept concept, Date onOrAfter, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		ObsForPersonDataDefinition def = new ObsForPersonDataDefinition(null, TimeQualifier.FIRST, concept, calculationContext.getNow(), onOrAfter);
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates the last obs of a given type of each patient
	 * @param concept the obs' concept
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the obss in a calculation result map
	 */
	public static CalculationResultMap lastObs(Concept concept, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		ObsForPersonDataDefinition def = new ObsForPersonDataDefinition(null, TimeQualifier.LAST, concept, calculationContext.getNow(), null);
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates the last obs of a given type of each patient that occurred at least the given number of days ago
	 * @param concept the obs' concept
	 * @param onOrBefore the number of days that must be elapsed between now and the observation
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the obss in a calculation result map
	 */
	public static CalculationResultMap lastObsOnOrBefore(Concept concept, Date onOrBefore, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		// Only interested in obs before now
		onOrBefore = CoreUtils.earliest(onOrBefore, calculationContext.getNow());

		ObsForPersonDataDefinition def = new ObsForPersonDataDefinition(null, TimeQualifier.LAST, concept, onOrBefore, null);
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates the last obs of a given type of each patient that occurred at least the given number of days ago
	 * @param concept the obs' concept
	 * @param atLeastDaysAgo the number of days that must be elapsed between now and the observation
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the obss in a calculation result map
	 */
	public static CalculationResultMap lastObsAtLeastDaysAgo(Concept concept, int atLeastDaysAgo, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		Date onOrBefore = CoreUtils.dateAddDays(calculationContext.getNow(), -atLeastDaysAgo);
		return lastObsOnOrBefore(concept, onOrBefore, cohort, calculationContext);
	}

	/**
	 * Evaluates all encounters of a given type of each patient
	 * @param encounterType the encounter type
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the encounters in a calculation result map
	 */
	public static CalculationResultMap allEncounters(EncounterType encounterType, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		EncountersForPatientDataDefinition def = new EncountersForPatientDataDefinition();
		if (encounterType != null) {
			def.addType(encounterType);
		}
		def.setWhich(TimeQualifier.ANY);
		def.setOnOrBefore(calculationContext.getNow());
		return CalculationUtils.ensureEmptyListResults(CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext), cohort);
	}

	/**
	 * Evaluates the last encounter of a given type of each patient
	 * @param encounterType the encounter type
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the encounters in a calculation result map
	 */
	public static CalculationResultMap lastEncounter(EncounterType encounterType, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		EncountersForPatientDataDefinition def = new EncountersForPatientDataDefinition();
		if (encounterType != null) {
			def.addType(encounterType);
		}
		def.setWhich(TimeQualifier.LAST);
		def.setOnOrBefore(calculationContext.getNow());
		return CalculationUtils.evaluateWithReporting(def, cohort, null, null, calculationContext);
	}

	/**
	 * Evaluates the active program enrollment of the specified program
	 * @param program the program
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the enrollments in a calculation result map
	 */
	public static CalculationResultMap activeEnrollment(Program program, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		return activeEnrollmentOn(program, calculationContext.getNow(), cohort, calculationContext);
	}

	/**
	 * Evaluates the last program enrollment on the specified program
	 * @param program the program
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the enrollments in a calculation result map
	 */
	public static CalculationResultMap activeEnrollmentOn(Program program, Date onDate, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		ProgramEnrollmentsForPatientDataDefinition def = new ProgramEnrollmentsForPatientDataDefinition();
		def.setWhichEnrollment(TimeQualifier.LAST);
		def.setProgram(program);
		def.setActiveOnDate(onDate);
		return CalculationUtils.evaluateWithReporting(def, cohort, new HashMap<String, Object>(), null, calculationContext);
	}
}