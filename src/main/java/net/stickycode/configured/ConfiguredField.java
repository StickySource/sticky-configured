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

import java.beans.Introspector;
import java.lang.reflect.Field;

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ResolvedConfiguration;
import net.stickycode.exception.NullParameterException;
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

  public ConfiguredField(Object target, Field field, CoercionTarget coercionTarget) {
    this.target = notNull(target, "The target bean for a configured field cannot be null");
    this.field = notNull(field, "A configured field cannot be null");
    this.coercionTarget = notNull(coercionTarget, "A configured field must have a coercion target");
    this.defaultValue = getValue();
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public Object getValue() {
    return Fields.get(target, field);
  }

  @Override
  public Class<?> getType() {
    return field.getType();
  }

  public boolean hasDefaultValue() {
    return defaultValue != null;
  }

  public void setValue(Object value) {
    if (value == null)
      throw new NullParameterException("Cannot set {} to null", field);

    this.hasBeenSet = true;
    Fields.set(target, field, value);
  }

  @Override
  public String getName() {
    return field.getName();
  }

  @Override
  public String toString() {
    return join(".");
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return coercionTarget;
  }

  @Override
  public boolean canBeUpdated() {
    return true;
  }

  @Override
  public boolean hasValue() {
    return hasBeenSet || hasDefaultValue();
  }

  @Override
  public String join(String delimeter) {
    return Introspector.decapitalize(target.getClass().getSimpleName()) + delimeter + field.getName();
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
    if (resolution.hasValue()) {
      this.value = this.coercion.coerce(coercionTarget, resolution.getValue());
    }
  }

  @Override
  public void update() {
    if (value != null) {
      Fields.set(target, field, value);
    }
    else
      if (coercion.hasDefaultValue())
        Fields.set(target, field, coercion.getDefaultValue(coercionTarget));
      else
        if (defaultValue == null) {
          throw new MissingConfigurationException(this, resolution);
        }
  }

  @Override
  public void invertControl(ComponentContainer container) {
    if (value != null)
      container.inject(value);
  }

}
