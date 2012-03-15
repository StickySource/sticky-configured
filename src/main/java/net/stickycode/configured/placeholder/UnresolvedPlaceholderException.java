package net.stickycode.configured.placeholder;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class UnresolvedPlaceholderException
    extends PermanentException {

  public UnresolvedPlaceholderException(String value, Placeholder placeholder) {
    super("A value for key {} could not be found when trying to resolve {}", placeholder.getKey(), value);
  }

}
