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
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link TestUtils}
 */
public class TestUtilsTest extends BaseModuleContextSensitiveTest {

	@Test
	public void date_withoutTime() {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(TestUtils.date(2013, 3, 1));
		Assert.assertThat(cal1.get(Calendar.YEAR), is(2013));
		Assert.assertThat(cal1.get(Calendar.MONTH), is(2)); // Zero-based
		Assert.assertThat(cal1.get(Calendar.DATE), is(1));
		Assert.assertThat(cal1.get(Calendar.HOUR), is(0));
		Assert.assertThat(cal1.get(Calendar.MINUTE), is(0));
		Assert.assertThat(cal1.get(Calendar.SECOND), is(0));
		Assert.assertThat(cal1.get(Calendar.MILLISECOND), is(0));
	}

	@Test
	public void date_withTime() {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(TestUtils.date(2013, 3, 1, 5, 45, 30));
		Assert.assertThat(cal1.get(Calendar.YEAR), is(2013));
		Assert.assertThat(cal1.get(Calendar.MONTH), is(2)); // Zero-based
		Assert.assertThat(cal1.get(Calendar.DATE), is(1));
		Assert.assertThat(cal1.get(Calendar.HOUR), is(5));
		Assert.assertThat(cal1.get(Calendar.MINUTE), is(45));
		Assert.assertThat(cal1.get(Calendar.SECOND), is(30));
		Assert.assertThat(cal1.get(Calendar.MILLISECOND), is(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void date_shouldThrowExceptionForInvalidDates() {
		TestUtils.date(2013, 13, 32);
	}

	@Test
	public void getPatient_shouldReturnPatient() {
		Assert.assertThat(TestUtils.getPatient(6), is(Context.getPatientService().getPatient(6)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPatient_shouldThrowExceptionIfNoSuchPatient() {
		TestUtils.getPatient(777);
	}

	/**
	 * @see TestUtils#saveVisit(org.openmrs.Patient, org.openmrs.VisitType, java.util.Date, java.util.Date, org.openmrs.Encounter...)
	 */
	@Test
	public void saveVisit() {
		Patient patient = TestUtils.getPatient(7);
		VisitType initial = Context.getVisitService().getVisitType(1);
		Visit visit = TestUtils.saveVisit(patient, initial, TestUtils.date(2012, 1, 1), null);
		Assert.assertThat(visit.getId(), is(notNullValue()));
		Assert.assertThat(visit.getPatient(), is(patient));
		Assert.assertThat(visit.getStartDatetime(), is(TestUtils.date(2012, 1, 1)));
		Assert.assertThat(visit.getStopDatetime(), is(nullValue()));
		Assert.assertThat(visit.getUuid(), is(notNullValue()));
	}

	/**
	 * @see TestUtils#modifyConstant(Class, String, Object)
	 */
	@Test
	public void modifyConstant() throws Exception {
		Assert.assertThat(TestConstants.CONSTANT, is("XXX"));
		TestUtils.modifyConstant(TestConstants.class, "CONSTANT", "YYY");
		Assert.assertThat(TestConstants.CONSTANT, is("YYY"));
	}

	/**
	 * For testing modifyConstant
	 */
	protected static class TestConstants {
		// Value needs to be an expression or it will be inlined by the compiler and impossible to modify later
		private static final String CONSTANT = (null != null) ? "": "XXX";
	}
}