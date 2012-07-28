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
import java.util.regex.Pattern;

import org.junit.Test;

import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.reflector.TriedToAccessFieldButWasDeniedException;

import static org.fest.assertions.Assertions.assertThat;

public class ConfiguredFieldTest {

  @SuppressWarnings("unused")
  private static class OneField {

    private String noDefault;

    private String defaulted = "blah";

    private Coercion<Pattern> generic;

    private Float[] floats;

  }

  @Test
  public void noDefault() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("noDefault");
    assertThat(f.getDefaultValue()).isNull();
    assertThat(f.getValue()).isNull();
  }

  @Test
  public void defaulted() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("defaulted");
    assertThat(f.hasDefaultValue()).isTrue();
    assertThat(f.getDefaultValue()).isEqualTo("blah");
    assertThat(f.getValue()).isEqualTo("blah");
    assertThat(f.join(".")).isEqualTo("oneField.defaulted");
  }

  @Test
  public void accessible() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("noDefault");
    field.setAccessible(false);
    try {
      ConfiguredField f = configuredField(new OneField(), field);
      assertThat(f.getDefaultValue()).isNull();
      assertThat(f.getValue()).isNull();
    }
    finally {
      field.setAccessible(true);
    }
  }

  private ConfiguredField configuredField(Object target, Field field) {
    return new ConfiguredField(new SimpleNameConfigurationTarget(target), target, field, CoercionTargets.find(field));
  }

  @Test(expected = TriedToAccessFieldButWasDeniedException.class)
  public void nullTarget() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("noDefault");
    field.setAccessible(true);
    try {
      ConfiguredField f = configuredField(new String(), field);
      assertThat(f.getDefaultValue()).isNull();
      assertThat(f.getValue()).isNull();
    }
    finally {
      field.setAccessible(false);
    }
  }

  @Test
  public void setit() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("defaulted");
    assertThat(f.getDefaultValue()).isEqualTo("blah");
    assertThat(f.getValue()).isEqualTo("blah");
    f.setValue("notblah");
    assertThat(f.getValue()).isEqualTo("notblah");
  }

  @Test
  public void generic() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("generic");
    assertThat(f.hasDefaultValue()).isFalse();
    assertThat(f.getDefaultValue()).isEqualTo(null);
//    assertThat(f.getCoercionTarget().hasComponents()).isTrue();
//    assertThat(f.getCoercionTarget().isArray()).isFalse();
//    assertThat(f.getCoercionTarget().isPrimitive()).isFalse();
  }
  
  @Test
  public void floats() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("floats");
    assertThat(f.hasDefaultValue()).isFalse();
    assertThat(f.getDefaultValue()).isEqualTo(null);
//    assertThat(f.getCoercionTarget().hasComponents()).isTrue();
//    assertThat(f.getCoercionTarget().getComponentCoercionTypes()).containsOnly(CoercionTargets.find(Float.class));
  }

  private ConfiguredField configuredField(String name) throws NoSuchFieldException {
    Field field = OneField.class.getDeclaredField(name);
    ConfiguredField f = configuredField(new OneField(), field);
    return f;
  }
}
