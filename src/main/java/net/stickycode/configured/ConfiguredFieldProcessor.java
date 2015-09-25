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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configuration.ConfigurationTarget;
import net.stickycode.configuration.ConfigurationValue;
import net.stickycode.reflector.AnnotatedFieldProcessor;

public class ConfiguredFieldProcessor
    extends AnnotatedFieldProcessor {

  private final class DefaultConfigurationValue
      implements ConfigurationValue {
    private String seed;

    public DefaultConfigurationValue(String seed) {
      this.seed = seed;
    }

    @Override
    public boolean hasPrecedence(ConfigurationValue v) {
      return false;
    }

    @Override
    public String get() {
      return seed;
    }

    @Override
    public String toString() {
      return String.format("DefaultConfiguration[%s]", seed);
    }
  }

  private final ConfigurationRepository configuration;

  private ConfigurationTarget parent;

  private Map<Class<? extends Annotation>, Method> defaultSeeds;


  public ConfiguredFieldProcessor(ConfigurationRepository configuration, ConfiguredMetadata annotations,
      ConfigurationTarget parent2) {
    super(annotations.getConfiguredAnnotations());
    this.configuration = configuration;
    this.defaultSeeds = annotations.getDefaultSeeds();
    this.parent = parent2;
  }

  @Override
  public void processField(Object target, Field field) {
    if (field.getType().isPrimitive())
      throw new ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossibleException(target, field);

    configuration.register(new ConfiguredField(parent, target, field, fieldTarget(field), deriveDefaultConfiguration(field)));
  }

  /**
   * If someone really really wants to define keys as part of the annotation which I think is horrible, this is the list of keys
   */
  private List<ConfigurationValue> deriveDefaultConfiguration(Field field) {
    if (defaultSeeds.isEmpty())
      return Collections.emptyList();

    List<ConfigurationValue> seeds = new ArrayList<>();
    for (Class<? extends Annotation> type : defaultSeeds.keySet()) {
      String seed = getDefaultKey(field, type);
      if (seed != null)
        seeds.add(new DefaultConfigurationValue(seed));

    }
    return seeds;
  }

  private String getDefaultKey(Field field, Class<? extends Annotation> type) {
    Annotation a = field.getAnnotation(type);
    if (a != null) {
      try {
        return (String) defaultSeeds.get(type).invoke(a);
      }
      catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        return null;
      }
    }
    return null;
  }

  private CoercionTarget fieldTarget(Field field) {
    if (parent.getCoercionTarget() == null)
      return CoercionTargets.find(field);

    return CoercionTargets.find(field, parent.getCoercionTarget());
  }

}
