/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.configured;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.configuration.ConfigurationTargetResolver;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
public class ConfiguredConfigurationListener
    implements ConfigurationListener {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private ConfigurationTargetResolver resolver;

  @Inject
  private CoercionFinder coercions;

  @Inject
  private ConfigurationRepository configurations;

  @Inject
  private ComponentContainer container;

  @PostConstruct
  public void initialise() {
    log.info(" resolving values with {} and coercing with {}", resolver, coercions);
  }

  @Override
  public void resolve() {
    log.debug("starting resolution");
    for (Configuration configuration : configurations)
      for (ConfigurationAttribute attribute : configuration) {
        if (attribute.requiresResolution()) {
          log.debug("resolve {}", attribute);
          resolver.resolve(attribute);
          attribute.applyCoercion(coercions);
        }
        attribute.invertControl(container);
      }

    for (Configuration configuration : configurations)
      for (ConfigurationAttribute attribute : configuration)
        if (attribute.requiresResolution()) {
          log.debug("resolve second pass {}", attribute);
          resolver.resolve(attribute);
          attribute.applyCoercion(coercions);
          attribute.invertControl(container);
        }
  }

  @Override
  public void preConfigure() {
    for (Configuration configuration : configurations)
      configuration.preConfigure();
  }

  @Override
  public void configure() {
    log.debug("configurations {}", configurations);
    for (Configuration configuration : configurations)
      configure(configuration);
  }

  @Override
  public void postConfigure() {
    for (Configuration configuration : configurations)
      configuration.postConfigure();
  }

	@Override
	public void completeConfigure() {
		for (Configuration configuration : configurations)
			configuration.completeConfigure();
	}

	void configure(Configuration configuration) {
    for (ConfigurationAttribute attribute : configuration) {
      updateAttribute(attribute);
    }
  }

  void updateAttribute(ConfigurationAttribute field) {
    field.update();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
