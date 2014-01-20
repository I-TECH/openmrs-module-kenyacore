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

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.kenyacore.AbstractEntityDescriptor;
import org.openmrs.module.kenyacore.app.AppRestrictedDescriptor;
import org.openmrs.module.reporting.definition.DefinitionSummary;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;

import java.util.Set;

/**
 * Describes a report
 */
public class ReportDescriptor extends AbstractEntityDescriptor<ReportDefinition> implements AppRestrictedDescriptor {

	protected String name;

	protected String description;

	protected Set<AppDescriptor> apps;

	/**
	 * @see org.openmrs.module.kenyacore.AbstractEntityDescriptor#getTarget()
	 */
	@Override
	public ReportDefinition getTarget() {
		return Context.getService(ReportDefinitionService.class).getDefinitionByUuid(targetUuid);
	}

	/**
	 * Gets the name
	 * @return the name
	 */
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
	 * Gets the description
	 * @return the description
	 */
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
	 * @see org.openmrs.module.kenyacore.app.AppRestrictedDescriptor#getApps()
	 */
	@Override
	public Set<AppDescriptor> getApps() {
		return apps;
	}

	/**
	 * @see org.openmrs.module.kenyacore.app.AppRestrictedDescriptor#setApps(java.util.Set)
	 */
	@Override
	public void setApps(Set<AppDescriptor> apps) {
		this.apps = apps;
	}

	/**
	 * Gets a definition summary
	 */
	public DefinitionSummary getDefinitionSummary() {
		DefinitionSummary ret = new DefinitionSummary();
		ret.setName(getName());
		ret.setDescription(getDescription());
		ret.setUuid(id);
		return ret;
	}
}