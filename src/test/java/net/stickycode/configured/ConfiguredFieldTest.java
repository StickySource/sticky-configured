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

import static org.fest.assertions.Assertions.assertThat;


public class ConfiguredFieldTest {
  @SuppressWarnings("unused")
  private static class OneField {
    private String noDefault;
    private String defaulted = "blah";
  }

  @Test
  public void noDefault() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("noDefault");
    ConfiguredField f = new ConfiguredField("oneField.noDefault", new OneField(), field);
    assertThat(f.getDefaultValue()).isNull();
    assertThat(f.getValue()).isNull();
  }

  @Test
  public void defaulted() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("defaulted");
    ConfiguredField f = new ConfiguredField("oneField.defaulted", new OneField(), field);
    assertThat(f.getDefaultValue()).isEqualTo("blah");
    assertThat(f.getValue()).isEqualTo("blah");
  }

  @Test
  public void accessible() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("noDefault");
    field.setAccessible(true);
    ConfiguredField f = new ConfiguredField("noDefault.defaultAccess", new OneField(), field);
    assertThat(f.getDefaultValue()).isNull();
    assertThat(f.getValue()).isNull();
    field.setAccessible(false);
  }

  @Test(expected=TriedToAccessFieldButWasDeniedException.class)
  public void nullTarget() throws SecurityException, NoSuchFieldException {
    Field field = OneField.class.getDeclaredField("noDefault");
    field.setAccessible(true);
    ConfiguredField f = new ConfiguredField("noDefault.defaultAccess", new String(), field);
    assertThat(f.getDefaultValue()).isNull();
    assertThat(f.getValue()).isNull();
    field.setAccessible(false);
  }

}
