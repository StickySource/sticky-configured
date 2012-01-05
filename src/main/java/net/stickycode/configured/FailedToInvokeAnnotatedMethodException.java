package net.stickycode.configured;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class FailedToInvokeAnnotatedMethodException
    extends PermanentException {

  public FailedToInvokeAnnotatedMethodException(InvocationTargetException e, Class<? extends Annotation> annotationClass,
      Object target, Method method) {
    super(e, "Failed to invoked {} on {} annotated with {}", method.getName(), target.getClass().getSimpleName(), annotationClass);
  }

}
