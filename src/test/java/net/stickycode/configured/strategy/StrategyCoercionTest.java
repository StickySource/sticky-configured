package net.stickycode.configured.strategy;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.metadata.MetadataResolver;
import net.stickycode.metadata.MetadataResolverRegistry;
import net.stickycode.reflector.Fields;
import net.stickycode.stereotype.configured.ConfiguredStrategy;

public class StrategyCoercionTest {

  @Injectable
  MetadataResolverRegistry registry;

  @Injectable
  MetadataResolver resolver;

  @Injectable
  StrategyFinder finder;

  @Tested
  ConfiguredStrategyCoercion coercion;

  private static interface Interface {

  }

  private Interface field;


  @Before
  public void setup () {
    new Expectations() {
      {
        registry.is(withInstanceOf(AnnotatedElement.class));
        result = resolver;
        minTimes = 0;
      }
    };
  }
  @Test
  public void strategyCoercionsDontHaveDefaultValues() {
    assertThat(coercion.hasDefaultValue()).isFalse();
  }

  @Test
  public void stringsAreNotStrategies() {
    assertThat(coercion.isApplicableTo(CoercionTargets.find(String.class))).isFalse();
  }

  @Test
  public void plainOldInterfaceTargetsDontApply() {
    assertThat(coercion.isApplicableTo(CoercionTargets.find(Interface.class))).isFalse();
  }

  @Test
  public void nonAnnotatedFieldsArentApplicable() {
    assertThat(coercion.isApplicableTo(CoercionTargets.find(field()))).isFalse();
  }

  @Test
  public void annotatedFieldsAreApplicable() {


    new Expectations() {
      {
        resolver.metaAnnotatedWith(withEqual(ConfiguredStrategy.class));
        result = true;
      }
    };
    assertThat(coercion.isApplicableTo(CoercionTargets.find(field()))).isTrue();
  }

  private Field field() {
    return Fields.find(getClass(), "field");
  }

}
