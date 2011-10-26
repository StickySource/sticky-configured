package net.stickycode.configured;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationXmlGenerationTest {
  
  @Mock
  private ConfigurationRepository configuration;
  
  @InjectMocks
  private ConfigurationXmlWriter writer = new ConfigurationXmlWriter();
  
  @Test
  public void test() {
    when(configuration.iterator()).thenReturn(Collections.<Configuration>emptyList().iterator());
    assertThat(writer.toXml()).contains("<sticky/>");
  }

}
