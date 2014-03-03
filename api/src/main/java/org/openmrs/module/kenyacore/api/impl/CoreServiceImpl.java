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

package org.openmrs.module.kenyacore.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.kenyacore.ContentManager;
import org.openmrs.module.kenyacore.CoreUtils;
import org.openmrs.module.kenyacore.api.CoreService;
import org.openmrs.module.kenyacore.chore.Chore;

import java.io.PrintWriter;

/**
 * Module service implementation
 */
public class CoreServiceImpl extends BaseOpenmrsService implements CoreService {

	protected static final Log log = LogFactory.getLog(CoreService.class);

	/**
	 * @see CoreService#refreshManager(org.openmrs.module.kenyacore.ContentManager)
	 */
	@Override
	public void refreshManager(ContentManager manager) {
		log.info("Refreshing " + manager.getClass().getName() + "...");

		long start = System.currentTimeMillis();
		manager.refresh();

		// A content manager might load a lot of stuff into Hibernate's cache
		Context.flushSession();
		Context.clearSession();

		long time = System.currentTimeMillis() - start;

		log.info("Refreshed " + manager.getClass().getName() + " in " + time + "ms");
	}

	/**
	 * @see CoreService#performChore(org.openmrs.module.kenyacore.chore.Chore)
	 */
	@Override
	public void performChore(Chore chore) {
		log.info("Performing chore '" + chore.getId() + "'...");

		PrintWriter writer = new PrintWriter(System.out);

		long start = System.currentTimeMillis();
		chore.perform(writer);

		writer.flush();

		Context.flushSession();
		Context.clearSession();

		long time = System.currentTimeMillis() - start;

		log.info("Performed chore '" + chore.getId() + "' in " + time + "ms");

		CoreUtils.setGlobalProperty(chore.getId() + ".done", "true");
	}
}