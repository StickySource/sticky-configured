package net.stickycode.configured;

import javax.inject.Inject;

import net.stickycode.configuration.ConfigurationTarget;
import net.stickycode.reflector.Reflector;
import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyService;

@StickyFramework
@StickyService
public class ConfiguredBeanProcessor {

  @Inject
  private ConfigurationRepository configurationRepository;

  public void process(Object instance) {
    configurationRepository.register(new ForMethodOnlyBeansDummyAttribute(instance));

    if (instance instanceof ConfigurationTarget)
      process((ConfigurationTarget) instance, instance);
    else
      process(new SimpleNameConfigurationTarget(instance), instance);
  }

  public void process(ConfigurationTarget parent, Object target) {
    new Reflector()
        .forEachField(new ConfiguredFieldProcessor(configurationRepository, parent))
        .process(target);
  }

}
