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
package net.stickycode.configured.content;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import net.stickycode.stereotype.ui.Content;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalisedFieldProcessorTest {

  @Rule
  public TestName name = new TestName();

  String string;

  Content content;

  Integer notString;

  @Mock
  private LocalisedElement element;

  @Test
  public void string() throws SecurityException, NoSuchFieldException {
    process();
    verify(element).addContent(isA(LocalisedStringField.class));
  }

  @Test
  public void content() throws SecurityException, NoSuchFieldException {
    process();
    verify(element).addContent(isA(LocalisedContentField.class));
  }

  @Test(expected = LocalisedFieldIsNotStringOrContentException.class)
  public void notString() throws SecurityException, NoSuchFieldException {
    process();
  }

  private void process() throws NoSuchFieldException {
    new LocalisedFieldProcessor(element).processField(this, getField(name.getMethodName()));
  }

  private Field getField(String string) throws SecurityException, NoSuchFieldException {
    return LocalisedFieldProcessorTest.class.getDeclaredField(string);
  }

}
