package net.stickycode.configured.placeholder;

public class FoundPlaceholder
    implements Placeholder {

  private String value;

  private int start;

  private int end;

  public FoundPlaceholder(String value, int indexOfStart, int indexOfClose) {
    this.value = value;
    this.start = indexOfStart;
    this.end = indexOfClose;
  }

  @Override
  public boolean notFound() {
    return false;
  }

  @Override
  public String getKey() {
    return value.substring(start + 2, end);
  }

  @Override
  public String replace(String lookup) {
    if (start == 0)
      if (end == value.length())
        return lookup;
      else
        return lookup + value.substring(end + 1);

    return value.substring(0, start) + lookup + value.substring(end + 1);
  }

}
