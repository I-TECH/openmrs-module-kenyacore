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

package org.openmrs.module.kenyacore.metadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.kenyacore.ContentManager;
import org.openmrs.module.metadatadeploy.bundle.MetadataBundle;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatasharing.ImportedPackage;
import org.openmrs.module.metadatasharing.api.MetadataSharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Metadata package manager
 */
@Component
public class MetadataManager implements ContentManager {

	protected static final Log log = LogFactory.getLog(MetadataManager.class);

	@Autowired
	private MetadataDeployService deployService;

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#getPriority()
	 */
	@Override
	public int getPriority() {
		return 10; // First because others will use metadata loaded by it
	}

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#refresh()
	 */
	@Override
	public synchronized void refresh() {
		// Process configuration beans
		for (MetadataConfiguration configuration : Context.getRegisteredComponents(MetadataConfiguration.class)) {
			String moduleId = configuration.getModuleId();
			ClassLoader loader =  ModuleFactory.getModuleClassLoader(moduleId);

			try {
				loadPackages(configuration.getPackages(), loader);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error occured while loading metadata packages from " + moduleId, ex);
			}
		}

		// Install bundle components
		deployService.installBundles(Context.getRegisteredComponents(MetadataBundle.class));
	}

	/**
	 * Gets all imported packages in the system
	 * @return the packages
	 */
	public List<ImportedPackage> getImportedPackages() {
		return Context.getService(MetadataSharingService.class).getAllImportedPackages();
	}

	/**
	 * Loads packages specified in an XML packages list
	 * @param packages the map of groupUuids to package filenames
	 * @param loader the class loader to use for loading the packages (null to use the default)
	 * @return whether any changes were made to the db
	 * @throws Exception
	 */
	protected boolean loadPackages(Map<String, String> packages, ClassLoader loader) throws Exception {
		boolean anyChanges = false;

		for (Map.Entry<String, String> entry : packages.entrySet()) {
			String groupUuid = entry.getKey();
			String filename = entry.getValue();

			anyChanges |= deployService.installPackage(filename, loader, groupUuid);
		}

		return anyChanges;
	}
}