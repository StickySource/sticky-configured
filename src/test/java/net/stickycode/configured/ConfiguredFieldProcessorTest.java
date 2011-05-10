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

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


public class ConfiguredFieldProcessorTest {

  Integer integer;
  int primitive;

  @Mock
  private Configuration configuration;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected=ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossibleException.class)
  public void primitive() throws SecurityException, NoSuchFieldException {
    new ConfiguredFieldProcessor(configuration).processField(this, getField("primitive"));
  }

  @Test
  public void processed() throws SecurityException, NoSuchFieldException {
    new ConfiguredFieldProcessor(configuration).processField(this, getField("integer"));
    verify(configuration).addAttribute(Mockito.any(ConfiguredField.class));
  }

  private Field getField(String string) throws SecurityException, NoSuchFieldException {
    return ConfiguredFieldProcessorTest.class.getDeclaredField(string);
  }

}
