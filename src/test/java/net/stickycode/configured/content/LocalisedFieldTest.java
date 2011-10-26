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
package net.stickycode.configured.content;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Field;

import net.stickycode.stereotype.ui.Content;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;


public class LocalisedFieldTest {
  
  @Rule
  public TestName name = new TestName();
  
  @SuppressWarnings("unused")
  private static class OneField {
    private String string;
    private String defaultedString = "blah";
    private String accessible;
    private Content content;
  }

  @Test
  public void string() throws SecurityException, NoSuchFieldException {
    LocalisedAttribute f = getAttribute();
    assertThat(f.getName()).isEqualTo("string");
    assertThat(f.getType()).isEqualTo(String.class);
  }
  
  @Test
  public void content() throws SecurityException, NoSuchFieldException {
    LocalisedAttribute f = getAttribute();
    assertThat(f.getName()).isEqualTo("content");
    assertThat(f.getType()).isEqualTo(Content.class);
  }

  @Test
  public void defaultedString() throws SecurityException, NoSuchFieldException {
    LocalisedAttribute f = getAttribute();
    assertThat(f.getType()).isEqualTo(String.class);
  }

  @Test
  public void setString() throws SecurityException, NoSuchFieldException {
    LocalisedAttribute f = getAttribute("string");
    assertThat(f.getType()).isEqualTo(String.class);
    f.setValue("blah");
    assertThat(f.getValue()).isEqualTo("blah");
  }

  @Test
  public void setContent() throws SecurityException, NoSuchFieldException {
    LocalisedAttribute f = getAttribute("content");
    assertThat(f.getType()).isEqualTo(Content.class);
    f.setValue("blah");
    assertThat(f.getValue()).isEqualTo("blah");
    f.setValue(new FixedContent("blue"));
    assertThat(f.getValue()).isEqualTo("blue");
  }

  private LocalisedAttribute getAttribute() throws NoSuchFieldException {
    return getAttribute(name.getMethodName());
  }

  private LocalisedAttribute getAttribute(String methodName) throws NoSuchFieldException {
    Field field = OneField.class.getDeclaredField(methodName);
    return new LocalisedFieldProcessor(null).createLocalisedAttribute(new OneField(), field);
  }
}
