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

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Date;

/**
 * Custom Hamcrest matchers
 */
public class OpenmrsMatchers {

	/**
	 * The default Hamcrest matcher doesn't do well at comparing different date subclasses like java.sql.Timestamp
	 * @param expectedValue the the expected date value
	 * @return true if date matches
	 */
	public static Matcher<Date> isDate(final Object expectedValue) {
		return new BaseMatcher<Date>() {
			/**
			 * @see BaseMatcher#matches(Object)
			 */
			@Override
			public boolean matches(final Object actualValue) {
				Date expected = (Date) expectedValue;
				Date actual = (Date) actualValue;
				return actualValue != null && (actual.getTime() == expected.getTime());
			}

			/**
			 * @see BaseMatcher#describeTo(org.hamcrest.Description)
			 */
			@Override
			public void describeTo(final Description description) {
				description.appendText(expectedValue.toString());
			}

			/**
			 * @see BaseMatcher#describeMismatch(Object, org.hamcrest.Description)
			 */
			@Override
			public void describeMismatch(Object item, Description description) {
				description.appendText("was " + item.toString());
			}
		};
	}
}