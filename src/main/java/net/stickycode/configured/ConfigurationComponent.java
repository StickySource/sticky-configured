package net.stickycode.configured;

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ResolvedConfiguration;

public class ConfigurationComponent
    implements ConfigurationAttribute {

  private final Object value;

  private final CoercionTarget target;

  private final String name;

  private ResolvedConfiguration resolution;

  private Object configuredValue;

  public ConfigurationComponent(Object value, CoercionTarget target, String name) {
    super();
    this.value = value;
    this.target = target;
    this.name = name;
  }

  @Override
  public Class<?> getType() {
    return value.getClass();
  }

  @Override
  public boolean hasDefaultValue() {
    return false;
  }

  @Override
  public Object getDefaultValue() {
    throw new UnsupportedOperationException("No default value");
  }

  @Override
  public void setValue(Object value) {
    throw new UnsupportedOperationException("No default value");
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return target;
  }

  @Override
  public boolean canBeUpdated() {
    return false;
  }

  @Override
  public boolean hasValue() {
    return true;
  }

  @Override
  public String join(String delimeter) {
    return name;
  }

  @Override
  public void resolvedWith(ResolvedConfiguration resolved) {
    this.resolution = resolved;
  }

  @Override
  public ResolvedConfiguration getResolution() {
    return resolution;
  }

  @Override
  public void applyCoercion(CoercionFinder coercions) {
    Coercion<?> coercion = coercions.find(target);
    this.configuredValue = coercion.coerce(target, resolution.getValue());
  }

  @Override
  public void update() {
    if (value == null)
      throw new AttributeCannotBeUpdatedAndHasNoValueException(this);
  }

  @Override
  public void invertControl(ComponentContainer container) {
    container.inject(value);
  }


}
