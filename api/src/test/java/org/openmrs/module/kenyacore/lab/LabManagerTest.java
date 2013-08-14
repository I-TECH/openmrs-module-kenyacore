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

package org.openmrs.module.kenyacore.lab;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link LabManager}
 */
public class LabManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private LabManager labManager;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() throws Exception {
		labManager.refresh();
	}

	@Test
	public void getCategories() {
		Assert.assertThat(labManager.getCategories(), contains("category1", "category2"));
	}

	@Test
	public void getTests() {
		List<LabTestDefinition> tests = labManager.getTests("category1");
		Assert.assertThat(tests.size(), is(2));
		Assert.assertThat(tests.get(0).getConcept().getTarget(), is(Context.getConceptService().getConcept(5089)));
		Assert.assertThat(tests.get(1).getConcept().getTarget(), is(Context.getConceptService().getConcept(5497)));
	}

	/**
	 * @see LabManager#isLabTest(org.openmrs.Concept)
	 */
	@Test
	public void isLabTest() {
		Assert.assertTrue(labManager.isLabTest(Context.getConceptService().getConcept(5089)));
		Assert.assertTrue(labManager.isLabTest(Context.getConceptService().getConcept(5497)));
		Assert.assertFalse(labManager.isLabTest(Context.getConceptService().getConcept(13)));
		Assert.assertFalse(labManager.isLabTest(null));
	}
}