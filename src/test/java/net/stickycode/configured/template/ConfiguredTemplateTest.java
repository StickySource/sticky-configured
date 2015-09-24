package net.stickycode.configured.template;

import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import mockit.Injectable;
import mockit.Tested;
import net.stickycode.configured.ConfigurationRepository;
import net.stickycode.configured.ConfiguredAnnotations;
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

  @Injectable
  ConfiguredAnnotations annotations;

  @Tested
  ConfiguredBeanProcessor configuredBeanProcessor;

  @Test
  @Ignore("really?")
  public void templated() {
    SampleTemplate template = new SampleTemplate();
    configuredBeanProcessor.process(template);
    assertThat(template.value).isNotNull();
  }
}
