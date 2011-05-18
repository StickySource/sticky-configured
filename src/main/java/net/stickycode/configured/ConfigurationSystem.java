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

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class ConfigurationSystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private Set<ConfigurationSource> sources;

  @Inject
  private CoercionFinder coercions;

  @Inject
  private ConfigurationRepository configurations;

  @Inject
  private ConfigurationKeyBuilder keyBuilder;

  @PostConstruct
  public void initialise() {
    log.info("Initialising configuration with configuration sources {} and coercions {}", keyBuilder, coercions);
  }

  public void configure() {
    log.info("configuring system {}", this);
    for (Configuration configuration : configurations)
      configure(configuration);
  }

  void configure(Configuration configuration) {
    log.debug("configuring {}", configuration);
    configuration.preConfigure();
    for (ConfigurationAttribute attribute : configuration) {
      log.debug("configuring attribute {}", attribute);
      String key = keyBuilder.buildKey(configuration, attribute);
      processAttribute(key, attribute);
    }
    configuration.postConfigure();
  }

  void processAttribute(String key, ConfigurationAttribute field) {
    // try to find the coercion first as this failure is cheaper
    // that a look up failure given there is a reasonable chance
    // that configuration is looked up externally
    Coercion<?> coercion = coercions.find(field);
    String value = lookupValue(key);

    // a null value means not configuration was found so do not set it
    if (value != null) {
      field.setValue(coercion.coerce(field, value));
    }
    else
      if (!field.hasDefaultValue()) {
        throw new MissingConfigurationException(key, sources);
      }
  }

  /**
   * @return the value to use or null one is not defined in any configuration source
   */
  String lookupValue(String key) {
    for (ConfigurationSource s : sources) {
      if (s.hasValue(key))
        return s.getValue(key);
    }

    log.debug("value not found for key '{}'", key);

    return null;
  }

}
