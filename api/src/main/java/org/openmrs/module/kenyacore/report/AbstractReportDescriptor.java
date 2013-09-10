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
import org.openmrs.module.kenyacore.AbstractOrderedDescriptor;
import org.openmrs.module.reporting.definition.DefinitionSummary;

import java.util.Set;

/**
 * Abstract base class for report descriptors
 */
public abstract class AbstractReportDescriptor extends AbstractOrderedDescriptor implements ReportDescriptor {

	protected String name;

	protected String description;

	protected Set<AppDescriptor> apps;

	/**
	 * @see ReportDescriptor#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see ReportDescriptor#getName()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see ReportDescriptor#getApps()
	 */
	@Override
	public Set<AppDescriptor> getApps() {
		return apps;
	}

	/**
	 * Sets the apps
	 * @param apps the apps
	 */
	public void setApps(Set<AppDescriptor> apps) {
		this.apps = apps;
	}

	/**
	 * @see ReportDescriptor#getDefinitionSummary()
	 */
	@Override
	public DefinitionSummary getDefinitionSummary() {
		DefinitionSummary ret = new DefinitionSummary();
		ret.setName(getName());
		ret.setDescription(getDescription());
		ret.setUuid(id);
		return ret;
	}
}