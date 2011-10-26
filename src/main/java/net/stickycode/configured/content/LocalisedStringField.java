package net.stickycode.configured.content;

import java.lang.reflect.Field;

import net.stickycode.reflector.Fields;
import net.stickycode.stereotype.ui.Content;

public class LocalisedStringField
    implements LocalisedAttribute {

  private final Object bean;

  private final Field field;

  public LocalisedStringField(Object bean, Field field) {
    this.bean = bean;
    this.field = field;
  }

  @Override
  public Class<?> getType() {
    return String.class;
  }

  @Override
  public String getName() {
    return field.getName();
  }

  @Override
  public void setValue(String value) {
    setString(value);
  }

  private void setString(String value) {
    Fields.set(bean, field, value);
  }

  @Override
  public void setValue(Content content) {
    throw new UnsupportedOperationException("String fields can only be set with strings not " + Content.class.getName());
  }

  @Override
  public String getValue() {
    return Fields.get(bean, field);
  }

}
