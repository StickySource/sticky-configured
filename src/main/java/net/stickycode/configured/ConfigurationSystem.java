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
import net.stickycode.configured.content.ContentResolver;
import net.stickycode.configured.placeholder.ResolvedValue;
import net.stickycode.stereotype.StickyComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
public class ConfigurationSystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private ConfigurationManifest sources;
  
  @Inject
  private CoercionFinder coercions;

  @Inject
  private ConfigurationRepository configurations;
  
  @Inject
  private ConfigurationKeyBuilder keyBuilder;
  
  @Inject
  private ContentResolver contentResolver;

  @PostConstruct
  public void initialise() {
    log.info("Initialising configuration building keys with {} with configuration sources {} and coercions {}", new Object[] {keyBuilder, sources, coercions});
  }

  public void configure() {
    log.debug("resolving configuration sources");
    sources.resolve(configurations);
    
    log.debug("preconfiguring system {}", this);
    for (Configuration configuration : configurations)
      configuration.preConfigure();

    log.info("configuring {}", this);
    for (Configuration configuration : configurations)
      configure(configuration);
    
    log.info("configuring content {}", this);
    for (Configuration configuration : configurations)
      configure(configuration);
    
    log.debug("postconfiguring system {}", this);
    for (Configuration configuration : configurations)
      configuration.postConfigure();
    
    log.info("configured {}", this);
  }

  void configure(Configuration configuration) {
    log.debug("configuring {}", configuration);
    for (ConfigurationAttribute attribute : configuration) {
      log.debug("configuring attribute {}", attribute);
      String key = keyBuilder.build(configuration, attribute);
      processAttribute(key, attribute);
    }
  }

  void processAttribute(String key, ConfigurationAttribute field) {
    // try to find the coercion first as this failure is cheaper
    // that a look up failure given there is a reasonable chance
    // that configuration is looked up externally
    Coercion<?> coercion = coercions.find(field);
    ResolvedValue value = sources.find(key);

    // if we have not resolved a value then don't set it
    // the implication is that if the configuration is reloaded and 
    // we no longer have a value that it does not get unset
    // but the general assumption is that all configured fields _must_ have a value
    // so thats ok
    if (value.isResolved()) {
      field.setValue(coercion.coerce(field, value.getValue()));
    }
    else
      if (!field.hasDefaultValue()) {
        throw new MissingConfigurationException(key, sources);
      }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
