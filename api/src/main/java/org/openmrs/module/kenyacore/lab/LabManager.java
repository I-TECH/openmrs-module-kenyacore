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

import org.openmrs.Concept;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.ContentManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Lab manager
 */
@Component
public class LabManager implements ContentManager {

	private List<LabTestCatalog> commonCatalogs = new ArrayList<LabTestCatalog>();

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#getPriority()
	 */
	@Override
	public int getPriority() {
		return 20;
	}

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#refresh()
	 */
	@Override
	public synchronized void refresh() {
		commonCatalogs.clear();

		// Process form configuration beans
		for (LabConfiguration configuration : Context.getRegisteredComponents(LabConfiguration.class)) {
			// Register common test catalogs
			if (configuration.getCommonCatalogs() != null) {
				commonCatalogs.addAll(configuration.getCommonCatalogs());
			}
		}
	}

	/**
	 * Gets the categories
	 * @return the list of categories
	 */
	public List<String> getCategories() {
		List<String> categories = new ArrayList<String>();

		for (LabTestCatalog catalog : commonCatalogs) {
			categories.addAll(catalog.getTests().keySet());
		}

		return categories;
	}

	/**
	 * Gets the lab tests for the given category
	 * @param category
	 * @return the list of tests
	 */
	public List<LabTestDefinition> getTests(String category) {

		for (LabTestCatalog catalog : commonCatalogs) {
			if (catalog.getTests().containsKey(category)) {
				return catalog.getTests().get(category);
			}
		}

		return null;
	}

	/**
	 * Gets whether the given concept is a registered lab test concept
	 * @param concept the concept
	 * @return true if concept is a lab test
	 */
	public boolean isLabTest(Concept concept) {
		for (LabTestCatalog catalog : commonCatalogs) {
			for (Map.Entry<String, List<LabTestDefinition>> entry : catalog.getTests().entrySet()) {
				for (LabTestDefinition test : entry.getValue()) {
					if (test.getConcept().getTarget().equals(concept)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets all test catalogs appropriate for this visit
	 * @param visit the visit
	 * @return the catalog descriptors
	 */
	public List<LabTestCatalog> getCatalogsForVisit(Visit visit) {
		return new ArrayList<LabTestCatalog>(commonCatalogs);
	}
}
