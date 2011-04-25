/**
 * Copyright (c) 2010 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.Coercions;
import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class ConfigurationSystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private Set<ConfigurationSource> sources;

  @Inject
  private CoercionFinder coercions;

  private List<ConfiguredField> fields = new LinkedList<ConfiguredField>();
  private final KeyGenerator keyGenerator;

  public ConfigurationSystem() {
    this.keyGenerator = new SimpleNameDotFieldKeyGenerator();
  }

  /**
   * For non DI instantiation
   */
  public ConfigurationSystem(KeyGenerator keyGenerator, ConfigurationSource source) {
    super();
    this.keyGenerator = keyGenerator;
    this.coercions = new Coercions();
    this.sources = Collections.singleton(source);
    log.debug("using key generator {}", keyGenerator);
  }

  public ConfigurationSystem(ConfigurationSource source) {
    super();
    this.keyGenerator = new SimpleNameDotFieldKeyGenerator();
    this.coercions = new Coercions();
    this.sources = Collections.singleton(source);
    log.debug("using key generator {}", keyGenerator);
  }

  @PostConstruct
  public void initialise() {
    log.info("Initialising configuration with configuration sources {} and coercions {}", keyGenerator, coercions);
  }

  public void registerField(Object target, Field field) {
    String key = keyGenerator.getKey(target, field);
    ConfiguredField configuredField = new ConfiguredField(key, target, field);
    fields.add(configuredField);

    configureField(configuredField);
  }

  public void configure() {
    for (ConfiguredField field : fields)
      configureField(field);
  }

  void configureField(ConfiguredField field) {
    Coercion<?> coercion = coercions.find(field);
    String value = lookupValue(field);
    if (value != null)
      field.configure(coercion.coerce(field, value));
  }

  /**
   * @return the value to use or null if there is a default value and one is not defined in any configuration
   */
  private String lookupValue(ConfiguredField field) {
    String key = field.getKey();
    for (ConfigurationSource s : sources) {
      if (s.hasValue(key))
        return s.getValue(key);
    }

    if (!field.hasDefaultValue())
      throw new ConfigurationValueNotFoundForKeyException(field.getKey(), sources);

    return null;
  }

  public int registeredFieldCount() {
    return fields.size();
  }

}
