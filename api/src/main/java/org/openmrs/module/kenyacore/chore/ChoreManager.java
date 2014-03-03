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

package org.openmrs.module.kenyacore.chore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.ContentManager;
import org.openmrs.module.kenyacore.api.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Chore manager
 */
@Component
public class ChoreManager implements ContentManager {

	protected static final Log log = LogFactory.getLog(ChoreManager.class);

	@Autowired
	private AdministrationService adminService;

	@Autowired
	private CoreService coreService;

	@Autowired(required = false)
	private Collection<Chore> allChores;

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#getPriority()
	 */
	@Override
	public int getPriority() {
		return 100; // After everything else
	}

	/**
	 * @see org.openmrs.module.kenyacore.ContentManager#refresh()
	 */
	@Override
	public synchronized void refresh() {
		// Perform all registered chore components (that haven't already been run)
		performChores(allChores);
	}

	/**
	 * Performs the given chores, ignoring those which have been performed previously
	 * @param chores the chores
	 */
	public void performChores(Collection<Chore> chores) {
		// Organize all available chores into map by class
		Map<Class<? extends Chore>, Chore> all = new HashMap<Class<? extends Chore>, Chore>();
		for (Chore chore : allChores) {
			all.put(chore.getClass(), chore);
		}

		// Begin recursive processing
		Set<Chore> performed = new HashSet<Chore>();
		for (Chore chore : chores) {
			performChore(chore, all, performed);
		}
	}

	/**
	 * Performs a chore by recursively performing it's required chores
	 * @param chore the chore
	 * @param all the map of all chores and their classes
	 * @param performed the set of previously performed chores
	 */
	protected void performChore(Chore chore, Map<Class<? extends Chore>, Chore> all, Set<Chore> performed) {
		// Return immediately if update has already been run
		if (performed.contains(chore)) {
			return;
		}

		try {
			// Perform required chores first
			Requires requires = chore.getClass().getAnnotation(Requires.class);
			if (requires != null) {
				for (Class<? extends Chore> requiredClass : requires.value()) {
					Chore required = all.get(requiredClass);

					if (required == null) {
						throw new RuntimeException("Can't find required chore class " + requiredClass + " for " + chore.getClass());
					}

					performChore(required, all, performed);
				}
			}

			if (!isChorePerformed(chore)) {
				coreService.performChore(chore);
			}
			else {
				log.info("Skipping previously performed chore '" + chore.getId() + "'");
			}

			performed.add(chore);

			Context.flushSession();
		}
		catch (Exception ex) {
			throw new APIException("Unable to perform chore: " + chore.getClass().getSimpleName(), ex);
		}
	}

	/**
	 * Checks whether a chore has been performed
	 * @param chore the chore
	 * @return true if chore has been performed
	 */
	public boolean isChorePerformed(Chore chore) {
		GlobalProperty gp = adminService.getGlobalPropertyObject(chore.getId() + ".done");
		return !(gp == null || gp.getPropertyValue().equals("false"));
	}
}