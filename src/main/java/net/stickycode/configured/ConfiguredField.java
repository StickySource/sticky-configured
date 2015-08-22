/**
 * Copyright (c) 2012 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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

import static net.stickycode.exception.Preconditions.notNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ConfigurationKey;
import net.stickycode.configuration.ResolvedConfiguration;
import net.stickycode.reflector.Fields;

public class ConfiguredField
    implements ConfigurationAttribute {

  private final Object defaultValue;

  private final Object target;

  private final Field field;

  private final CoercionTarget coercionTarget;

  private boolean hasBeenSet = false;

  private ResolvedConfiguration resolution;

  private Object value;

  private Coercion<Object> coercion;

  private ConfigurationKey name;

  public ConfiguredField(ConfigurationKey name, Object target, Field field, CoercionTarget coercionTarget) {
    this.target = notNull(target, "The target bean for a configured field cannot be null");
    this.field = notNull(field, "A configured field cannot be null");
    this.coercionTarget = notNull(coercionTarget, "A configured field must have a coercion target");
    this.name = name;
    this.defaultValue = getValue();
  }

  public Object getValue() {
    return Fields.get(target, field);
  }

  @Override
  public String toString() {
    return join(".").get(0);
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return coercionTarget;
  }

  @Override
  public List<String> join(String delimeter) {
    return name.join(delimeter).stream()
        .map(s -> s + delimeter + field.getName())
        .collect(Collectors.toList());
  }

  @Override
  public void resolvedWith(ResolvedConfiguration resolved) {
    this.resolution = resolved;
  }

  @Override
  public ResolvedConfiguration getResolution() {
    return resolution;
  }

  @Override
  public void applyCoercion(CoercionFinder coercions) {
    this.coercion = coercions.find(coercionTarget);
    this.value = resolveValue();
  }

  private Object resolveValue() {
    if (resolution.hasValue())
      return this.coercion.coerce(coercionTarget, resolution.getValue());

    if (coercion.hasDefaultValue())
      return coercion.getDefaultValue(coercionTarget);

    // no useful values so its still null
    return null;
  }

  @Override
  public void update() {
    if (value != null)
      Fields.set(target, field, value);

    else
      if (defaultValue == null)
        throw new MissingConfigurationException(this, resolution);

  }

  @Override
  public void invertControl(ComponentContainer container) {
    if (value != null)
      container.inject(value);
  }

  @Override
  public Object getTarget() {
    return target;
  }

  @Override
  public boolean requiresResolution() {
    return resolution == null;
  }

}
