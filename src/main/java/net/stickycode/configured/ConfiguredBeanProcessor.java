package net.stickycode.configured;

import javax.inject.Inject;

import net.stickycode.reflector.Reflector;
import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyService;

@StickyFramework
@StickyService
public class ConfiguredBeanProcessor {

  @Inject
  private ConfigurationRepository configurationRepository;

  public void process(Object instance) {
    ConfiguredConfiguration configuration = new ConfiguredConfiguration(instance);
    new Reflector()
        .forEachField(new ConfiguredFieldProcessor(configuration))
        .process(instance);
    configurationRepository.register(configuration);
  }
}
