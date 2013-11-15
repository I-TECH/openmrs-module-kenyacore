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

package org.openmrs.module.kenyacore.test;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link OpenmrsMatchers}
 */
public class OpenmrsMatchersTest {

	/**
	 * @see OpenmrsMatchers#isDate(Object)
	 */
	@Test
	public void isDate_shouldMatchSameDates() {
		Assert.assertThat(TestUtils.date(2006, 5, 4, 3, 2, 1), OpenmrsMatchers.isDate(TestUtils.date(2006, 5, 4, 3, 2, 1)));
		Assert.assertThat(TestUtils.date(2006, 5, 4), OpenmrsMatchers.isDate(TestUtils.date(2006, 5, 4)));
		Assert.assertThat(TestUtils.date(2006, 5, 4), not(OpenmrsMatchers.isDate(TestUtils.date(2001, 2, 3))));
	}
}