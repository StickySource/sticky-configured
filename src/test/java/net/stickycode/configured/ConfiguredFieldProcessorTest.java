/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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

import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ConfiguredFieldProcessorTest {

  Integer integer;
  int primitive;

  @Mock
  private ConfiguredConfiguration configuration;
  
  @Mock
  private ConfiguredBeanProcessor processor;

  @Test(expected=ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossibleException.class)
  public void primitive() throws SecurityException, NoSuchFieldException {
    new ConfiguredFieldProcessor(configuration, null).processField(this, getField("primitive"));
  }

  @Test
  public void processed() throws SecurityException, NoSuchFieldException {
    new ConfiguredFieldProcessor(configuration, null).processField(this, getField("integer"));
    verify(configuration).addAttribute(Mockito.any(ConfiguredField.class));
  }

  private Field getField(String string) throws SecurityException, NoSuchFieldException {
    return ConfiguredFieldProcessorTest.class.getDeclaredField(string);
  }

}
