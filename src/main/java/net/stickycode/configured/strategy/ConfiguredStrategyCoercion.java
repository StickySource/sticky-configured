package net.stickycode.configured.strategy;

import java.lang.reflect.AnnotatedElement;

import javax.inject.Inject;

import net.stickycode.coercion.AbstractNoDefaultCoercion;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.metadata.MetadataResolverRegistry;
import net.stickycode.stereotype.StickyPlugin;
import net.stickycode.stereotype.configured.ConfiguredStrategy;

@StickyPlugin
public class ConfiguredStrategyCoercion
    extends AbstractNoDefaultCoercion<Object> {
  
  @Inject
  private StrategyFinder finder;
  
  @Inject
  private MetadataResolverRegistry metadataRegistry;

  @Override
  public Object coerce(CoercionTarget type, String value) {
    return finder.findWithName(type.getType(), value);
  }

  @Override
  public boolean isApplicableTo(CoercionTarget type) {
    if (!type.canBeAnnotated())
      return false;
    
    AnnotatedElement annotatedElement = type.getAnnotatedElement();
    if (!metadataRegistry.is(annotatedElement).metaAnnotatedWith(ConfiguredStrategy.class))
      return false;
    
    if (!type.getType().isInterface())
      throw new ConfiguredStrategyTargetsMustBeInterfaces(type);

    return true;
  }

}
