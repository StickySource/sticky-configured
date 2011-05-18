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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

import net.stickycode.stereotype.Configured;
import net.stickycode.stereotype.PostConfigured;
import net.stickycode.stereotype.PreConfigured;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AroundConfiguredMethodInvocationTest {

  static class Sample {

    @Configured
    String noDefault;

    @PreConfigured
    void preconfigured() {
    }

    @PostConfigured
    void postconfigured() {
    }

    @PostConfigured
    @PreConfigured
    Integer badReturnType() {
      return null;
    }

    @PostConfigured
    @PreConfigured
    void hasParameters(Integer badParam) {
    }
  }

  @Test
  public void preconfiguredAnnotationIsRecognised() throws SecurityException, NoSuchMethodException {
    Method m = method("preconfigured");
    assertThat(process(m, PreConfigured.class)).isTrue();
    assertThat(process(m, PostConfigured.class)).isFalse();
  }

  @Test
  public void postconfiguredAnnotationIsRecognised() throws SecurityException, NoSuchMethodException {
    Method m = method("postconfigured");
    assertThat(process(m, PostConfigured.class)).isTrue();
    assertThat(process(m, PreConfigured.class)).isFalse();
  }

  @Test(expected = ReturnTypeMustBeVoidException.class)
  public void returnsTypesMustBeVoidForPreconfigured() throws SecurityException, NoSuchMethodException {
    Method m = method("badReturnType");
    process(m, PreConfigured.class);
  }

  @Test(expected = ReturnTypeMustBeVoidException.class)
  public void returnsTypesMustBeVoidForPostconfigured() throws SecurityException, NoSuchMethodException {
    Method m = method("badReturnType");
    process(m, PostConfigured.class);
  }

  @Test(expected = AnnotatedMethodsMustNotHaveParametersException.class)
  public void annotatedMethodsMustNotHaveParametersForPreconfigured() throws SecurityException, NoSuchMethodException {
    Method m = method("hasParameters", new Class[] { Integer.class });
    process(m, PreConfigured.class);
  }

  @Test(expected = AnnotatedMethodsMustNotHaveParametersException.class)
  public void annotatedMethodsMustNotHaveParametersForPostConfigured() throws SecurityException, NoSuchMethodException {
    Method m = method("hasParameters", new Class[] { Integer.class });
    process(m, PostConfigured.class);
  }

  @Test
  public void preconfigured() throws SecurityException, NoSuchMethodException {
    Sample s = mock(Sample.class);
    Method m = method("preconfigured");
    new InvokingAnnotatedMethodProcessor(PreConfigured.class).processMethod(s, m);
    verify(s).preconfigured();
  }

  @Test
  public void postconfigured() throws SecurityException, NoSuchMethodException {
    Sample s = mock(Sample.class);
    Method m = method("postconfigured");
    new InvokingAnnotatedMethodProcessor(PostConfigured.class).processMethod(s, m);
    verify(s).postconfigured();
  }

  private Method method(String methodName) throws NoSuchMethodException {
    return Sample.class.getDeclaredMethod(methodName, new Class[0]);
  }

  private Method method(String methodName, Class<?>[] types) throws NoSuchMethodException {
    return Sample.class.getDeclaredMethod(methodName, types);
  }

  private boolean process(Method m, Class<? extends Annotation> annotationClass) {
    return new InvokingAnnotatedMethodProcessor(annotationClass).canProcess(m);
  }
}
