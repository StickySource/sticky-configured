package net.stickycode.configured.placeholder;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class PlaceholderTest {

  @Test
  public void foundPlaceHolderKey() {
    String value = "abc${key}hij";
    assertThat(value.indexOf("}")).isEqualTo(8);
    assertThat(value.lastIndexOf("${", 8)).isEqualTo(3);
    assertThat(new FoundPlaceholder(value, 3, 8).getKey()).isEqualTo("key");
  }
  
  @Test
  public void foundPlaceHolderKey2() {
    String value = "abc${keykey}hij";
    assertThat(value.indexOf("}")).isEqualTo(11);
    assertThat(value.lastIndexOf("${", 11)).isEqualTo(3);
    assertThat(new FoundPlaceholder(value, 3, 11).getKey()).isEqualTo("keykey");
  }
  
  @Test
  public void foundPlaceHolderFound() {
    assertThat(new FoundPlaceholder("abc${key}hij", 4, 9).notFound()).isFalse();
  }
  
  @Test
  public void foundPlaceHolderReplace() {
    assertThat(new FoundPlaceholder("abc${key}hij", 3, 8).replace("def")).isEqualTo("abcdefhij");
    assertThat(new FoundPlaceholder("${key}", 0, 5).replace("def")).isEqualTo("def");
  }
}
