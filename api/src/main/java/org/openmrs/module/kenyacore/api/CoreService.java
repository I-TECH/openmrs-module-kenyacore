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

package org.openmrs.module.kenyacore.api;

import org.openmrs.api.APIException;
import org.openmrs.module.kenyacore.ContentManager;
import org.openmrs.module.kenyacore.chore.Chore;
import org.springframework.transaction.annotation.Transactional;

/**
 * Module service implementation
 */
@Transactional
public interface CoreService {

	/**
	 * Refreshes a content manager
	 * @param manager the manager
	 */
	void refreshManager(ContentManager manager) throws APIException;

	/**
	 * Performs the given chore
	 * @param chore the chore
	 */
	void performChore(Chore chore) throws APIException;
}