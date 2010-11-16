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


public class ConfigurationSystemTest {
  @SuppressWarnings("unused")
  private static class OneField {
    private String noDefault;
    private String defaulted = "blah";
    private OneField noCoercion;
  }

  @Test
  public void noDefault() throws SecurityException, NoSuchFieldException {
    ConfigurationSystem s = new ConfigurationSystem();
    s.add(new ConfigurationSource() {

      @Override
      public boolean hasValue(String key) {
        return true;
      }

      @Override
      public String getValue(String key) {
        return key;
      }
    });
    Field field = OneField.class.getDeclaredField("noDefault");
    OneField target = new OneField();
    s.registerField(target, field);
    s.configure();
  }

  @Test(expected=ConfigurationValueNotFoundForKeyException.class)
  public void noValue() throws SecurityException, NoSuchFieldException {
    ConfigurationSystem s = new ConfigurationSystem();
    Field field = OneField.class.getDeclaredField("noDefault");
    OneField target = new OneField();
    s.registerField(target, field);
    s.configure();
  }

  @Test(expected=CoercionNotFoundForTypeException.class)
  public void noCoercion() throws SecurityException, NoSuchFieldException {
    ConfigurationSystem s = new ConfigurationSystem();
    Field field = OneField.class.getDeclaredField("noCoercion");
    OneField target = new OneField();
    s.registerField(target, field);
    s.configure();
  }

}
