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

import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import net.stickycode.coercion.Coercions;

import static org.mockito.Mockito.times;

import static org.mockito.Mockito.verify;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationSystemComponentTest {

  @Mock
  Set<ConfigurationSource> source;

  @Mock
  ConfigurationAttribute attribute;

  @Spy
  Coercions coercions = new Coercions();

  @InjectMocks
  ConfigurationSystem configurationSystem = new ConfigurationSystem();

  @SuppressWarnings("unused")
  private static class OneField {

    private String noDefault;
    private String defaulted = "blah";
    private OneField noCoercion;
  }

  @Test
  public void lookupValue() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(s.hasValue("a")).thenReturn(true);
    when(s.getValue("a")).thenReturn("a");
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    assertThat(configurationSystem.lookupValue("a")).isEqualTo("a");
  }

  @Test
  public void lookupValueNotFoundIsNull() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    assertThat(configurationSystem.lookupValue("a")).isNull();
  }

  @Test(expected=NoConfiguredValueAndNoDefaultValueForAttribute.class)
  public void missingConfigurationExcepts() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    when((Class)attribute.getType()).thenReturn((Class) String.class);
    configurationSystem.processAttribute("a", attribute);
  }

  @Test
  public void processAttribute() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(s.hasValue("a")).thenReturn(true);
    when(s.getValue("a")).thenReturn("a");
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    when((Class)attribute.getType()).thenReturn((Class) String.class);
    configurationSystem.processAttribute("a", attribute);
    verify(attribute).setValue("a");
  }

  @Test
  public void leaveDefaultValue() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    when((Class)attribute.getType()).thenReturn((Class) String.class);
    when(attribute.hasDefaultValue()).thenReturn(true);
    configurationSystem.processAttribute("a", attribute);
    verify(attribute, times(0)).setValue("a");
  }
}
