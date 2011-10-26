package net.stickycode.configured.content;

import java.lang.reflect.Field;

import net.stickycode.reflector.Fields;
import net.stickycode.stereotype.ui.Content;

public class LocalisedContentField
    implements LocalisedAttribute {

  private final Object bean;

  private final Field field;

  public LocalisedContentField(Object bean, Field field) {
    this.bean = bean;
    this.field = field;
  }

  @Override
  public Class<?> getType() {
    return Content.class;
  }

  @Override
  public String getName() {
    return field.getName();
  }

  @Override
  public void setValue(String value) {
    setValue(new FixedContent(value));
  }

  @Override
  public void setValue(Content content) {
    Fields.set(bean, field, content);
  }

  @Override
  public String getValue() {
    Content value = Fields.get(bean, field);
    return value.get();
  }

}
