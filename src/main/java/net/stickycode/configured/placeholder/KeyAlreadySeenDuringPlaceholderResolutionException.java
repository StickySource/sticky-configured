package net.stickycode.configured.placeholder;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class KeyAlreadySeenDuringPlaceholderResolutionException
    extends PermanentException {

  public KeyAlreadySeenDuringPlaceholderResolutionException(Placeholder placeholder, ResolvedValue resolution) {
    super("Encountered placeholder {} again when resolving {}", placeholder, resolution);
  }

}
