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

package org.openmrs.module.kenyacore.calculation;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link BooleanResult}
 */
public class BooleanResultTest {

	/**
	 * @see BooleanResult#BooleanResult(Boolean, org.openmrs.calculation.Calculation, org.openmrs.calculation.CalculationContext)
	 */
	@Test
	public void BooleanResult() {
		BooleanResult result1 = new BooleanResult(Boolean.TRUE, null, null);

		Assert.assertThat(result1.getValue(), is((Object) Boolean.TRUE));
	}

	/**
	 * @see BooleanResult#isEmpty()
	 */
	@Test
	public void isEmpty() {
		Assert.assertThat(new BooleanResult(null, null).isEmpty(), is(true));
		Assert.assertThat(new BooleanResult(Boolean.FALSE, null).isEmpty(), is(true));
		Assert.assertThat(new BooleanResult(Boolean.TRUE, null).isEmpty(), is(false));
	}
}