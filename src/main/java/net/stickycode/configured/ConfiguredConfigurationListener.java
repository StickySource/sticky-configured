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

import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ConfigurationResolutions;
import net.stickycode.configuration.ConfigurationResolver;
import net.stickycode.configuration.ConfigurationValues;
import net.stickycode.configuration.Value;
import net.stickycode.stereotype.StickyPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyPlugin
public class ConfiguredConfigurationListener
    implements ConfigurationListener {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private ConfigurationResolver resolver;

  @Inject
  private CoercionFinder coercions;

  @Inject
  private ConfigurationRepository configurations;

  @Inject
  private ConfigurationKeyBuilder keyBuilder;

  @PostConstruct
  public void initialise() {
    log.info("building keys using {}, resolving values from sources {} and coercing with {}", new Object[] {
        keyBuilder, resolver, coercions });
  }

  public void resolve() {
  }

  public void preConfigure() {
    for (Configuration configuration : configurations)
      configuration.preConfigure();
  }

  public void configure() {
    ConfigurationResolutions resolutions = resolver.resolve(new ConfiguredAttributeIterable(configurations));
    for (Configuration configuration : configurations)
      configure(configuration, resolutions);
  }

  public void postConfigure() {
    for (Configuration configuration : configurations)
      configuration.postConfigure();
  }

  void configure(Configuration configuration, ConfigurationResolutions resolutions) {
    for (ConfigurationAttribute attribute : configuration) {
      if (attribute.canBeUpdated())
        updateAttribute(attribute,resolutions);
      else {
        if (!attribute.hasValue())
          throw new AttributeCannotBeUpdatedAndHasNoValueException(configuration, attribute);
      }
    }
  }

  void updateAttribute(ConfigurationAttribute field, ConfigurationResolutions resolutions) {
    // try to find the coercion first as this failure is cheaper
    // that a look up failure given there is a reasonable chance
    // that configuration is looked up externally
    CoercionTarget coercionTarget = field.getCoercionTarget();
    Coercion<?> coercion = coercions.find(coercionTarget);
    ConfigurationValues value = resolutions.find(field);

    // if we have not resolved a value then don't set it
    // the implication is that if the configuration is reloaded and
    // we no longer have a value that it does not get unset
    // but the general assumption is that all configured fields _must_ have a value
    // so thats ok
    if (value.hasValue()) {
      field.setValue(coercion.coerce(coercionTarget, value.getValue()));
    }
    else
      if (coercion.hasDefaultValue())
        field.setValue(coercion.getDefaultValue(coercionTarget));
      else
        if (!field.hasDefaultValue()) {
          throw new MissingConfigurationException(field, resolver);
        }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
