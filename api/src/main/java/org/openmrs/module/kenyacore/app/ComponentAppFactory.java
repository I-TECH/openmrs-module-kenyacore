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

package org.openmrs.module.kenyacore.app;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.factory.AppFrameworkFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * App framework implementation which loads app descriptors from the application context
 */
@Component
public class ComponentAppFactory implements AppFrameworkFactory {

	/**
	 * @see org.openmrs.module.appframework.factory.AppFrameworkFactory#getAppDescriptors()
	 */
	@Override
	public List<AppDescriptor> getAppDescriptors() throws IOException {
		return Context.getRegisteredComponents(AppDescriptor.class);
	}

	/**
	 * @see org.openmrs.module.appframework.factory.AppFrameworkFactory#getExtensions()
	 */
	@Override
	public List<Extension> getExtensions() throws IOException {
		return null;
	}

	/**
	 * @see org.openmrs.module.appframework.factory.AppFrameworkFactory#getAppTemplates()
	 */
	@Override
	public List<AppTemplate> getAppTemplates() throws IOException {
		return null;
	}
}