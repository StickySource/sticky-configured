package net.stickycode.configured.strategy;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.metadata.MetadataResolver;
import net.stickycode.metadata.MetadataResolverRegistry;
import net.stickycode.reflector.Fields;
import net.stickycode.stereotype.ConfiguredStrategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StrategyCoercionTest {

  @Mock
  MetadataResolverRegistry registery;

  @Mock
  MetadataResolver resolver;

  @InjectMocks
  ConfiguredStrategyCoercion coercion = new ConfiguredStrategyCoercion();

  private static interface Interface {

  }

  private Interface field;

  @Test
  public void strategyCoercionsDontHaveDefaultValues() {
    assertThat(coercion.hasDefaultValue()).isFalse();
  }

  @Test
  public void stringsAreNotStrategies() {
    when(registery.is(any(AnnotatedElement.class))).thenReturn(resolver);
    assertThat(coercion.isApplicableTo(CoercionTargets.find(String.class))).isFalse();
  }

  @Test
  public void plainOldInterfaceTargetsDontApply() {
    when(registery.is(any(AnnotatedElement.class))).thenReturn(resolver);
    assertThat(coercion.isApplicableTo(CoercionTargets.find(Interface.class))).isFalse();
  }

  @Test
  public void nonAnnotatedFieldsArentApplicable() {
    when(registery.is(any(AnnotatedElement.class))).thenReturn(resolver);
    assertThat(coercion.isApplicableTo(CoercionTargets.find(field()))).isFalse();
  }

  @Test
  public void annotatedFieldsAreApplicable() {
    when(registery.is(any(AnnotatedElement.class))).thenReturn(resolver);
    when(resolver.metaAnnotatedWith(ConfiguredStrategy.class)).thenReturn(true);
    assertThat(coercion.isApplicableTo(CoercionTargets.find(field()))).isTrue();
  }

  private Field field() {
    return Fields.find(getClass(), "field");
  }

}
