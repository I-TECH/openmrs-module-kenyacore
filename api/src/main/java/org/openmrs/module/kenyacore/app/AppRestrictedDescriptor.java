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

package org.openmrs.module.kenyacore.app;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.kenyacore.Descriptor;

import java.util.Set;

/**
 * Describes entities which access to is restricted by app
 */
public interface AppRestrictedDescriptor extends Descriptor {

	/**
	 * Gets the apps allowed to access this
	 * @return the apps descriptors
	 */
	Set<AppDescriptor> getApps();

	/**
	 * Sets the apps allowed to access this
	 * @param apps the app descriptors
	 */
	void setApps(Set<AppDescriptor> apps);
}