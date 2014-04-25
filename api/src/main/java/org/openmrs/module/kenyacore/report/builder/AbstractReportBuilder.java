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

import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;

import java.util.List;

/**
 * Abstract base class for all report builders
 */
public abstract class AbstractReportBuilder implements ReportBuilder {

	/**
	 * Builds the report definition
	 * @return the report definition
	 */
	@Override
	public ReportDefinition build(ReportDescriptor descriptor) {
		ReportDefinition definition = new ReportDefinition();
		definition.setName(descriptor.getName());
		definition.setDescription(descriptor.getDescription());
		definition.addParameters(getParameters(descriptor));

		// Add all datasets
		for (Mapped<DataSetDefinition> dataset : buildDataSets(descriptor, definition)) {
			definition.addDataSetDefinition(dataset.getParameterizable().getName(), dataset);
		}

		return definition;
	}

	/**
	 * Gets the report parameters
	 * @param descriptor the report descriptor
	 * @return the report parameters
	 */
	protected abstract List<Parameter> getParameters(ReportDescriptor descriptor);

	/**
	 * Builds and maps the data sets
	 * @param descriptor the report descriptor
	 * @param report the report definition
	 * @return the mapped datasets
	 */
	protected abstract List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report);
}