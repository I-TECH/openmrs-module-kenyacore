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

package org.openmrs.module.kenyacore.lab;

import org.openmrs.module.kenyacore.AbstractContentConfiguration;

import java.util.Set;

/**
 * Configuration bean class for lab
 */
public class LabConfiguration extends AbstractContentConfiguration {

	private Set<LabTestCatalog> commonCatalogs;

	/**
	 * Gets the general test catalogs
	 * @return the catalogs
	 */
	public Set<LabTestCatalog> getCommonCatalogs() {
		return commonCatalogs;
	}

	/**
	 * Sets the general catalogs
	 * @param commonCatalogs the catalogs
	 */
	public void setCommonCatalogs(Set<LabTestCatalog> commonCatalogs) {
		this.commonCatalogs = commonCatalogs;
	}
}