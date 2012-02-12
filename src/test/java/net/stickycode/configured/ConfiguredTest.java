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
import java.util.List;

import org.junit.Test;

import net.stickycode.stereotype.Configured;

import static org.fest.assertions.Assertions.assertThat;

public class ConfiguredTest {

  @Configured
  String configured;

  @Configured
  Integer number;

  @Configured
  List<Double> doubles;

  String notConfigured;

  @Test
  public void notConfigured() {
    assertThat(canProcess("notConfigured")).isFalse();
  }

  @Test
  public void configured() {
    assertThat(canProcess("configured")).isTrue();
  }

  @Test
  public void configuredNumber() {
    assertThat(canProcess("number")).isTrue();
  }

  @Test
  public void configuredDoubles() {
    assertThat(canProcess("doubles")).isTrue();
  }

  private boolean canProcess(String fieldName) {
    ConfiguredFieldProcessor configuredFieldProcessor = new ConfiguredFieldProcessor(null, null, null, null);
    return configuredFieldProcessor.canProcess(getField(fieldName));
  }

  private Field getField(String fieldName) {
    try {
      return getClass().getDeclaredField(fieldName);
    }
    catch (SecurityException e) {
      throw new RuntimeException(e);
    }
    catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

}
