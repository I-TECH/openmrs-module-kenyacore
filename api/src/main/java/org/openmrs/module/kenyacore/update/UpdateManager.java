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

package org.openmrs.module.kenyacore.update;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.ContentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Update manager
 */
@Component
public class UpdateManager implements ContentManager {

	protected static final Log log = LogFactory.getLog(UpdateManager.class);

	@Autowired
	private AdministrationService adminService;

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
		for (MaintenanceUpdate update : Context.getRegisteredComponents(MaintenanceUpdate.class)) {
			String className = update.getClass().getName();
			String ranGpName = "update." + className + ".ran";
			GlobalProperty ranGp = adminService.getGlobalPropertyObject(ranGpName);

			if (ranGp == null) {
				try {
					StringWriter writer = new StringWriter();
					update.run(new PrintWriter(writer));
					writer.close();

					log.info("Ran update component " + className);
				}
				catch (Exception ex) {
					throw new RuntimeException(ex);
				}

				ranGp = new GlobalProperty();
				ranGp.setProperty(ranGpName);
				ranGp.setPropertyValue("true");
				adminService.saveGlobalProperty(ranGp);
			}
			else {
				log.info("Skipping previously ran update component " + className);
			}
		}
	}
}