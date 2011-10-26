package net.stickycode.configured.content;

import net.stickycode.stereotype.ui.Content;

public class FixedContent
    implements Content {

  private final String value;

  public FixedContent(String value) {
    this.value = value;
  }

  @Override
  public String get() {
    return value;
  }

}