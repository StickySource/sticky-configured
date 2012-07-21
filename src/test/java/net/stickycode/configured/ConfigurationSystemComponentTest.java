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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.stickycode.coercion.Coercions;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configuration.ConfigurationKey;
import net.stickycode.configuration.ConfigurationResolutions;
import net.stickycode.configuration.ConfigurationResolver;
import net.stickycode.configuration.ConfigurationValues;

import org.junit.Before;
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
  ConfigurationResolver resolver;
  
  @Spy
  Coercions coercions = new Coercions();
  
  @Mock
  ConfigurationResolutions resolutions;
  
  @InjectMocks
  ConfiguredConfigurationListener configurationSystem = new ConfiguredConfigurationListener();

  @Before
  public void before() {
    when(attribute.getCoercionTarget()).thenReturn(CoercionTargets.find(String.class));
    when(attribute.join(".")).thenReturn("bean.field");
  }
  
  @Test(expected = MissingConfigurationException.class)
  public void missingConfigurationExcepts() {
    ConfigurationValues mock = mock(ConfigurationValues.class);
    when(resolutions.find(any(ConfigurationKey.class))).thenReturn(mock);
    configurationSystem.updateAttribute(attribute, resolutions);
  }

  @Test
  public void processAttribute() {

    ConfigurationValues mock = mock(ConfigurationValues.class);
    when(mock.getValue()).thenReturn("a");
    when(mock.hasValue()).thenReturn(true);
    when(resolutions.find(any(ConfigurationKey.class))).thenReturn(mock);
    
    configurationSystem.updateAttribute(attribute, resolutions);
    
    verify(attribute).setValue("a");
  }

  @Test
  public void leaveDefaultValue() {
    ConfigurationValues mock = mock(ConfigurationValues.class);
    when(resolutions.find(any(ConfigurationKey.class))).thenReturn(mock);
    when(attribute.hasDefaultValue()).thenReturn(true);
    configurationSystem.updateAttribute(attribute, resolutions);
    verify(attribute, times(0)).setValue("a");
  }
}
