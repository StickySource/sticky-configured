package net.stickycode.configured;

import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class ConfigurationSources {
  
  private Logger log = LoggerFactory.getLogger(getClass());
  
  @Inject
  private Set<ConfigurationSource> sources;

  public void resolve(ConfigurationRepository configurations) {
    
  }
  
  /**
   * @return the value to use or null one is not defined in any configuration source
   */
  String lookupValue(String key) {
    for (ConfigurationSource s : sources) {
      if (s.hasValue(key))
        return s.getValue(key);
    }

    log.debug("value not found for key '{}'", key);

    return null;
  }
}
