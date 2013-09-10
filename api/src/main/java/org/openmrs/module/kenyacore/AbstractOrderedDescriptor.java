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

import org.openmrs.util.OpenmrsUtil;

/**
 * Abstract base class for descriptors which are comparable via an order property
 */
public abstract class AbstractOrderedDescriptor extends AbstractDescriptor implements Comparable<AbstractOrderedDescriptor> {

	protected Integer order;

	/**
	 * Gets the order
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * Sets the order
	 * @param order the order
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @see Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(AbstractOrderedDescriptor descriptor) {
		int byOrder = OpenmrsUtil.compareWithNullAsGreatest(order, descriptor.order);

		// Return by id if order is equal. Important to not return zero from this method unless the objects
		// *are* actually equal. Otherwise TreeSet sees them as duplicates.
		return byOrder != 0 ? byOrder : id.compareTo(descriptor.id);
	}
}