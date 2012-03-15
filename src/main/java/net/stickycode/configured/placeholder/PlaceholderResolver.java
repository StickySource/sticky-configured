package net.stickycode.configured.placeholder;

import java.util.ArrayDeque;
import java.util.Deque;

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
    if (lookup == null)
      throw new UnresolvedPlaceholderException(value, placeholder);

    ArrayDeque<Placeholder> seen = new ArrayDeque<Placeholder>();
    seen.push(placeholder);
    return resolve(placeholder.replace(lookup), resolution, seen);
  }

  ResolvedValue resolve(String value, ResolvedValue resolution, Deque<Placeholder> seen) {
    if (value == null)
      return resolution;

    Placeholder placeholder = find(value);
    if (placeholder.notFound())
      return resolution.withValue(value);

    for (Placeholder p : seen) {
      if (p.contains(placeholder))
        throw new KeyAlreadySeenDuringPlaceholderResolutionException(placeholder, resolution);
    }

    String lookup = manifest.lookupValue(placeholder.getKey());

    seen.push(placeholder);
    ResolvedValue resolve = resolve(placeholder.replace(lookup), resolution, seen);
    seen.pop();
    return resolve;
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
