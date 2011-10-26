package net.stickycode.configured.content;

import java.lang.reflect.Field;

import net.stickycode.exception.PermanentException;
import net.stickycode.stereotype.ui.Content;

@SuppressWarnings("serial")
public class LocalisedFieldIsNotStringOrContentException
    extends PermanentException {

  public LocalisedFieldIsNotStringOrContentException(Class<?> target, Field attribute) {
    super("Tried to configured content '{}' of '{}' but it was of type '{}'. " +
        "Localised fields must be of type {} or {}",
        attribute.getName(), target.getSimpleName(),
        attribute.getType().getName(),
        String.class.getName(), Content.class.getName());
  }

}
