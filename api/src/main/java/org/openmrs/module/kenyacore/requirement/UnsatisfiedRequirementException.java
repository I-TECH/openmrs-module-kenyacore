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

package org.openmrs.module.kenyacore.requirement;

/**
 * Exception class for unsatisfied requirements
 */
public class UnsatisfiedRequirementException extends RuntimeException {

	private Requirement requirement;

	/**
	 * Createst a new exception
	 * @param requirement the unsatisfied requirement
	 */
	public UnsatisfiedRequirementException(Requirement requirement) {
		super("Requirement '" + requirement.getName() + "' is not satisfied. Required " + requirement.getRequiredVersion() + ", but found " + requirement.getFoundVersion());

		this.requirement = requirement;
	}

	/**
	 * Gets the unsatisfied requirement
	 * @return the requirement
	 */
	public Requirement getRequirement() {
		return requirement;
	}
}