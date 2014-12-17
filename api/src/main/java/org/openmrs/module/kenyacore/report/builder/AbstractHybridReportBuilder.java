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
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for report builders which build cohort and/or indicator components reports - i.e. one row-per-patient dataset
 */
public abstract class AbstractHybridReportBuilder extends AbstractReportBuilder {

	/**
	 * @see org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder#getParameters(org.openmrs.module.kenyacore.report.ReportDescriptor)
	 */
	@Override
	protected List<Parameter> getParameters(ReportDescriptor descriptor) {
		return Arrays.asList();
	}

	/**
	 * @see org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder#buildDataSets(org.openmrs.module.kenyacore.report.ReportDescriptor, org.openmrs.module.reporting.report.definition.ReportDefinition)
	 */
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition rd) {
		PatientDataSetDefinition dsd = new PatientDataSetDefinition(descriptor.getName() + " DSD");
		dsd.addParameters(rd.getParameters()); // Same parameters as report

		Mapped<CohortDefinition> cohort = buildCohort((HybridReportDescriptor) descriptor, dsd);
		dsd.addRowFilter(cohort);
		addColumns((HybridReportDescriptor) descriptor, dsd);

		// Map all parameters straight through
		return Arrays.asList(new Mapped<DataSetDefinition>(dsd, Mapped.straightThroughMappings(dsd)));
	}

	/**
	 * Builds and maps the cohort to base this cohort report on
	 * @param descriptor the report descriptor
	 * @param dsd the data set definition
	 * @return the mapped cohort definition
	 */
	protected abstract Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd);

	/**
	 * Override this if you don't want the default (HIV ID, name, sex, age)
	 * @param dsd this will be modified by having columns added
	 */
	protected void addColumns(HybridReportDescriptor report, PatientDataSetDefinition dsd) {
		addStandardColumns(report, dsd);
	}

	/**
	 * Adds the standard patient list columns
	 * @param dsd the data set definition
	 */
	protected void addStandardColumns(HybridReportDescriptor report, PatientDataSetDefinition dsd) {
		DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
		DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);

		dsd.addColumn("id", new PatientIdDataDefinition(), "");
		dsd.addColumn("Name", nameDef, "");
		dsd.addColumn("Age", new AgeDataDefinition(), "");
		dsd.addColumn("Sex", new GenderDataDefinition(), "");

		if (report.getDisplayIdentifier() != null) {
			PatientIdentifierType idType = report.getDisplayIdentifier().getTarget();

			DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
			DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(idType.getName(), idType), identifierFormatter);

			dsd.addColumn(idType.getName(), identifierDef, "");
		}
	}
}
