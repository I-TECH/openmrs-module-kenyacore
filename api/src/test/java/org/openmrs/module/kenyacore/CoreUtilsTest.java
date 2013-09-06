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

package org.openmrs.module.kenyacore;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CoreUtils}
 */
public class CoreUtilsTest {

	@Test
	public void integration() {
		new CoreUtils();
	}

	/**
	 * @see CoreUtils#merge(java.util.Collection[])
	 */
	@Test
	public void merge_shouldMergeByNaturalOrder() {
		List<Integer> list1 = Arrays.asList(2, 5, 9);
		List<Integer> list2 = Arrays.asList(1, 3, 10);

		Assert.assertThat(CoreUtils.merge(list1, list2), contains(1, 2, 3, 5, 9, 10));
	}
}