package net.stickycode.configured.placeholder;

import net.stickycode.configured.Configuration;
import net.stickycode.configured.ConfigurationAttribute;

public class ResolvedValue {

  private final Configuration configuration;

  private final ConfigurationAttribute attribute;

  private final String key;

  private final String seed;

  private String value;

  public ResolvedValue(Configuration configuration, ConfigurationAttribute attribute, String key, String seed2) {
    this.configuration = configuration;
    this.attribute = attribute;
    this.key = key;
    this.seed = seed2;
  }

  public boolean isResolved() {
    return value != null;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    if (!isResolved())
      return seed + " (unresolved)";

    if (seed.equals(value))
      return seed;

    return seed + "->" + value;
  }

  public ResolvedValue withValue(String value2) {
    this.value = value2;
    return this;
  }

}
