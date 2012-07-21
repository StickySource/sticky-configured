package net.stickycode.configured;

import java.util.Iterator;

import net.stickycode.configuration.ConfigurationKey;


public class ConfiguredAttributeIterable
    implements Iterable<ConfigurationKey> {

  public ConfiguredAttributeIterable(ConfigurationRepository configurations) {
  }

  @Override
  public Iterator<ConfigurationKey> iterator() {
    return null;
  }

}
