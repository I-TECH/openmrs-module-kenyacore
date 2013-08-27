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
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.TreeSet;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractEntityDescriptor}
 */
public class AbstractEntityDescriptorTest extends BaseModuleContextSensitiveTest {

	private TestLocationDescriptor descriptor1, descriptor2, descriptor3, descriptor4, descriptor5;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		descriptor1 = new TestLocationDescriptor();
		descriptor1.setTargetUuid("dc5c1fcc-0459-4201-bf70-0b90535ba362"); // Unknown location
		descriptor1.setOrder(100);

		descriptor2 = new TestLocationDescriptor();
		descriptor2.setTargetUuid("9356400c-a5a2-4532-8f2b-2361b3446eb8"); // Xanadu
		descriptor2.setOrder(200);

		descriptor3 = new TestLocationDescriptor();
		descriptor3.setTargetUuid("test-uuid"); // Doesn't exist in db
		descriptor3.setOrder(300);

		descriptor4 = new TestLocationDescriptor();
		descriptor4.setTargetUuid("test-uuid"); // Same UUID as previous
		descriptor4.setOrder(400);

		descriptor5 = new TestLocationDescriptor();
		descriptor5.setTargetUuid("xxxx");
		descriptor5.setOrder(400); // Same order as previous
	}

	@Test
	public void getTargetUuid() throws Exception {
		Assert.assertThat(descriptor1.getTargetUuid(), is("dc5c1fcc-0459-4201-bf70-0b90535ba362"));
		Assert.assertThat(descriptor2.getTargetUuid(), is("9356400c-a5a2-4532-8f2b-2361b3446eb8"));
	}

	@Test
	public void getTarget() throws Exception {
		Assert.assertThat(descriptor1.getTarget(), is(Context.getLocationService().getLocation(1)));
		Assert.assertThat(descriptor2.getTarget(), is(Context.getLocationService().getLocation(2)));
	}

	@Test
	public void getOrder() throws Exception {
		Assert.assertThat(descriptor1.getOrder(), is(100));
		Assert.assertThat(descriptor2.getOrder(), is(200));
	}

	@Test
	public void compareTo() {
		TreeSet<TestLocationDescriptor> tree = new TreeSet<TestLocationDescriptor>();
		tree.add(descriptor5);
		tree.add(descriptor4);
		tree.add(descriptor3);
		tree.add(descriptor2);
		tree.add(descriptor1);

		Assert.assertThat(tree, contains(descriptor1, descriptor2, descriptor3, descriptor4, descriptor5));
	}

	@Test
	public void hashCode_shouldReturnSameValueIfSameTargetUuid() {
		Assert.assertThat(descriptor3.hashCode(), is(descriptor4.hashCode()));
	}

	@Test
	public void equals_shouldReturnTrueIfEqual() {
		Assert.assertThat(descriptor1.equals(descriptor1), is(true)); // Same object
		Assert.assertThat(descriptor3.equals(descriptor4), is(true)); // Same target

		Assert.assertThat(descriptor3.equals(new Integer(123)), is(false)); // Different class
	}

	@Test
	public void toString_shouldReturnNonNull() {
		Assert.assertThat(descriptor1.toString(), is(notNullValue()));
		Assert.assertThat(descriptor2.toString(), is(notNullValue()));
	}

	/**
	 * For testing, we implement a descriptor class for locations
	 */
	private class TestLocationDescriptor extends AbstractEntityDescriptor<Location> {

		@Override
		public Location getTarget() {
			return Context.getLocationService().getLocationByUuid(targetUuid);
		}
	}
}