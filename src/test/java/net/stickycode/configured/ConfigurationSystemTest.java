package net.stickycode.configured;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationSystemTest {

  private VerifyingListener listener = new VerifyingListener();

  @SuppressWarnings("unused")
  @Spy
  private Set<VerifyingListener> listeners =
      new HashSet<ConfigurationSystemTest.VerifyingListener>(Arrays.asList(listener));

  @InjectMocks
  private ConfigurationSystem system = new ConfigurationSystem();

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

    system.configure();

    assertThat(listener.order)
        .containsExactly("resolve", "preConfigure", "configure", "postConfigure");
  }

}
