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

package org.openmrs.module.kenyacore.converter;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.ConceptReference;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link StringToConceptReferenceConverter}
 */
public class StringToConceptReferenceConverterTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private StringToConceptReferenceConverter converter;

	@Test
	public void convert() {
		Concept cd4 = Context.getConceptService().getConcept(5497);

		ConceptReference conceptRef1 = converter.convert(StandardTestData._Concept.CD4_COUNT);
		Assert.assertThat(conceptRef1.getTarget(), is(cd4));

		// Empty strings should convert to nulls
		Assert.assertNull(converter.convert(null));
		Assert.assertNull(converter.convert(""));
	}
}