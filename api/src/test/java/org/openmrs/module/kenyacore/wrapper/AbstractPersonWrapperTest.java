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
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AbstractPersonWrapper}
 */
public class AbstractPersonWrapperTest extends BaseModuleContextSensitiveTest {

	private static final String BIRTHPLACE_ATTRTYPE_UUID = "54fc8400-1683-4d71-a1ac-98d40836ff7c";

	@Autowired
	private PersonService personService;

	/**
	 * @see AbstractPersonWrapper#findFirstAttribute(String)
	 */
	@Test
	public void findFirstAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(7));
		Assert.assertThat(wrapper.findFirstAttribute(BIRTHPLACE_ATTRTYPE_UUID).getValue(), is("NULL"));
	}

	/**
	 * @see AbstractPersonWrapper#findFirstAttribute(String)
	 */
	@Test
	public void findFirstAttribute_shouldReturnNullIfNoAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(1));
		Assert.assertThat(wrapper.findFirstAttribute(BIRTHPLACE_ATTRTYPE_UUID), nullValue());
	}

	/**
	 * @see AbstractPersonWrapper#getAsAttribute(String)
	 */
	@Test
	public void getAsAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(7));
		Assert.assertThat(wrapper.getAsAttribute(BIRTHPLACE_ATTRTYPE_UUID), is("NULL"));
	}

	/**
	 * @see AbstractPersonWrapper#getAsAttribute(String)
	 */
	@Test
	public void getAsAttribute_shouldReturnNullIfNoAttribute() {
		PersonWrapper wrapper = new PersonWrapper(personService.getPerson(1));
		Assert.assertThat(wrapper.getAsAttribute(BIRTHPLACE_ATTRTYPE_UUID), nullValue());
	}

	/**
	 * @see AbstractPersonWrapper#setAsAttribute(String, String)
	 */
	@Test
	public void setAsAttribute_shouldUpdateExistingAttribute() {
		Person person = personService.getPerson(7);
		PersonWrapper wrapper = new PersonWrapper(person);

		wrapper.setAsAttribute(BIRTHPLACE_ATTRTYPE_UUID, "Nairobi");

		personService.savePerson(person);

		Assert.assertThat(wrapper.getAsAttribute(BIRTHPLACE_ATTRTYPE_UUID), is("Nairobi"));
		Assert.assertThat(person.getAttributes(2), hasSize(1));
	}

	/**
	 * @see AbstractPersonWrapper#setAsAttribute(String, String)
	 */
	@Test
	public void setAsAttribute_shouldCreateNewAttributeWhenNonExists() {
		Person person = personService.getPerson(1);
		PersonWrapper wrapper = new PersonWrapper(person);

		wrapper.setAsAttribute(BIRTHPLACE_ATTRTYPE_UUID, "Nairobi");

		personService.savePerson(person);

		Assert.assertThat(wrapper.getAsAttribute(BIRTHPLACE_ATTRTYPE_UUID), is("Nairobi"));
		Assert.assertThat(person.getAttributes(2), hasSize(1));
	}

	/**
	 * @see AbstractPersonWrapper#setAsAttribute(String, String)
	 */
	@Test
	public void setAsAttribute_shouldVoidIfAttributeValueIsBlank() {
		Person person = personService.getPerson(1);
		PersonWrapper wrapper = new PersonWrapper(person);

		wrapper.setAsAttribute(BIRTHPLACE_ATTRTYPE_UUID, "");

		personService.savePerson(person);

		Assert.assertThat(wrapper.getAsAttribute(BIRTHPLACE_ATTRTYPE_UUID), nullValue());
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