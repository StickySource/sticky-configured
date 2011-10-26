package net.stickycode.configured.content;

import java.lang.reflect.Field;

import net.stickycode.reflector.FieldProcessor;
import net.stickycode.stereotype.ui.ConfiguredContent;
import net.stickycode.stereotype.ui.Content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalisedFieldProcessor
    implements FieldProcessor {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final LocalisedElement bean;

  public LocalisedFieldProcessor(LocalisedElement bean) {
    this.bean = bean;
  }

  @Override
  public boolean canProcess(Field field) {
    if (field.isAnnotationPresent(ConfiguredContent.class)) {
      if (field.getType().isAssignableFrom(String.class))
        return true;

      if (field.getType().isAssignableFrom(Content.class))
        return true;
      
      throw new LocalisedFieldIsNotStringOrContentException(field.getDeclaringClass(), field);
    }

    return false;
  }

  @Override
  public void processField(Object target, Field attribute) {
    LocalisedAttribute configuredField = createLocalisedAttribute(target, attribute);
    log.debug("registering content {}", configuredField);
    bean.addContent(configuredField);
  }

  LocalisedAttribute createLocalisedAttribute(Object target, Field attribute) {
    if (attribute.getType().isAssignableFrom(String.class))
      return new LocalisedStringField(target, attribute);

    if (attribute.getType().isAssignableFrom(Content.class))
      return new LocalisedContentField(target, attribute);
    
    throw new LocalisedFieldIsNotStringOrContentException(target.getClass(), attribute);
  }

}
