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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import net.stickycode.coercion.Coercions;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configuration.ConfigurationTargetResolver;
import net.stickycode.configuration.ResolvedConfiguration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationSystemComponentTest {

  @Mock
  ConfigurationAttribute attribute;

  @Mock
  Configuration configuration;

  @Mock
  ConfigurationTargetResolver resolver;

  @Spy
  Coercions coercions = new Coercions();

  @InjectMocks
  ConfiguredConfigurationListener configurationSystem = new ConfiguredConfigurationListener();

  @Before
  public void before() {
//    when(attribute.getCoercionTarget()).thenReturn(CoercionTargets.find(String.class));
    when(attribute.join(".")).thenReturn(Collections.singletonList("bean.field"));
  }

  @Test(expected = MissingConfigurationException.class)
  @Ignore
  public void missingConfigurationExcepts() {
    ResolvedConfiguration mock = mock(ResolvedConfiguration.class);
    when(attribute.getResolution()).thenReturn(mock);
    configurationSystem.updateAttribute(attribute);
  }

  @Test
  @Ignore
  public void processAttribute() {

    ResolvedConfiguration mock = mock(ResolvedConfiguration.class);
    when(mock.getValue()).thenReturn("a");
    when(mock.hasValue()).thenReturn(true);
    when(attribute.getResolution()).thenReturn(mock);

    configurationSystem.updateAttribute(attribute);

    verify(attribute).update();
  }

  @Test
  @Ignore
  public void leaveDefaultValue() {
    ResolvedConfiguration mock = mock(ResolvedConfiguration.class);
    when(attribute.getResolution()).thenReturn(mock);
//    when(attribute.hasDefaultValue()).thenReturn(true);
    configurationSystem.updateAttribute(attribute);
//    verify(attribute, times(0)).setValue("a");
  }
}
