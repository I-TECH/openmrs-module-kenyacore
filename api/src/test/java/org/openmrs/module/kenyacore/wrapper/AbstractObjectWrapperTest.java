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

package org.openmrs.module.kenyacore.wrapper;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractObjectWrapper}
 */
public class AbstractObjectWrapperTest extends BaseModuleContextSensitiveTest {

	/**
	 * @see AbstractObjectWrapper#getId()
	 */
	@Test
	public void getId_shouldReturnTargetObjectId() {
		Location unknown = MetadataUtils.existing(Location.class, StandardTestData._Location.UNKNOWN);
		LocationWrapper wrapper = new LocationWrapper(unknown);

		Assert.assertThat(wrapper.getId(), is(unknown.getId()));
	}

	/**
	 * @see AbstractObjectWrapper#getUuid()
	 */
	@Test
	public void getUuid_shouldReturnTargetObjectId() {
		Location unknown = MetadataUtils.existing(Location.class, StandardTestData._Location.UNKNOWN);
		LocationWrapper wrapper = new LocationWrapper(unknown);

		Assert.assertThat(wrapper.getUuid(), is(StandardTestData._Location.UNKNOWN));
	}

	/**
	 * @see AbstractObjectWrapper#equals(Object)
	 */
	@Test
	public void equals_shouldDelegateToTargetEquals() {
		Location unknown = MetadataUtils.existing(Location.class, StandardTestData._Location.UNKNOWN);
		Location xanadu = MetadataUtils.existing(Location.class, StandardTestData._Location.XANADU);
		LocationWrapper unknownWrapped = new LocationWrapper(unknown);
		LocationWrapper xanaduWrapped = new LocationWrapper(xanadu);

		Assert.assertThat(unknownWrapped.equals(unknownWrapped), is(true));
		Assert.assertThat(unknownWrapped.equals(xanaduWrapped), is(false));
		Assert.assertThat(unknownWrapped.equals(null), is(false));
	}

	/**
	 * @see AbstractObjectWrapper#hashCode()
	 */
	@Test
	public void hashCode_shouldDelegateToTargetHashCode() {
		Location unknown = MetadataUtils.existing(Location.class, StandardTestData._Location.UNKNOWN);
		LocationWrapper wrapped = new LocationWrapper(unknown);

		Assert.assertThat(wrapped.hashCode(), is(unknown.hashCode()));
	}

	/**
	 * Location rapper class for testing
	 */
	private static class LocationWrapper extends AbstractCustomizableWrapper<Location, LocationAttribute> {

		public LocationWrapper(Location target) {
			super(target);
		}
	}
}