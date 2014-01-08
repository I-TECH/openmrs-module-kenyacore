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

import org.openmrs.BaseCustomizableMetadata;
import org.openmrs.attribute.Attribute;
import org.openmrs.customdatatype.CustomValueDescriptor;

import java.util.List;

/**
 * Abstract base class for wrappers of customizable (i.e. have attributes) objects
 */
public abstract class AbstractCustomizableWrapper<T extends BaseCustomizableMetadata> extends AbstractWrapper<T> {

	/**
	 * Creates a new wrapper
	 * @param target the target object
	 */
	public AbstractCustomizableWrapper(T target) {
		super(target);
	}

	/**
	 * Gets the value of the first active attribute of the given type
	 * @param attrType the attribute type
	 * @return the value
	 */
	protected Object getAttributeValue(CustomValueDescriptor attrType) {
		List<Attribute> attrs = target.getActiveAttributes(attrType);
		return attrs.size() > 0 ? attrs.get(0).getValue() : null;
	}
}