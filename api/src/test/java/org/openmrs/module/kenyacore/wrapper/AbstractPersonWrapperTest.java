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
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractPersonWrapper}
 */
public class AbstractPersonWrapperTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private PersonService personService;

	/**
	 * @see AbstractPatientWrapper#lastObs(org.openmrs.Concept)
	 */
	@Test
	public void lastObs_shouldFindLastObsWithConcept() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(7));

		Concept cd4 = MetadataUtils.existing(Concept.class, StandardTestData._Concept.CD4_COUNT);
		Concept weight = MetadataUtils.existing(Concept.class, StandardTestData._Concept.WEIGHT_KG);

		Patient patient = TestUtils.getPatient(7);

		TestUtils.saveObs(patient, cd4, 123.0, TestUtils.date(2012, 1, 1));
		TestUtils.saveObs(patient, cd4, 234.0, TestUtils.date(2012, 1, 2));
		Obs obs = TestUtils.saveObs(patient, cd4, 345.0, TestUtils.date(2012, 1, 3));
		TestUtils.saveObs(patient, weight, 50.0, TestUtils.date(2012, 1, 31)); // Wrong concept

		Assert.assertThat(wrapper.lastObs(cd4), is(obs));
	}

	/**
	 * @see AbstractPersonWrapper#findFirstAttribute(String)
	 */
	@Test
	public void findFirstAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(7));
		Assert.assertThat(wrapper.findFirstAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE).getValue(), is("NULL"));
	}

	/**
	 * @see AbstractPersonWrapper#findFirstAttribute(String)
	 */
	@Test
	public void findFirstAttribute_shouldReturnNullIfNoAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(1));
		Assert.assertThat(wrapper.findFirstAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE), nullValue());
	}

	/**
	 * @see AbstractPersonWrapper#getAsAttribute(String)
	 */
	@Test
	public void getAsAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(7));
		Assert.assertThat(wrapper.getAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE), is("NULL"));
	}

	/**
	 * @see AbstractPersonWrapper#getAsAttribute(String)
	 */
	@Test
	public void getAsAttribute_shouldReturnNullIfNoAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(1));
		Assert.assertThat(wrapper.getAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE), nullValue());
	}

	/**
	 * @see AbstractPersonWrapper#setAsAttribute(String, String)
	 */
	@Test
	public void setAsAttribute_shouldUpdateExistingAttribute() {
		Person person = personService.getPerson(7);
		PersonWrapper wrapper = new PersonWrapper(person);

		wrapper.setAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE, "Nairobi");

		personService.savePerson(person);

		Assert.assertThat(wrapper.getAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE), is("Nairobi"));
		Assert.assertThat(person.getAttributes(2), hasSize(1));
	}

	/**
	 * @see AbstractPersonWrapper#setAsAttribute(String, String)
	 */
	@Test
	public void setAsAttribute_shouldCreateNewAttributeWhenNonExists() {
		Person person = personService.getPerson(1);
		PersonWrapper wrapper = new PersonWrapper(person);

		wrapper.setAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE, "Nairobi");

		personService.savePerson(person);

		Assert.assertThat(wrapper.getAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE), is("Nairobi"));
		Assert.assertThat(person.getAttributes(2), hasSize(1));
	}

	/**
	 * @see AbstractPersonWrapper#setAsAttribute(String, String)
	 */
	@Test
	public void setAsAttribute_shouldVoidIfAttributeValueIsBlank() {
		Person person = personService.getPerson(1);
		PersonWrapper wrapper = new PersonWrapper(person);

		wrapper.setAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE, "");

		personService.savePerson(person);

		Assert.assertThat(wrapper.getAsAttribute(StandardTestData._PersonAttributeType.BIRTHPLACE), nullValue());
		Assert.assertThat(person.getAttributes(2), hasSize(0));
	}

	/**
	 * Person wrapper class for testing
	 */
	private static class PersonWrapper extends AbstractPersonWrapper {

		public PersonWrapper(Person target) {
			super(target);
		}
	}
}