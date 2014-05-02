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

import org.openmrs.Concept;
import org.openmrs.module.metadatadeploy.MetadataUtils;

/**
 * Represents a reference to a concept
 */
public class ConceptReference {

	private String targetIdentifier;

	/**
	 * Creates a new concept reference
	 * @param targetIdentifier the target concept identifier
	 */
	public ConceptReference(String targetIdentifier) {
		this.targetIdentifier = targetIdentifier;
	}

	/**
	 * Gets the target concept
	 * @return the target concept
	 */
	public Concept getTarget() {
		return MetadataUtils.existing(Concept.class, targetIdentifier);
	}
}