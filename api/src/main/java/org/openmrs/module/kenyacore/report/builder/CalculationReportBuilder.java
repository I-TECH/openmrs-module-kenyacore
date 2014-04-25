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

package org.openmrs.module.kenyacore.report.builder;

import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.report.CalculationReportDescriptor;
import org.openmrs.module.kenyacore.report.CohortReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.springframework.stereotype.Component;

/**
 * Generic calculation based patient-list report builder
 */
@Component("kenyacore.genericCalcReportBuilder")
public class CalculationReportBuilder extends AbstractCohortReportBuilder {

	/**
	 * @see AbstractCohortReportBuilder#buildCohort(org.openmrs.module.kenyacore.report.CohortReportDescriptor)
	 */
	@Override
	protected Mapped<CohortDefinition> buildCohort(CohortReportDescriptor descriptor) {
		CalculationReportDescriptor desc = (CalculationReportDescriptor) descriptor;

		PatientCalculation calc = CalculationUtils.instantiateCalculation(desc.getCalculation(), null);
		CohortDefinition cd = new CalculationCohortDefinition(calc);
		cd.setName(descriptor.getName());

		return ReportUtils.map(cd);
	}
}