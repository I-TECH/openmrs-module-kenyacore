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

import org.openmrs.PatientIdentifierType;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.report.CalculationReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

/**
 * Generic calculation based patient-list report builder
 */
@Component("kenyacore.genericCalcReportBuilder")
public class CalculationReportBuilder implements ReportBuilder {

	/**
	 * @see ReportBuilder#build(org.openmrs.module.kenyacore.report.ReportDescriptor)
	 */
	@Override
	public ReportDefinition build(ReportDescriptor report) {
		ReportDefinition rd = new ReportDefinition();
		rd.setName(report.getName());
		rd.setDescription(report.getDescription());
		rd.addDataSetDefinition(buildDataSet((CalculationReportDescriptor) report), null);
		return rd;
	}

	/**
	 * Builds the data set
	 * @return the data set
	 */
	protected PatientDataSetDefinition buildDataSet(CalculationReportDescriptor report) {
		PatientCalculation calc = CalculationUtils.instantiateCalculation(report.getCalculation(), null);
		CalculationCohortDefinition cd = new CalculationCohortDefinition(calc);
		cd.setName(report.getName());

		PatientDataSetDefinition dsd = new PatientDataSetDefinition(report.getName() + " DSD");
		dsd.addRowFilter(ReportUtils.map(cd));
		addColumns(report, dsd);
		return dsd;
	}

	/**
	 * Override this if you don't want the default (HIV ID, name, sex, age)
	 * @param dsd this will be modified by having columns added
	 */
	protected void addColumns(CalculationReportDescriptor report, PatientDataSetDefinition dsd) {
		addStandardColumns(report, dsd);
	}

	/**
	 * Adds the standard patient list columns
	 * @param dsd the data set definition
	 */
	protected void addStandardColumns(CalculationReportDescriptor report, PatientDataSetDefinition dsd) {
		dsd.addColumn("id", new PatientIdDataDefinition(), "");
		dsd.addColumn("Name", new PreferredNameDataDefinition(), "");
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");

		if (report.getDisplayIdentifier() != null) {
			PatientIdentifierType idType = report.getDisplayIdentifier().getTarget();
			dsd.addColumn(idType.getName(), new PatientIdentifierDataDefinition(idType.getName(), idType), "");
		}
	}
}