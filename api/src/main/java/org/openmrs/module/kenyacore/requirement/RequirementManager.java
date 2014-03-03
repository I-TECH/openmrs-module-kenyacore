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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.kenyacore.ContentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Requirements manager
 */
@Component
public class RequirementManager implements ContentManager {

	protected static final Log log = LogFactory.getLog(RequirementManager.class);

	@Autowired(required = false)
	private Set<Requirement> requirements;

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0; // Refresh before anything else
	}

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#refresh()
	 */
	@Override
	public void refresh() {
		log.info("Checking all requirements...");

		if (requirements != null) {
			for (Requirement requirement : requirements) {
				boolean satisfied = requirement.isSatisfied();

				if (satisfied) {
					log.info("Requirement '" + requirement.getName() + "' is satisfied");
				} else {
					throw new UnsatisfiedRequirementException(requirement);
				}
			}
		}
	}

	/**
	 * Gets all requirements
	 * @return the requirements
	 */
	public Set<Requirement> getAllRequirements() {
		return requirements;
	}
}