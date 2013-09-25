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
import org.openmrs.module.kenyacore.metadata.bundle.AbstractMetadataBundle;
import org.openmrs.module.kenyacore.metadata.bundle.MetadataBundle;
import org.openmrs.module.kenyacore.metadata.bundle.Requires;
import org.openmrs.module.metadatasharing.ImportConfig;
import org.openmrs.module.metadatasharing.ImportMode;
import org.openmrs.module.metadatasharing.ImportedPackage;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.api.MetadataSharingService;
import org.openmrs.module.metadatasharing.wrapper.PackageImporter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metadata package manager
 */
@Component
public class MetadataManager implements ContentManager {

	protected static final Log log = LogFactory.getLog(MetadataManager.class);

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
		installMetadataBundles(Context.getRegisteredComponents(MetadataBundle.class));
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

			anyChanges |= ensurePackageInstalled(groupUuid, filename, loader);
		}

		return anyChanges;
	}

	/**
	 * Checks whether the given version of the MDS package has been installed yet, and if not, install it
	 * @param groupUuid the package group UUID
	 * @param filename the package filename
	 * @param loader the class loader to use for loading the packages (null to use the default)
	 * @return whether any changes were made to the db
	 * @throws IOException
	 */
	protected static boolean ensurePackageInstalled(String groupUuid, String filename, ClassLoader loader) throws IOException {
		try {
			Matcher matcher = Pattern.compile("[\\w/-]+-(\\d+).zip").matcher(filename);
			if (!matcher.matches())
				throw new RuntimeException("Filename must match PackageNameWithNoSpaces-X.zip");
			Integer version = Integer.valueOf(matcher.group(1));

			ImportedPackage installed = Context.getService(MetadataSharingService.class).getImportedPackageByGroup(groupUuid);
			if (installed != null && installed.getVersion() >= version) {
				log.info("Metadata package " + filename + " is already installed with version " + installed.getVersion());
				return false;
			}

			if (loader == null) {
				loader = MetadataManager.class.getClassLoader();
			}

			if (loader.getResource(filename) == null) {
				throw new RuntimeException("Cannot find " + filename + " for group " + groupUuid);
			}

			PackageImporter metadataImporter = MetadataSharing.getInstance().newPackageImporter();
			metadataImporter.setImportConfig(ImportConfig.valueOf(ImportMode.MIRROR));
			metadataImporter.loadSerializedPackageStream(loader.getResourceAsStream(filename));
			metadataImporter.importPackage();

			log.debug("Loaded metadata package '" + filename + "'");

			return true;

		} catch (Exception ex) {
			throw new RuntimeException("Failed to install metadata package " + filename, ex);
		}
	}

	/**
	 * Processes the given list of metadata bundles
	 * @param bundles the bundles
	 */
	protected void installMetadataBundles(List<MetadataBundle> bundles) {
		// Organize into map by class
		Map<Class<? extends MetadataBundle>, MetadataBundle> all = new HashMap<Class<? extends MetadataBundle>, MetadataBundle>();
		for (MetadataBundle bundle : bundles) {
			all.put(bundle.getClass(), bundle);
		}

		// Begin recursive processing
		Set<MetadataBundle> installed = new HashSet<MetadataBundle>();
		for (MetadataBundle bundle : bundles) {
			installMetadataBundle(bundle, all, installed);
		}
	}

	/**
	 * Installs a metadata bundle by recursively installing it's required bundles
	 * @param bundle the bundle
	 * @param all the map of all bundles and their ids
	 * @param installed the set of previously installed bundles
	 */
	protected void installMetadataBundle(MetadataBundle bundle, Map<Class<? extends MetadataBundle>, MetadataBundle> all, Set<MetadataBundle> installed) {
		// Return immediately if bundle has already been installed
		if (installed.contains(bundle)) {
			return;
		}

		// Install required bundles first
		Requires requires = bundle.getClass().getAnnotation(Requires.class);
		if (requires != null) {
			for (Class<? extends MetadataBundle> requiredClass : requires.value()) {
				MetadataBundle required = all.get(requiredClass);

				if (required == null) {
					throw new RuntimeException("Can't find required bundle class " + requiredClass + " for " + bundle.getClass());
				}

				installMetadataBundle(required, all, installed);
			}
		}

		bundle.install();
		installed.add(bundle);

		Context.flushSession();
	}
}