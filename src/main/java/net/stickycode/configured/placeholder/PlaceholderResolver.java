package net.stickycode.configured.placeholder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.configured.ConfigurationManifest;

public class PlaceholderResolver {

  private ConfigurationManifest manifest;

  public PlaceholderResolver(ConfigurationManifest manifest) {
    super();
    this.manifest = manifest;
  }

  public ResolvedValue resolve(String value, ResolvedValue resolution) {
    if (value == null)
      return resolution;
    
    Placeholder placeholder = find(value);
    if (placeholder.notFound())
      return resolution.withValue(value);
    
    String lookup = manifest.lookupValue(placeholder.getKey());
    return resolve(placeholder.replace(lookup), resolution);
  }

  private Placeholder find(String value) {
    int indexOfClose = value.indexOf('}');
    if (indexOfClose == -1)
      return new NoPlaceholder();
    
    int indexOfStart = value.lastIndexOf("${", indexOfClose);
    if (indexOfStart == -1)
      return new NoPlaceholder();
    
    return new FoundPlaceholder(value, indexOfStart, indexOfClose);
  }
}
