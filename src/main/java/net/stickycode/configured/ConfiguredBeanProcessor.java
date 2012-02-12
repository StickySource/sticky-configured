package net.stickycode.configured;

import java.beans.Introspector;

import javax.inject.Inject;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configured.finder.BeanFinder;
import net.stickycode.reflector.Reflector;
import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyService;

@StickyFramework
@StickyService
public class ConfiguredBeanProcessor {

  @Inject
  private ConfigurationRepository configurationRepository;
  
  @Inject
  private BeanFinder finder;

  public void process(Object instance) {
    String name = Introspector.decapitalize(instance.getClass().getSimpleName());
    process(instance, name, null);
  }

  public void process(Object instance, String name, CoercionTarget parent) {
    ConfiguredConfiguration configuration = new ConfiguredConfiguration(
        instance,
        name);
    
    new Reflector()
        .forEachField(new ConfiguredFieldProcessor(this, configuration, parent, finder))
        .process(instance);
    
    configurationRepository.register(configuration);
  }
}
