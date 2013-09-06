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

package org.openmrs.module.kenyacore.report;

import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.reporting.definition.DefinitionSummary;

import java.util.Set;

/**
 * Describes a report
 */
public interface ReportDescriptor {

	/**
	 * Gets the report id
	 * @return the id
	 */
	public String getId();

	/**
	 * Gets the report name
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the report description
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Gets the apps
	 * @return the apps
	 */
	public Set<AppDescriptor> getApps();

	/**
	 * Gets a definition summary
	 * @return the definition summary
	 */
	public DefinitionSummary getDefinitionSummary();
}