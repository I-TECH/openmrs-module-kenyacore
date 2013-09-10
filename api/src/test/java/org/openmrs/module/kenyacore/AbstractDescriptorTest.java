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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests for {@link AbstractDescriptor}
 */
public class AbstractDescriptorTest extends BaseModuleContextSensitiveTest {

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
	}

	/**
	 * @see AbstractDescriptor#getId()
	 */
	@Test
	public void getId() throws Exception {
		Assert.assertThat(descriptor1.getId(), is("test.descriptor1"));
		Assert.assertThat(descriptor2.getId(), is("test.descriptor2"));
		Assert.assertThat(descriptor3.getId(), is("test.descriptor1"));
	}

	/**
	 * @see AbstractDescriptor#setId(String)
	 */
	@Test
	public void setId() throws Exception {
		descriptor3.setId("xxx");
		Assert.assertThat(descriptor3.getId(), is("xxx"));
	}

	/**
	 * @see AbstractDescriptor#equals(Object)
	 */
	@Test
	public void equals_shouldReturnTrueWhenEqual() throws Exception {
		Assert.assertThat(descriptor1.equals(descriptor1), is(true)); // Same object
		Assert.assertThat(descriptor3.equals(descriptor1), is(true)); // Same id

		Assert.assertThat(descriptor1.equals(new Integer(123)), is(false)); // Different class
	}

	/**
	 * @see AbstractDescriptor#hashCode()
	 */
	@Test
	public void hashCode_shouldReturnSameValueIfSameId() {
		Assert.assertThat(descriptor1.hashCode(), is(descriptor3.hashCode()));
	}

	/**
	 * @see AbstractDescriptor#toString()
	 */
	@Test
	public void toString_shouldReturnNonNull() {
		Assert.assertThat(descriptor1.toString(), is(notNullValue()));
		Assert.assertThat(descriptor2.toString(), is(notNullValue()));
	}
}