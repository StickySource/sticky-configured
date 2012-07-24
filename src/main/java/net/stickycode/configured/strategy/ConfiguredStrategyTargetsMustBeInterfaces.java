package net.stickycode.configured.strategy;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.exception.PermanentException;
import net.stickycode.stereotype.configured.ConfiguredStrategy;

@SuppressWarnings("serial")
public class ConfiguredStrategyTargetsMustBeInterfaces
    extends PermanentException {

  public ConfiguredStrategyTargetsMustBeInterfaces(CoercionTarget type) {
    super(
        "The coercion target being a {} was annotated with @{} but not type {} is not an interface. "
            + "IMO to allow other than interfaces in this context would not show a clear intention. "
            + "If you disagree let me know and we can talk",
        type, ConfiguredStrategy.class.getName(), type.getType().getName());
  }

}
