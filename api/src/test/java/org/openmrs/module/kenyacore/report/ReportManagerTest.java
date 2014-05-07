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

package org.openmrs.module.kenyacore.report;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.kenyacore.program.ProgramDescriptor;
import org.openmrs.module.kenyacore.program.ProgramManager;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ReportManager}
 */
public class ReportManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private ProgramManager programManager;

	@Autowired
	private ReportManager reportManager;

	@Autowired
	@Qualifier("test.program.hiv")
	private ProgramDescriptor hivProgram;

	@Autowired
	@Qualifier("test.report.test1")
	private ReportDescriptor report1;

	@Autowired
	@Qualifier("test.report.test2")
	private ReportDescriptor report2;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		programManager.refresh();
		reportManager.refresh();
	}

	@Test
	public void integration() {
		// Check that report #2 is added to the program via the report configuration
		Assert.assertThat(hivProgram.getReports(), contains(report2));
	}

	/**
	 * @see ReportManager#getAllReportDescriptors()
	 */
	@Test
	public void getAllReportDescriptors_shouldReturnAllDescriptors() {
		List<ReportDescriptor> descriptors = reportManager.getAllReportDescriptors();
		Assert.assertThat(descriptors, containsInAnyOrder(report1, report2));
	}

	/**
	 * @see ReportManager#getReportDescriptor(org.openmrs.module.reporting.report.definition.ReportDefinition)
	 */
	@Test
	public void getReportDescriptor() {
		ReportDefinition definition = new ReportDefinition();
		definition.setUuid("4FAE55A5-E144-471F-9142-7E776A4E19E0");

		ReportDescriptor descriptor = reportManager.getReportDescriptor(definition);
		Assert.assertThat(descriptor, is(report1));
	}
}