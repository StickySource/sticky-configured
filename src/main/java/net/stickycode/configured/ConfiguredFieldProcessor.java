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

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configured.finder.BeanFinder;
import net.stickycode.reflector.AnnotatedFieldProcessor;
import net.stickycode.reflector.Fields;
import net.stickycode.stereotype.Configured;
import net.stickycode.stereotype.ConfiguredComponent;
import net.stickycode.stereotype.ConfiguredStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfiguredFieldProcessor
    extends AnnotatedFieldProcessor {

  private Logger log = LoggerFactory.getLogger(getClass());

  private final ConfiguredConfiguration configuration;

  private final ConfiguredBeanProcessor repository;

  private final BeanFinder finder;

  private CoercionTarget parent;

  public ConfiguredFieldProcessor(ConfiguredBeanProcessor configuredBeanProcessor, ConfiguredConfiguration configuration,
      CoercionTarget parent, BeanFinder finder) {
    super(Configured.class, ConfiguredStrategy.class);
    this.configuration = configuration;
    this.repository = configuredBeanProcessor;
    this.parent = parent;
    this.finder = finder;
  }

  @Override
  public void processField(Object target, Field field) {
    if (field.getType().isPrimitive())
      throw new ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossibleException(target, field);

    if (field.getType().isAnnotationPresent(ConfiguredComponent.class))
      processComponent(target, field);
    else
      configuration.addAttribute(new ConfiguredField(target, field, fieldTarget(field)));
  }

  private CoercionTarget fieldTarget(Field field) {
    if (parent == null)
      return CoercionTargets.find(field);

    return CoercionTargets.find(field, parent);
  }

  private void processComponent(Object target, Field field) {
    Class<?> type = field.getType();
    Object component = newInstance(type);
    CoercionTarget componentTarget = fieldTarget(field);

    repository.process(component, Introspector.decapitalize(target.getClass().getSimpleName() + "." + field.getName()),
        componentTarget);
    configuration.addAttribute(new ConfigurationComponent(component, componentTarget, field.getName()));
    Fields.set(target, field, component);
  }

  private Object newInstance(Class<?> type) {
    return finder.find(type);
  }
}
