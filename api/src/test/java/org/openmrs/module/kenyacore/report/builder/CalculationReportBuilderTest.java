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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.CsvReportRenderer;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CalculationReportBuilder}
 */
public class CalculationReportBuilderTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private CalculationReportBuilder builder;

	@Autowired
	@Qualifier("test.report.test1")
	private ReportDescriptor test1;

	@Autowired
	@Qualifier("test.report.test2")
	private ReportDescriptor test2;

	/**
	 * @see CalculationReportBuilder#build(org.openmrs.module.kenyacore.report.ReportDescriptor)
	 */
	@Test
	public void build_calculationHasNoParams() throws Exception {
		ReportDefinition definition = builder.build(test1);

		Assert.assertThat(definition.getParameters(), hasSize(0));

		ReportData data = Context.getService(ReportDefinitionService.class).evaluate(definition, new EvaluationContext());

		CsvReportRenderer renderer = new CsvReportRenderer();
		renderer.render(data, "", System.out);
	}

	/**
	 * @see CalculationReportBuilder#build(org.openmrs.module.kenyacore.report.ReportDescriptor)
	 */
	@Test
	public void build_calculationHasParams() throws Exception {
		ReportDefinition definition = builder.build(test2);

		Assert.assertThat(definition.getParameters(), hasSize(2));

		ReportData data = Context.getService(ReportDefinitionService.class).evaluate(definition, new EvaluationContext());

		CsvReportRenderer renderer = new CsvReportRenderer();
		renderer.render(data, "", System.out);
	}
}