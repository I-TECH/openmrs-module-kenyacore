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
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.kenyacore.test.TestDescriptor;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.TreeSet;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractOrderedDescriptor}
 */
public class AbstractOrderedDescriptorTest extends BaseModuleContextSensitiveTest {

	@Autowired
	@Qualifier("test.descriptor1")
	private TestDescriptor descriptor1;

	@Autowired
	@Qualifier("test.descriptor2")
	private TestDescriptor descriptor2;

	private TestDescriptor descriptor3;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		descriptor3 = new TestDescriptor();
		descriptor3.setId("test.descriptor1"); // Duplicate id
		descriptor3.setOrder(1);
	}

	@Test
	public void setOrder() throws Exception {
		descriptor3.setOrder(100);
		Assert.assertThat(descriptor3.getOrder(), is(100));
	}

	@Test
	public void getOrder() throws Exception {
		Assert.assertThat(descriptor1.getOrder(), is(3));
		Assert.assertThat(descriptor2.getOrder(), is(2));
	}

	@Test
	public void compareTo() {
		TreeSet<TestDescriptor> tree = new TreeSet<TestDescriptor>();
		tree.add(descriptor3);
		tree.add(descriptor2);
		tree.add(descriptor1);

		Assert.assertThat(tree, contains(descriptor1, descriptor2, descriptor3));
	}
}