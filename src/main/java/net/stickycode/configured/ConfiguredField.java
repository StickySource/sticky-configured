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

import net.stickycode.coercion.CoercionTarget;

public class ConfiguredField
    implements CoercionTarget {

  private final Object defaultValue;
  private final String key;
  private final Object target;
  private final Field field;

  public ConfiguredField(String key, Object target, Field field) {
    this.defaultValue = getValue(target, field);
    this.target = target;
    this.field = field;
    this.key = key;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public Object getValue() {
    return getValue(target, field);
  }

  public String getKey() {
    return this.key;
  }

  public void configure(Object value) {
    setValue(value);
  }

  @Override
  public Class<?> getType() {
    return field.getType();
  }

  public boolean hasDefaultValue() {
    return defaultValue != null;
  }

  private Object getValue(Object target, Field field) {
    boolean accessible = field.isAccessible();
    try {
      field.setAccessible(true);
      return safeGetField(target, field);
    }
    finally {
      field.setAccessible(accessible);
    }
  }

  private Object safeGetField(Object target, Field field) {
    try {
      return field.get(target);
    }
    catch (IllegalArgumentException e) {
      throw new TriedToAccessFieldButWasDeniedException(e, field, target);
    }
    catch (IllegalAccessException e) {
      throw new TriedToAccessFieldButWasDeniedException(e, field, target);
    }
  }

  private void setValue(Object value) {
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    try {
      safeSetValue(value);
    }
    finally {
      field.setAccessible(accessible);
    }
  }

  private void safeSetValue(Object value) {
    try {
      field.set(target, value);
    }
    catch (IllegalArgumentException e) {
      throw new TriedToAccessFieldButWasDeniedException(e, field, target);
    }
    catch (IllegalAccessException e) {
      throw new TriedToAccessFieldButWasDeniedException(e, field, target);
    }
  }

}
