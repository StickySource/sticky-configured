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

  private Object getValue(Object target, Field field) {
    try {
      if (field.isAccessible())
        return field.get(target);

      return getInaccessibleField(target, field);
    }
    catch (IllegalArgumentException e) {
      throw new TriedToAccessFieldButWasDeniedException(e, field, target);
    }
    catch (IllegalAccessException e) {
      throw new TriedToAccessFieldButWasDeniedException(e, field, target);
    }
  }

  private Object getInaccessibleField(Object target, Field field)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      field.setAccessible(true);
      return field.get(target);
    }
    finally {
      field.setAccessible(false);
    }
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

  public void configure(Object coerce) {

  }

  @Override
  public Class<?> getType() {
    return field.getType();
  }

}
