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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.stickycode.reflector.MethodProcessor;

public class InvokingAnnotatedMethodProcessor
    implements MethodProcessor {

  private final Class<? extends Annotation> annotationClass;

  public InvokingAnnotatedMethodProcessor(Class<? extends Annotation> annotationClass) {
    super();
    this.annotationClass = annotationClass;
  }

  @Override
  public void processMethod(Object target, Method method) {
    try {
      method.invoke(target, new Object[0]);
    }
    catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (InvocationTargetException e) {
      throw new FailedToInvokeAnnotatedMethodException(e, annotationClass, target, method);
    }
  }

  @Override
  public boolean canProcess(Method method) {
    if (!method.isAnnotationPresent(annotationClass))
      return false;

    if (!Void.TYPE.equals(method.getReturnType()))
      throw new ReturnTypeMustBeVoidException(annotationClass, method);

    if (method.getParameterTypes().length > 0)
      throw new AnnotatedMethodsMustNotHaveParametersException(annotationClass, method);

    return true;
  }

  @Override
  public void sort(List<Method> methods) {
  }

}
