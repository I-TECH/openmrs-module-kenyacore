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

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;

import java.util.Collections;
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
}