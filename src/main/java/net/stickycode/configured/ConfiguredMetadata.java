package net.stickycode.configured;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.LoggerFactory;

import net.stickycode.metadata.MetadataResolverRegistry;
import net.stickycode.reflector.AnnotationFinder;
import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.configured.AfterConfiguration;
import net.stickycode.stereotype.configured.BeforeConfiguration;
import net.stickycode.stereotype.configured.PostConfigured;
import net.stickycode.stereotype.configured.PreConfigured;

@StickyComponent
@StickyFramework
public class ConfiguredMetadata {

  private static Class<? extends Annotation>[] configuredAnnotations;

  @SuppressWarnings("unchecked")
  private static Class<? extends Annotation>[] configuredLifecycleAnnotations = new Class[] { PreConfigured.class,
    PostConfigured.class, AfterConfiguration.class, BeforeConfiguration.class };

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

  @Inject
  MetadataResolverRegistry metdataResolverRegistry;

  public Class<? extends Annotation>[] getConfiguredAnnotations() {
    return configuredAnnotations;
  }

  public Class<? extends Annotation>[] getConfiguredLifecycleAnnotations() {
    return configuredLifecycleAnnotations;
  }

  public Map<Class<? extends Annotation>, Method> getDefaultSeeds() {
    return defaultSeeds;
  }

  public boolean typeIsConfigured(Class<?> type) {
    if (metdataResolverRegistry
      .does(type)
      .haveAnyFieldsMetaAnnotatedWith(getConfiguredAnnotations()))
      return true;

    if (metdataResolverRegistry
      .does(type)
      .haveAnyMethodsMetaAnnotatedWith(getConfiguredLifecycleAnnotations()))
      return true;

    return false;
  }

}
