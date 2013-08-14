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

import java.util.List;

/**
 * Lab test panel definition
 */
public class LabTestPanel extends LabTestDefinition {

	private List<LabTestDefinition> tests;

	public List<LabTestDefinition> getTests() {
		return tests;
	}

	public void setTests(List<LabTestDefinition> tests) {
		this.tests = tests;
	}
}