package net.stickycode.configured;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import mockit.Injectable;
import mockit.Tested;

public class ConfigurationSystemTest {

  private VerifyingListener listener = new VerifyingListener();

  @Injectable
  private Set<ConfigurationListener> listeners =
      new HashSet<ConfigurationListener>(Arrays.asList(listener));

  @Tested
  private ConfigurationSystem system;

  public static class VerifyingListener
      implements ConfigurationListener {

    private List<String> order = new ArrayList<String>();

    @Override
    public void resolve() {
      order.add("resolve");
    }

    @Override
    public void preConfigure() {
      order.add("preConfigure");
    }

    @Override
    public void configure() {
      order.add("configure");
    }

    @Override
    public void postConfigure() {
      order.add("postConfigure");
    }
  }

  @Test
  public void verify() {
    assertThat(listener.order).isEmpty();

    system.start();

    assertThat(listener.order)
        .containsExactly("resolve", "preConfigure", "configure", "postConfigure");
  }

}
