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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import net.stickycode.coercion.Coercions;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configured.source.StickyApplicationConfigurationSource;
import net.stickycode.configured.source.SystemPropertiesConfigurationSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationSystemComponentTest {

  @Mock
  Set<ConfigurationSource> source;

  @Spy
  @InjectMocks
  ConfigurationManifest sources = new ConfigurationManifest();

  @Mock
  ConfigurationAttribute attribute;

  @Mock
  Configuration configuration;

  @Spy
  ConfigurationKeyBuilder builder = new SimpleNameDotFieldConfigurationKeyBuilder();

  @Spy
  Coercions coercions = new Coercions();

  @Mock
  ConfigurationRepository repository;

  @Mock
  StickyApplicationConfigurationSource applicationSource;

  @Mock
  SystemPropertiesConfigurationSource systemProperties;

  @InjectMocks
  ConfiguredConfigurationListener configurationSystem = new ConfiguredConfigurationListener();

  @Test
  public void lookupValue() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(s.hasValue("a")).thenReturn(true);
    when(s.getValue("a")).thenReturn("a");
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    assertThat(sources.lookupValue("a")).isEqualTo("a");
  }

  @Test
  public void lookupValueNotFoundIsNull() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    assertThat(sources.lookupValue("a")).isNull();
  }

  @Test(expected = MissingConfigurationException.class)
  public void missingConfigurationExcepts() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());

    mockConfiguration();
    
    configurationSystem.updateAttribute("bean.field", attribute);
  }

  @SuppressWarnings("rawtypes")
  private void mockConfiguration() {
    when((Class) attribute.getType()).thenReturn((Class) String.class);
    when(attribute.getCoercionTarget()).thenReturn(CoercionTargets.find(String.class));
    when(configuration.getName()).thenReturn("bean");
    when(attribute.getName()).thenReturn("field");
    when(repository.iterator()).thenReturn(Collections.singleton(configuration).iterator());
    when(configuration.iterator()).thenReturn(Collections.singleton(attribute).iterator());
    sources.resolve(repository);
  }

  @Test
  public void processAttribute() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(s.hasValue("bean.field")).thenReturn(true);
    when(s.getValue("bean.field")).thenReturn("a");
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());

    mockConfiguration();

    configurationSystem.updateAttribute("bean.field", attribute);
    verify(attribute).setValue("a");
  }

  @Test
  public void leaveDefaultValue() {
    ConfigurationSource s = mock(ConfigurationSource.class);
    when(source.iterator()).thenReturn(Collections.singleton(s).iterator());
    when(attribute.hasDefaultValue()).thenReturn(true);
    mockConfiguration();
    configurationSystem.updateAttribute("bean.field", attribute);
    verify(attribute, times(0)).setValue("a");
  }
}
