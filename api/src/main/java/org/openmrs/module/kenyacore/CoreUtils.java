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

package org.openmrs.module.kenyacore;

import org.openmrs.calculation.ConfigurableCalculation;
import org.openmrs.calculation.patient.PatientCalculation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * General utility functions
 */
public class CoreUtils {

	/**
	 * Merges multiple collections into a list with natural ordering of elements
	 * @param collections the collections
	 * @param <T> the element type
	 * @return the merged list
	 */
	public static <T extends Comparable> List<T> merge(Collection<T>... collections) {
		Set<T> merged = new TreeSet<T>();

		for (Collection<T> list : collections) {
			for (T element : list) {
				merged.add(element);
			}
		}

		return new ArrayList(merged);
	}
}