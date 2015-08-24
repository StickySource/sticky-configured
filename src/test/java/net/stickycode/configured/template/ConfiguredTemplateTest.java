package net.stickycode.configured.template;

import org.junit.Test;

import mockit.Injectable;
import mockit.Tested;
import net.stickycode.configured.ConfigurationRepository;
import net.stickycode.configured.ConfiguredBeanProcessor;

/**
 * When I finished this it should prove that
 *
 * <pre>
 * class AbstractBean {
 *   &#064;Configured
 *   private String inherited;
 *
 * }
 *
 * class Child extends AbstractBean {
 *
 * }
 * </pre>
 *
 * can have inherited configured by 'abstractBean.inherited' and 'child.inherited'
 */
public class ConfiguredTemplateTest {

  @Injectable
  ConfigurationRepository repository;

  @Tested
  ConfiguredBeanProcessor configuredBeanProcessor;

  @Test
  public void templated() {
    configuredBeanProcessor.process(new SampleTemplate());
  }
}
