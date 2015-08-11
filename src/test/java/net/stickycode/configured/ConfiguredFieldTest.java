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
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.coercion.Coercions;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configuration.ConfigurationValue;
import net.stickycode.configuration.ResolvedConfiguration;
import net.stickycode.reflector.TriedToAccessFieldButWasDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfiguredFieldTest {


  public class CoercionWithDefault
      implements Coercion<Object> {

    @Override
    public Object coerce(CoercionTarget type, String value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isApplicableTo(CoercionTarget target) {
      return true;
    }

    @Override
    public boolean hasDefaultValue() {
      return true;
    }

    @Override
    public Object getDefaultValue(CoercionTarget target) {
      return "coerciondefault";
    }

  }

  private final class BlahResolution
      implements ResolvedConfiguration {

    @Override
    public boolean hasValue() {
      return true;
    }

    @Override
    public String getValue() {
      return "blah";
    }

    @Override
    public void add(ConfigurationValue value) {
    }
  }

  private final class NoResolution
      implements ResolvedConfiguration {

    @Override
    public boolean hasValue() {
      return false;
    }

    @Override
    public String getValue() {
      return null;
    }

    @Override
    public void add(ConfigurationValue value) {
    }
  }

  @SuppressWarnings("unused")
  private static class OneField {

    private String noDefault;

    private String defaulted = "alreadyset";

    private Coercion<Pattern> generic;

    private Float[] floats;

  }

  @Test(expected=MissingConfigurationException.class)
  public void noDefaultNoConfig() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("noDefault");
    f.resolvedWith(new NoResolution());
    f.applyCoercion(new Coercions());
    f.update();
  }

  @Test
  public void noDefaultWithConfig() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("noDefault");
    f.resolvedWith(new BlahResolution());
    f.applyCoercion(new Coercions());
    f.update();
    assertThat(f.getValue()).isEqualTo("blah");
  }

  @Test
  public void noDefaultButCoercionHasOne() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("noDefault");
    f.resolvedWith(new NoResolution());
    CoercionFinder finder = mock(CoercionFinder.class);
    when(finder.find(any(CoercionTarget.class))).thenReturn(new CoercionWithDefault());
    f.applyCoercion(finder);
    f.update();
    assertThat(f.getValue()).isEqualTo("coerciondefault");
  }

  @Test
  public void defaulted() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("defaulted");
    f.resolvedWith(new NoResolution());
    f.applyCoercion(new Coercions());
    f.update();
    assertThat(f.getValue()).isEqualTo("alreadyset");
    assertThat(f.join(".")).containsOnly("oneField.defaulted");
  }

  @Test
  public void defaultIsOverridden() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("defaulted");
    f.resolvedWith(new BlahResolution());
    f.applyCoercion(new Coercions());
    f.update();
    assertThat(f.getValue()).isEqualTo("blah");
    assertThat(f.join(".")).containsOnly("oneField.defaulted");
  }

  @Test
  public void accessible() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("noDefault");
    field.setAccessible(false);
    try {
      ConfiguredField f = configuredField(new OneField(), field);
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
      assertThat(f.getValue()).isNull();
    }
    finally {
      field.setAccessible(false);
    }
  }

  @Test
  public void generic() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("generic");
    assertThat(f.getCoercionTarget().hasComponents()).isTrue();
    assertThat(f.getCoercionTarget().isArray()).isFalse();
    assertThat(f.getCoercionTarget().isPrimitive()).isFalse();
  }

  @Test
  public void floats() throws SecurityException, NoSuchFieldException {
    ConfiguredField f = configuredField("floats");
    assertThat(f.getCoercionTarget().hasComponents()).isTrue();
    assertThat(f.getCoercionTarget().getComponentCoercionTypes()).containsOnly(CoercionTargets.find(Float.class));
  }

  private ConfiguredField configuredField(String name) throws NoSuchFieldException {
    Field field = OneField.class.getDeclaredField(name);
    ConfiguredField f = configuredField(new OneField(), field);
    return f;
  }
}
