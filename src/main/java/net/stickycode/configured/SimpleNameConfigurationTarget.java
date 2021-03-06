package net.stickycode.configured;

import java.beans.Introspector;
import java.util.Collections;
import java.util.List;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ConfigurationTarget;
import net.stickycode.configuration.ResolvedConfiguration;


public class SimpleNameConfigurationTarget
    implements ConfigurationTarget {

  private String name;

  public SimpleNameConfigurationTarget(Class<?> name) {
    this.name = Introspector.decapitalize(name.getSimpleName());
  }

  public SimpleNameConfigurationTarget(Object instance) {
    this(instance.getClass());
  }

  @Override
  public List<String> join(String delimeter) {
    return Collections.singletonList(name);
  }

  @Override
  public void resolvedWith(ResolvedConfiguration resolved) {
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return null;
  }

  @Override
  public String toString() {
    return name;
  }

}
