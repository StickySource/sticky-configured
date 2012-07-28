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

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.configuration.ConfigurationTargetResolver;
import net.stickycode.stereotype.StickyPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  @Inject
  private ConfiguredBeanProcessor beanProcessor;

  @PostConstruct
  public void initialise() {
    log.info(" resolving values with {} and coercing with {}", resolver, coercions);
  }

  public void resolve() {
    for (Configuration configuration : configurations)
      for (ConfigurationAttribute attribute : configuration) {
        resolver.resolve(attribute);
        attribute.applyCoercion(coercions);
        attribute.invertControl(container);

        attribute.recurse(beanProcessor);
      }

    for (Configuration configuration : configurations)
      for (ConfigurationAttribute attribute : configuration)
        if (attribute.requiresResolution()) {
          resolver.resolve(attribute);
          attribute.applyCoercion(coercions);
          attribute.invertControl(container);
        }
  }

  public void preConfigure() {
    for (Configuration configuration : configurations)
      configuration.preConfigure();
  }

  public void configure() {
    for (Configuration configuration : configurations)
      configure(configuration);
  }

  public void postConfigure() {
    for (Configuration configuration : configurations)
      configuration.postConfigure();
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
