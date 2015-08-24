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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import net.stickycode.coercion.Coercions;
import net.stickycode.configuration.ConfigurationTargetResolver;

public class ConfigurationSystemComponentTest {

  @Injectable
  ConfigurationAttribute attribute;

  @Injectable
  Configuration configuration;

  @Injectable
  ConfigurationTargetResolver resolver;

  @Injectable
  Coercions coercions = new Coercions();

  @Tested
  ConfiguredConfigurationListener configurationSystem;

  @Before
  public void before() {
    new Expectations() {
      {
        attribute.join(".");
        result = Collections.singletonList("bean.field");
      }
    };
  }

  @Test(expected = MissingConfigurationException.class)
  @Ignore
  public void missingConfigurationExcepts() {
//    ResolvedConfiguration mock = mock(ResolvedConfiguration.class);
//    when(attribute.getResolution()).thenReturn(mock);
    configurationSystem.updateAttribute(attribute);
  }

  @Test
  @Ignore
  public void processAttribute() {

//    ResolvedConfiguration mock = mock(ResolvedConfiguration.class);
//    when(mock.getValue()).thenReturn("a");
//    when(mock.hasValue()).thenReturn(true);
//    when(attribute.getResolution()).thenReturn(mock);

    configurationSystem.updateAttribute(attribute);

//    verify(attribute).update();
  }

  @Test
  @Ignore
  public void leaveDefaultValue() {
//    ResolvedConfiguration mock = mock(ResolvedConfiguration.class);
//    when(attribute.getResolution()).thenReturn(mock);
    // when(attribute.hasDefaultValue()).thenReturn(true);
    configurationSystem.updateAttribute(attribute);
    // verify(attribute, times(0)).setValue("a");
  }
}
