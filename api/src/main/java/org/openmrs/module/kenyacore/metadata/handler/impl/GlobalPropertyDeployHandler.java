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

package org.openmrs.module.kenyacore.metadata.handler.impl;

import org.openmrs.GlobalProperty;
import org.openmrs.annotation.Handler;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Deployment handler for global properties
 */
@Handler(supports = { GlobalProperty.class })
public class GlobalPropertyDeployHandler implements ObjectDeployHandler<GlobalProperty> {

	@Autowired
	@Qualifier("adminService")
	private AdministrationService adminService;

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#fetch(String)
	 */
	@Override
	public GlobalProperty fetch(String uuid) {
		return adminService.getGlobalPropertyByUuid(uuid);
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#save(org.openmrs.OpenmrsObject)
	 */
	@Override
	public GlobalProperty save(GlobalProperty obj) {
		return adminService.saveGlobalProperty(obj);
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#findAlternateMatch(org.openmrs.OpenmrsObject)
	 */
	@Override
	public GlobalProperty findAlternateMatch(GlobalProperty incoming) {
		return adminService.getGlobalPropertyObject(incoming.getProperty());
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#remove(org.openmrs.OpenmrsObject, String)
	 * @param obj the object to remove
	 */
	@Override
	public void remove(GlobalProperty obj, String reason) {
		adminService.purgeGlobalProperty(obj);
	}
}