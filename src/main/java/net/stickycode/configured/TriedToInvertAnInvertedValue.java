package net.stickycode.configured;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class TriedToInvertAnInvertedValue
    extends PermanentException {

  public TriedToInvertAnInvertedValue(Configuration configuration) {
    super(
      "A coercion for one of the the following attributes on {} produces inverted values but has not defined isInverted as true.",
      configuration);
  }

}
