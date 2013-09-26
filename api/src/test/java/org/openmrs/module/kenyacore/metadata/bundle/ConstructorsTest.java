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

package org.openmrs.module.kenyacore.metadata.bundle;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.customdatatype.SerializingCustomDatatype;
import org.openmrs.customdatatype.datatype.FreeTextDatatype;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Constructors}
 */
public class ConstructorsTest extends BaseModuleContextSensitiveTest {

	/**
	 * @see Constructors#globalProperty(String, String, String, String)
	 */
	@Test
	public void globalProperty_withoutCustomDatatype() {
		// Check with non-null value
		GlobalProperty obj = Constructors.globalProperty("property", "desc", "value", "obj-uuid");
		Assert.assertThat(obj.getProperty(), is("property"));
		Assert.assertThat(obj.getDescription(), is("desc"));
		Assert.assertThat(obj.getDatatypeClassname(), is(FreeTextDatatype.class.getName()));
		Assert.assertThat(obj.getDatatypeConfig(), is(nullValue()));
		Assert.assertThat(obj.getPropertyValue(), is(""));
		Assert.assertThat(obj.getValue(), is((Object) "value"));
		Assert.assertThat(obj.getUuid(), is("obj-uuid"));

		// Check with empty string value
		obj = Constructors.globalProperty("property", "desc", "", "obj-uuid");
		Assert.assertThat(obj.getProperty(), is("property"));
		Assert.assertThat(obj.getDescription(), is("desc"));
		Assert.assertThat(obj.getDatatypeClassname(), is(FreeTextDatatype.class.getName()));
		Assert.assertThat(obj.getDatatypeConfig(), is(nullValue()));
		Assert.assertThat(obj.getPropertyValue(), is(""));
		Assert.assertThat(obj.getValue(), is((Object) ""));
		Assert.assertThat(obj.getUuid(), is("obj-uuid"));

		// Check with null value
		obj = Constructors.globalProperty("property", "desc", null, "obj-uuid");
		Assert.assertThat(obj.getProperty(), is("property"));
		Assert.assertThat(obj.getDescription(), is("desc"));
		Assert.assertThat(obj.getDatatypeClassname(), is(FreeTextDatatype.class.getName()));
		Assert.assertThat(obj.getDatatypeConfig(), is(nullValue()));
		Assert.assertThat(obj.getPropertyValue(), is(""));
		Assert.assertThat(obj.getValue(), is((Object) "")); // You'd think this would be null...
		Assert.assertThat(obj.getUuid(), is("obj-uuid"));
	}

	/**
	 * @see Constructors#globalProperty(String, String, Class, String, Object, String)
	 */
	@Test
	public void globalProperty_withCustomDatatype() {
		// Check with non-null value
		GlobalProperty obj = Constructors.globalProperty("property", "desc", TestingDatatype.class, "config", 123, "obj-uuid");
		Assert.assertThat(obj.getProperty(), is("property"));
		Assert.assertThat(obj.getDescription(), is("desc"));
		Assert.assertThat(obj.getDatatypeClassname(), is(TestingDatatype.class.getName()));
		Assert.assertThat(obj.getDatatypeConfig(), is("config"));
		Assert.assertThat(obj.getPropertyValue(), is(""));
		Assert.assertThat(obj.getValue(), is((Object) new Integer(123)));
		Assert.assertThat(obj.getUuid(), is("obj-uuid"));

		// Check with null value
		obj = Constructors.globalProperty("property", "desc", TestingDatatype.class, "config", null, "obj-uuid");
		Assert.assertThat(obj.getProperty(), is("property"));
		Assert.assertThat(obj.getDescription(), is("desc"));
		Assert.assertThat(obj.getDatatypeClassname(), is(TestingDatatype.class.getName()));
		Assert.assertThat(obj.getDatatypeConfig(), is("config"));
		Assert.assertThat(obj.getPropertyValue(), is(""));
		Assert.assertThat(obj.getValue(), is(nullValue()));
		Assert.assertThat(obj.getUuid(), is("obj-uuid"));
	}

	/**
	 * Custom data type class for testing
	 */
	public static class TestingDatatype extends SerializingCustomDatatype<Integer> {

		@Override
		public String serialize(Integer typedValue) {
			return typedValue != null ? String.valueOf(typedValue) : "";
		}

		@Override
		public Integer deserialize(String serializedValue) {
			return StringUtils.isNotEmpty(serializedValue) ? Integer.valueOf(serializedValue) : null;
		}
	}
}