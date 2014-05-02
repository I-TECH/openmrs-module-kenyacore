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

package org.openmrs.module.kenyacore.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Abstract wrapper class for patients
 */
public abstract class AbstractPatientWrapper extends AbstractPersonWrapper<Patient> {

	/**
	 * Creates a new patient wrapper
	 * @param target the target
	 */
	public AbstractPatientWrapper(Patient target) {
		super(target);
	}

	/**
	 * Fetches all encounters with the given type in chronological order
	 * @param type the encounter type
	 * @return the encounters
	 */
	public List<Encounter> allEncounters(EncounterType type) {
		return Context.getEncounterService().getEncounters(target, null, null, null, null, Collections.singleton(type), null, null, null, false);
	}

	/**
	 * Fetches all encounters from the given form in chronological order
	 * @param form the encounter form
	 * @return the encounters
	 */
	public List<Encounter> allEncounters(Form form) {
		return Context.getEncounterService().getEncounters(target, null, null, null, Collections.singleton(form), null, null, null, null, false);
	}

	/**
	 * Finds the last encounter with the given encounter type
	 * @param type the encounter type
	 * @return the encounter
	 */
	public Encounter firstEncounter(EncounterType type) {
		List<Encounter> encounters = allEncounters(type);
		return encounters.size() > 0 ? encounters.get(0) : null;
	}

	/**
	 * Finds the last encounter with the given encounter type
	 * @param type the encounter type
	 * @return the encounter
	 */
	public Encounter lastEncounter(EncounterType type) {
		List<Encounter> encounters = allEncounters(type);
		return encounters.size() > 0 ? encounters.get(encounters.size() - 1) : null;
	}

	/**
	 * Gets the value of any identifier of the given type
	 * @param idTypeUuid the identifier type UUID
	 * @return the identifier value (or null)
	 */
	public String getAsIdentifier(String idTypeUuid) {
		PatientIdentifierType type = MetadataUtils.existing(PatientIdentifierType.class, idTypeUuid);
		PatientIdentifier identifier = target.getPatientIdentifier(type);
		return identifier != null ? identifier.getIdentifier() : null;
	}

	/**
	 * Sets the value of any identifier of the given type
	 * @param idTypeUuid the identifier type UUID
	 * @param value the identifier value
	 * @param location the identifier location
	 * @return the identifier object which should be saved
	 */
	public PatientIdentifier setAsIdentifier(String idTypeUuid, String value, Location location) {
		PatientIdentifierType type = MetadataUtils.existing(PatientIdentifierType.class, idTypeUuid);
		PatientIdentifier identifier = target.getPatientIdentifier(type);

		if (StringUtils.isNotBlank(value)) {
			if (identifier == null) {
				identifier = new PatientIdentifier();
				identifier.setIdentifierType(type);
				target.addIdentifier(identifier);
			}

			identifier.setIdentifier(value);
			identifier.setLocation(location);
		}
		else if (identifier != null) {
			identifier.setVoided(true);
			identifier.setDateVoided(new Date());
			identifier.setVoidedBy(Context.getAuthenticatedUser());
			identifier.setVoidReason("Removed");
		}

		return identifier;
	}
}