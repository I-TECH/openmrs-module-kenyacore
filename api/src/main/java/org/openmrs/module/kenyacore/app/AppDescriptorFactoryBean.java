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
import org.springframework.stereotype.Component;

/**
 * There's no nice way to create components of the AppDescriptor class defined in the App Framework module so we use
 * this factory bean.
 */
@Component("kenyacore.appFactoryBean")
public class AppDescriptorFactoryBean {

	/**
	 * Creates a new AppDescriptor instance
	 * @param id the app id (not the same as the bean id)
	 * @param label the label
	 * @param url the home page URL
	 * @param icon the icon
	 * @param order the order
	 * @return the app descriptor
	 */
	public AppDescriptor createInstance(String id, String label, String url, String icon, Integer order) {
		String requiredPrivilege = "App: " + id;

		return new AppDescriptor(id, null, label, url, icon, null, order, requiredPrivilege, null);
	}
}