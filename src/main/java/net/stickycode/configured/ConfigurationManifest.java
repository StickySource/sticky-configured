package net.stickycode.configured;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import net.stickycode.configured.placeholder.PlaceholderResolver;
import net.stickycode.configured.placeholder.ResolvedValue;
import net.stickycode.stereotype.StickyComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
public class ConfigurationManifest {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private Set<ConfigurationSource> sources;

  @Inject
  private ConfigurationKeyBuilder keyBuilder;

  private String environment = System.getProperty("env");

  private Map<String, ResolvedValue> resolved;

  @PostConstruct
  public void startup() {
    if (environment != null)
      log.info("Using environment {} when resolving configuration", environment);
    else
      log.debug("No environment is specified for resolving configuration");
  }

  public void resolve(ConfigurationRepository configurations) {
    PlaceholderResolver resolver = new PlaceholderResolver(this);
    Map<String, ResolvedValue> resolutions = new HashMap<String, ResolvedValue>();
    for (Configuration configuration : configurations)
      for (ConfigurationAttribute a : configuration) {
        String key = keyBuilder.build(configuration, a);
        String seed = lookupValue(key);
        log.debug("resolving key '{}' with seed '{}'", key, seed);
        resolutions.put(key, resolver.resolve(seed, new ResolvedValue(configuration, a, key, seed)));
      }

    log.debug("resolutions {}", resolutions);
    resolved = resolutions;
  }

  /**
   * @return the value to use or null if one is not defined in any configuration source
   */
  public String lookupValue(String key) {
    for (ConfigurationSource s : sources) {
      if (environment != null)
        if (s.hasValue(environment + "." + key))
          return s.getValue(environment + "." + key);

      if (s.hasValue(key))
        return s.getValue(key);
    }

    log.debug("value not found for key '{}'", key);

    return null;
  }

  public ResolvedValue find(String key) {
    return resolved.get(key);
  }
  
  @Override
  public String toString() {
    return sources.toString();
  }

}
