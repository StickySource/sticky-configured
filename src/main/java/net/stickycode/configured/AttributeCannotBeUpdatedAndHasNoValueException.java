package net.stickycode.configured;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class AttributeCannotBeUpdatedAndHasNoValueException
    extends PermanentException {

  public AttributeCannotBeUpdatedAndHasNoValueException(Configuration configuration, ConfigurationAttribute attribute) {
    super(
        "Configuration attribute {} was registered and defined as not updateable, however it has no value. The attribute was part of ",
        attribute, configuration);
  }

}
