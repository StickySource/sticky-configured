package net.stickycode.configured.placeholder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import net.stickycode.configured.ConfigurationManifest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlaceholderResolverTest {

  @Mock
  ConfigurationManifest manifest;

  @Before
  public void before() {
    when(manifest.lookupValue("key")).thenReturn("value");
  }
  
  @Test
  public void identity() {
    assertThat(resolve("abcde")).isEqualTo("abcde");
    assertThat(resolve("abc}de")).isEqualTo("abc}de");
    assertThat(resolve("ab$c}de")).isEqualTo("ab$c}de");
    assertThat(resolve("ab${cde")).isEqualTo("ab${cde");
    assertThat(resolve("ab}${cde")).isEqualTo("ab}${cde");
  }
  
  @Test
  public void onePlaceholder() {
    assertThat(resolve("${key}")).isEqualTo("value");
    assertThat(resolve("${key} ")).isEqualTo("value ");
    assertThat(resolve(" ${key}")).isEqualTo(" value");
    assertThat(resolve(" ${key} ")).isEqualTo(" value ");
    assertThat(resolve("key ${key} key value")).isEqualTo("key value key value");
  }
  
  @Test
  public void twoPlaceholders() {
    assertThat(resolve("${key}${key}")).isEqualTo("valuevalue");
    assertThat(resolve("${key} ${key}")).isEqualTo("value value");
    assertThat(resolve(" ${key}$[key}")).isEqualTo(" valuevalue");
    assertThat(resolve(" ${key}${key} ")).isEqualTo(" valuevalue ");
    assertThat(resolve("key ${key} key ${key}value")).isEqualTo("key value key valuevalue");
  }

  private String resolve(String string) {
    return new PlaceholderResolver(manifest).resolve(string, new ResolvedValue(null, null, "key", string)).getValue();
  }

}
