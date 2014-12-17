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

import org.openmrs.module.kenyacore.UiResource;
import org.openmrs.module.kenyacore.identifier.IdentifierDescriptor;

/**
 * A indicator report with patient level data with an associated template
 */
public class HybridReportDescriptor extends ReportDescriptor {

	protected UiResource template;
	protected IdentifierDescriptor displayIdentifier;
	protected String repeatingSection;

	/**
	 * Gets the template (e.g. an Excel file)
	 * @return the template
	 */
	public UiResource getTemplate() {
		return template;
	}

	/**
	 * Sets the template (e.g. an Excel file)
	 * @param template the template
	 */
	public void setTemplate(UiResource template) {
		this.template = template;
	}

	/**
	 * Gets the identifier type to display for each patient
	 * @return the identifier
	 */
	public IdentifierDescriptor getDisplayIdentifier() {
		return displayIdentifier;
	}

	/**
	 * Sets the identifier type to display for each patient
	 * @param displayIdentifier
	 */
	public void setDisplayIdentifier(IdentifierDescriptor displayIdentifier) {
		this.displayIdentifier = displayIdentifier;
	}

	/**
	 * gets a string for repeating section
	 * @return string configuration for repeating section
	 */
	public String getRepeatingSection() {
		return repeatingSection;
	}

	/**
	 * provides a string for repeating section
	 * The string is expected to specify sheet i.e 1,2 etc, row i.e 10, and dataset i.e allPatients
	 * an example is "sheet:1,row:11,dataset:allPatients"
	 * @param repeatingSection
	 */
	public void setRepeatingSection(String repeatingSection) {
		this.repeatingSection = repeatingSection;
	}
}