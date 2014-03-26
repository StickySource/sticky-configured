package net.stickycode.configured;

import java.beans.Introspector;

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ResolvedConfiguration;

public class ForMethodOnlyBeansDummyAttribute
    implements ConfigurationAttribute {

  private Object instance;

  public ForMethodOnlyBeansDummyAttribute(Object instance) {
    this.instance = instance;
  }

  @Override
  public void resolvedWith(ResolvedConfiguration resolved) {
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return null;
  }

  @Override
  public String join(String delimeter) {
    return null;
  }

  @Override
  public void applyCoercion(CoercionFinder coercions) {
  }

  @Override
  public void update() {
  }

  @Override
  public void invertControl(ComponentContainer container) {
  }

  @Override
  public boolean requiresResolution() {
    return false;
  }

  @Override
  public ResolvedConfiguration getResolution() {
    return null;
  }

  @Override
  public Object getTarget() {
    return instance;
  }

  @Override
  public String toString() {
    return Introspector.decapitalize(instance.getClass().getSimpleName());
  }

}
