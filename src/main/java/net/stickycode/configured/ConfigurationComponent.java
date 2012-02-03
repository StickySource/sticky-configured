package net.stickycode.configured;

import net.stickycode.coercion.CoercionTarget;

public class ConfigurationComponent
    implements ConfigurationAttribute {

  private final Object value;

  private final CoercionTarget target;

  private final String name;

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

}
