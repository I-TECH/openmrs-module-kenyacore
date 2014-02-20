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

package org.openmrs.module.kenyacore.requirement;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link RequirementManager}
 */
public class RequirementManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private RequirementManager requirementManager;

	@Autowired
	private TestRequirement testRequirement;

	/**
	 * @see RequirementManager#refresh()
	 */
	@Test
	public void refresh() {
		requirementManager.refresh();

		Assert.assertThat(requirementManager.getAllRequirements(), containsInAnyOrder((Requirement) testRequirement));
	}

	/**
	 * @see RequirementManager#refresh()
	 */
	@Test
	public void refresh_shouldThrowExceptionIfRequirementNotSatisfied() {
		testRequirement.satisfied = false;

		try {
			requirementManager.refresh();
		}
		catch(UnsatisfiedRequirementException ex) {
			return;
		}
		finally {
			testRequirement.satisfied = true;
		}

		Assert.fail();
	}

	/**
	 * Requirement component for testing that can be configured to fail or pass
	 */
	@Component
	public static class TestRequirement implements Requirement {

		public boolean satisfied = true;

		@Override
		public String getName() {
			return "Test Requirement";
		}

		@Override
		public String getRequiredVersion() {
			return "1.0";
		}

		@Override
		public String getFoundVersion() {
			return "2.0";
		}

		@Override
		public boolean isSatisfied() {
			return satisfied;
		}
	}
}