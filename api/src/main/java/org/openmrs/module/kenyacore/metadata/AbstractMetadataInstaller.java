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

import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Program;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.AbstractDescriptor;

/**
 * Abstract base class for metadata installer components
 */
public abstract class AbstractMetadataInstaller extends AbstractDescriptor {

	/**
	 * Performs the installation of metadata items
	 */
	public abstract void install();

	/**
	 * Installs an encounter type
	 * @param name the name
	 * @param description the description
	 * @param uuid the UUID
	 * @return the encounter type
	 */
	public EncounterType installEncounterType(String name, String description, String uuid) {
		EncounterType obj = Context.getEncounterService().getEncounterTypeByUuid(uuid);
		if (obj == null) {
			obj = new EncounterType();
			obj.setUuid(uuid);
		}
		obj.setName(name);
		obj.setDescription(description);

		return Context.getEncounterService().saveEncounterType(obj);
	}

	/**
	 * Installs a form
	 * @param name the name
	 * @param description the description
	 * @param encTypeUuid the encounter type UUID
	 * @param uuid the UUID
	 * @return the form
	 */
	public Form installForm(String name, String description, String encTypeUuid, String version, String uuid) {
		Form obj = Context.getFormService().getFormByUuid(uuid);
		if (obj == null) {
			obj = new Form();
			obj.setUuid(uuid);
		}
		obj.setName(name);
		obj.setDescription(description);
		obj.setEncounterType(MetadataUtils.getEncounterType(encTypeUuid));
		obj.setVersion(version);

		return Context.getFormService().saveForm(obj);
	}

	/**
	 * Installs a program
	 * @param name the name
	 * @param description the description
	 * @param concept the concept identifier
	 * @param uuid the UUID
	 * @return the program
	 */
	public Program installProgram(String name, String description, String concept, String uuid) {
		Program obj = Context.getProgramWorkflowService().getProgramByUuid(uuid);
		if (obj == null) {
			obj = new Program();
			obj.setUuid(uuid);
		}
		obj.setName(name);
		obj.setDescription(description);
		obj.setConcept(MetadataUtils.getConcept(concept));

		return Context.getProgramWorkflowService().saveProgram(obj);
	}

	/**
	 * Installs a visit type
	 * @param name the name
	 * @param description the description
	 * @param uuid the UUID
	 * @return the visit type
	 */
	public VisitType installVisitType(String name, String description, String uuid) {
		VisitType obj = Context.getVisitService().getVisitTypeByUuid(uuid);
		if (obj == null) {
			obj = new VisitType();
			obj.setUuid(uuid);
		}
		obj.setName(name);
		obj.setDescription(description);

		return Context.getVisitService().saveVisitType(obj);
	}
}