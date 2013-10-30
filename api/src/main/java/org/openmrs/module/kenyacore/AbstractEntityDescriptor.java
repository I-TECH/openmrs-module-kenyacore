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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openmrs.OpenmrsObject;
import org.openmrs.util.OpenmrsUtil;

/**
 * Abstract base class for descriptors that target an existing OpenMRS object
 */
public abstract class AbstractEntityDescriptor<T extends OpenmrsObject> extends AbstractOrderedDescriptor {

	protected String targetUuid;

	/**
	 * Gets the target object UUID
	 * @return the target object UUID
	 */
	public String getTargetUuid() {
		return targetUuid;
	}

	/**
	 * Sets the target object UUID
	 * @param targetUuid the target object UUID
	 */
	public void setTargetUuid(String targetUuid) {
		this.targetUuid = targetUuid;
	}

	/**
	 * Gets the target object
	 * @return the target object
	 */
	public abstract T getTarget();

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("targetUuid", targetUuid)
				.append("enabled", enabled)
				.toString();
	}
}