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

import org.junit.Test;

import net.stickycode.coercion.CoercionNotFoundException;

public class ConfigurationSystemTest {

  private final class IdentityConfigurationSource
      implements ConfigurationSource {

    @Override
    public boolean hasValue(String key) {
      return true;
    }

    @Override
    public String getValue(String key) {
      return key;
    }
  }

  private final class NoConfigurationSource
      implements ConfigurationSource {

    @Override
    public boolean hasValue(String key) {
      return false;
    }

    @Override
    public String getValue(String key) {
      return null;
    }
  }

  @SuppressWarnings("unused")
  private static class OneField {

    private String noDefault;
    private String defaulted = "blah";
    private OneField noCoercion;
  }

  @Test
  public void noDefault() throws SecurityException, NoSuchFieldException {
    ConfigurationSystem s = new ConfigurationSystem(
        new IdentityConfigurationSource());
    Field field = OneField.class.getDeclaredField("noDefault");
    OneField target = new OneField();
    s.registerField(target, field);
    s.configure();
  }

  @Test(expected = ConfigurationValueNotFoundForKeyException.class)
  public void noValueWithNoDefaultErrors() throws SecurityException, NoSuchFieldException {
    configureField("noDefault", new NoConfigurationSource());
  }

  @Test(expected = CoercionNotFoundException.class)
  public void noCoercion() throws SecurityException, NoSuchFieldException {
    configureField("noCoercion", new IdentityConfigurationSource());
  }

  private void configureField(String fieldName, ConfigurationSource configurationSource) throws NoSuchFieldException {
    ConfigurationSystem s = new ConfigurationSystem(configurationSource);
    Field field = OneField.class.getDeclaredField(fieldName);
    OneField target = new OneField();
    s.registerField(target, field);
    s.configure();
  }

}
