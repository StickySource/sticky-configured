package net.stickycode.configured;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import net.stickycode.reflector.AnnotationFinder;
import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.StickyFramework;

@StickyComponent
@StickyFramework
public class ConfiguredAnnotations {

  private static Class<? extends Annotation>[] configuredAnnotations;

  private static Map<Class<? extends Annotation>, Method> defaultSeeds = new HashMap<>();

  static {
    configuredAnnotations = AnnotationFinder.load("co.nfigured", "configured");
    LoggerFactory.getLogger(ConfiguredFieldProcessor.class).info("configuring with {}", (Object[]) configuredAnnotations);
    for (Class<? extends Annotation> type : configuredAnnotations) {
      try {
        Method defaultValue = type.getDeclaredMethod("defaultValue");
        if (String.class.isAssignableFrom(defaultValue.getReturnType()))
          defaultSeeds.put(type, defaultValue);
      }
      catch (NoSuchMethodException e) {
        // do nothing as its go no default value
      }
    }
  }

  public Class<? extends Annotation>[] getConfiguredAnnotations() {
    return configuredAnnotations;
  }

  public Map<Class<? extends Annotation>, Method> getDefaultSeeds() {
    return defaultSeeds;
  }
}
