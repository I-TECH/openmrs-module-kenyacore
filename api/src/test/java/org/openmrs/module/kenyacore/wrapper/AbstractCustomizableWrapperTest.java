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
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.customdatatype.datatype.FreeTextDatatype;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.metadatadeploy.bundle.CoreConstructors;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractCustomizableWrapper}
 */
public class AbstractCustomizableWrapperTest extends BaseModuleContextSensitiveTest {

	private static final String TELEPHONE_ATTRTYPE_UUID = "8F057D10-F7FB-467A-A87D-F63E7537D85A";

	@Before
	public void setup() {
		// Create and save location attribute type for telephone number
		LocationAttributeType telephoneType = CoreConstructors.locationAttributeType("Telephone", "Contact No.", FreeTextDatatype.class, null, 0, 1, TELEPHONE_ATTRTYPE_UUID);
		Context.getLocationService().saveLocationAttributeType(telephoneType);
	}

	@Test
	public void getAsAttribute_shouldReturnNullIfNoSuchAttribute() {
		Location unknown = MetadataUtils.existing(Location.class, StandardTestData._Location.UNKNOWN);
		LocationWrapper wrapper = new LocationWrapper(unknown);

		Assert.assertThat(wrapper.getTelephone(), nullValue());
	}

	/**
	 * Location rapper class for testing
	 */
	private static class LocationWrapper extends AbstractCustomizableWrapper<Location, LocationAttribute> {

		public LocationWrapper(Location target) {
			super(target);
		}

		public String getTelephone() {
			return (String) getAsAttribute(TELEPHONE_ATTRTYPE_UUID);
		}
	}
}