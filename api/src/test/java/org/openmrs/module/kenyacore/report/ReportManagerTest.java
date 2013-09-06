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
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ReportManager}
 */
public class ReportManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private ReportManager reportManager;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		reportManager.refresh();
	}

	/**
	 * @see ReportManager#getAllReportDescriptors()
	 */
	@Test
	public void getAllReportBuilders() {
		List<ReportDescriptor> descriptors = reportManager.getAllReportDescriptors();
		Assert.assertThat(descriptors.size(), is(1));
		Assert.assertThat(descriptors.get(0), is(instanceOf(CalculationReportDescriptor.class)));

		CalculationReportDescriptor calcReport = (CalculationReportDescriptor) descriptors.get(0);
		Assert.assertThat(calcReport.getId(), is("test.report.test1"));
	}

	/**
	 * @see ReportManager#getReportDescriptor(String)
	 */
	@Test
	public void getReportDescriptor() {
		ReportDescriptor descriptor = reportManager.getReportDescriptor("test.report.test1");
		Assert.assertThat(descriptor, is(instanceOf(CalculationReportDescriptor.class)));
	}
}