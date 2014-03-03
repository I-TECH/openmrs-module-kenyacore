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
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.metadatadeploy.MissingMetadataException;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ConceptReference}
 */
public class ConceptReferenceTest extends BaseModuleContextSensitiveTest {

	@Test
	public void getTarget_shouldReturnConceptIfReferenceIsValid() throws Exception {
		ConceptReference reference = new ConceptReference(StandardTestData._Concept.WEIGHT_KG);

		Assert.assertThat(reference.getTarget(), is(Context.getConceptService().getConcept(5089)));
	}

	@Test(expected = MissingMetadataException.class)
	public void getTarget_shouldThrowExceptionIfReferenceIsInvalid() throws Exception {
		ConceptReference reference = new ConceptReference("xxxxx");
		reference.getTarget();
	}
}