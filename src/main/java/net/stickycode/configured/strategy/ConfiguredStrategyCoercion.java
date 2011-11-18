package net.stickycode.configured.strategy;

import javax.inject.Inject;

import net.stickycode.coercion.AbstractFailedToCoerceValueException;
import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.stereotype.ConfiguredStrategy;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
public class ConfiguredStrategyCoercion
    implements Coercion<Object> {

  @Inject
  private StrategyFinder finder;

  @Override
  public Object coerce(CoercionTarget type, String value)
      throws AbstractFailedToCoerceValueException {
    return finder.findWithName(type.getType(), value);
  }

  @Override
  public boolean isApplicableTo(CoercionTarget type) {
    if (!type.hasAnnotation(ConfiguredStrategy.class))
      return false;

    if (!type.getType().isInterface())
      throw new ConfiguredStrategyTargetsMustBeInterfaces(type);

    return true;
  }

}
