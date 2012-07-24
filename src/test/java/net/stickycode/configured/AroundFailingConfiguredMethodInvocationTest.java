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

import static org.fest.assertions.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.stickycode.stereotype.configured.PostConfigured;
import net.stickycode.stereotype.configured.PreConfigured;

import org.junit.Test;

public class AroundFailingConfiguredMethodInvocationTest {

  static class Fail {

    @PreConfigured
    void preconfigured() {
      throw new NullPointerException();
    }

    @PostConfigured
    void postconfigured() {
      throw new NullPointerException();
    }

  }

  @Test(expected = FailedToInvokeAnnotatedMethodException.class)
  public void preconfiguredAnnotationIsRecognised() throws SecurityException, NoSuchMethodException {
    Method m = method("preconfigured");
    process(m, PreConfigured.class);
  }

  @Test(expected = FailedToInvokeAnnotatedMethodException.class)
  public void postconfiguredAnnotationIsRecognised() throws SecurityException, NoSuchMethodException {
    Method m = method("postconfigured");
    process(m, PostConfigured.class);
  }

  private Method method(String methodName) throws NoSuchMethodException {
    return Fail.class.getDeclaredMethod(methodName, new Class[0]);
  }

  private void process(Method m, Class<? extends Annotation> annotationClass) {
    try {
      new InvokingAnnotatedMethodProcessor(annotationClass).processMethod(new Fail(), m);
    }
    catch (FailedToInvokeAnnotatedMethodException e) {
      assertThat(e.getCause()).isNotNull();
      throw e;
    }
  }
}
