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

import org.openmrs.Role;
import org.openmrs.annotation.Handler;
import org.openmrs.api.UserService;
import org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Deployment handler for roles
 */
@Handler(supports = { Role.class })
public class RoleDeployHandler implements ObjectDeployHandler<Role> {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#fetch(String)
	 */
	@Override
	public Role fetch(String uuid) {
		return userService.getRoleByUuid(uuid);
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#save(org.openmrs.OpenmrsObject)
	 */
	@Override
	public Role save(Role obj) {
		return userService.saveRole(obj);
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#findAlternateMatch(org.openmrs.OpenmrsObject)
	 */
	@Override
	public Role findAlternateMatch(Role incoming) {
		return userService.getRole(incoming.getRole());
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#remove(org.openmrs.OpenmrsObject, String)
	 * @param obj the object to remove
	 */
	@Override
	public void remove(Role obj, String reason) {
		userService.purgeRole(obj);
	}
}