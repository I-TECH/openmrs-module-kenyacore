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
		EncounterType obj = findExistingEncounterType(name, uuid);
		if (obj == null) {
			obj = new EncounterType();
		}

		obj.setName(name);
		obj.setDescription(description);
		obj.setUuid(uuid);

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
		Form obj = findExistingForm(name, version, uuid);
		if (obj == null) {
			obj = new Form();
		}

		obj.setName(name);
		obj.setDescription(description);
		obj.setEncounterType(MetadataUtils.getEncounterType(encTypeUuid));
		obj.setVersion(version);
		obj.setUuid(uuid);

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
		Program obj = findExistingProgram(name, uuid);
		if (obj == null) {
			obj = new Program();
		}

		obj.setName(name);
		obj.setDescription(description);
		obj.setConcept(MetadataUtils.getConcept(concept));
		obj.setUuid(uuid);

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
		}

		obj.setName(name);
		obj.setDescription(description);
		obj.setUuid(uuid);

		return Context.getVisitService().saveVisitType(obj);
	}

	/**
	 * Finds an existing encounter type
	 * @param name the name
	 * @param uuid the uuid
	 * @return the encounter type or null
	 */
	protected EncounterType findExistingEncounterType(String name, String uuid) {
		EncounterType obj = Context.getEncounterService().getEncounterTypeByUuid(uuid);
		if (obj != null) {
			return obj;
		}

		return Context.getEncounterService().getEncounterType(name);
	}

	/**
	 * Finds an existing form
	 * @param name the name
	 * @param version the version
	 * @param uuid the uuid
	 * @return the form or null
	 */
	protected Form findExistingForm(String name, String version, String uuid) {
		Form obj = Context.getFormService().getFormByUuid(uuid);
		if (obj != null) {
			return obj;
		}

		return Context.getFormService().getForm(name, version);
	}

	/**
	 * Finds an existing program
	 * @param name the name
	 * @param uuid the uuid
	 * @return the program or null
	 */
	protected Program findExistingProgram(String name, String uuid) {
		Program obj = Context.getProgramWorkflowService().getProgramByUuid(uuid);
		if (obj != null) {
			return obj;
		}

		// In 1.9.x getProgramByName incorrectly looks at concept name (TRUNK-3504)
		for (Program p : Context.getProgramWorkflowService().getAllPrograms(true)) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}
}