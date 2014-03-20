package net.stickycode.configured.template;

import net.stickycode.configured.ConfigurationRepository;
import net.stickycode.configured.ConfiguredBeanProcessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
@RunWith(MockitoJUnitRunner.class)
public class ConfiguredTemplateTest {

  @Mock
  ConfigurationRepository repository;

  @InjectMocks
  ConfiguredBeanProcessor configuredBeanProcessor;

  @Test
  public void templated() {
    configuredBeanProcessor.process(new SampleTemplate());
  }
}
