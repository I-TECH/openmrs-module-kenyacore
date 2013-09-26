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

package org.openmrs.module.kenyacore.metadata.api.impl;

import org.openmrs.GlobalProperty;
import org.openmrs.OpenmrsObject;
import org.openmrs.annotation.Handler;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.kenyacore.metadata.api.MetadataDeployService;
import org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler;
import org.openmrs.module.kenyacore.metadata.handler.ObjectMergeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the metadata deploy service
 */
public class MetadataDeployServiceImpl extends BaseOpenmrsService implements MetadataDeployService {

	private Map<Class<?>, ObjectDeployHandler> handlers;

	@Autowired
	@Qualifier("adminService")
	private AdministrationService adminService;

	/**
	 * Sets the object handlers, reorganising them into a map
	 * @param handlers the handler components
	 */
	@Autowired
	public void setHandlers(Set<ObjectDeployHandler> handlers) {
		this.handlers = new HashMap<Class<?>, ObjectDeployHandler>();

		for (ObjectDeployHandler handler : handlers) {
			Handler handlerAnnotation = handler.getClass().getAnnotation(Handler.class);
			if (handlerAnnotation != null) {
				for (Class<?> supportedClass : handlerAnnotation.supports()) {
					this.handlers.put(supportedClass, handler);
				}
			}
		}
	}

	/**
	 * @see MetadataDeployService#installObject(org.openmrs.OpenmrsObject)
	 */
	@Override
	public boolean installObject(OpenmrsObject incoming) {
		ObjectDeployHandler handler = getHandler(incoming.getClass());

		// Look for existing by UUID (i.e. exact match)
		OpenmrsObject existing = handler.fetch(incoming.getUuid());

		// If no exact match, look for another existing item that should be replaced
		if (existing == null) {
			existing = handler.findAlternateMatch(incoming);
		}

		if (existing != null) {
			// Optionally perform merge operation
			if (handler instanceof ObjectMergeHandler) {
				((ObjectMergeHandler) handler).merge(existing, incoming);
			}

			// Global properties are special case as the property name is the id and the saveGlobalProperty method
			// won't update the UUID. So easiest solution is to delete the existing property
			if (existing instanceof GlobalProperty) {
				adminService.purgeGlobalProperty((GlobalProperty) existing);
			}
			else {
				// Steal existing object's id and evict it so that it can completely overwritten
				incoming.setId(existing.getId());
				Context.evictFromSession(existing);
			}
		}

		handler.save(incoming);

		return existing != null;
	}

	/**
	 * @see MetadataDeployService#uninstallObject(org.openmrs.OpenmrsObject, String)
	 */
	@Override
	public void uninstallObject(OpenmrsObject outgoing, String reason) {
		ObjectDeployHandler handler = getHandler(outgoing.getClass());

		handler.remove(outgoing, reason);
	}

	/**
	 * @see MetadataDeployService#fetchObject(Class, String)
	 */
	@Override
	public <T extends OpenmrsObject> T fetchObject(Class<T> clazz, String uuid) {
		ObjectDeployHandler handler = getHandler(clazz);
		return (T) handler.fetch(uuid);
	}

	/**
	 * Convenience method to get the handler for the given object class
	 * @param clazz the object class
	 * @return the handler
	 * @throws RuntimeException if no suitable handler exists
	 */
	protected ObjectDeployHandler getHandler(Class<? extends OpenmrsObject> clazz) throws RuntimeException {
		ObjectDeployHandler handler = handlers.get(clazz);
		if (handler != null) {
			return handler;
		}

		throw new RuntimeException("No handler class found for " + clazz.getName());
	}
}