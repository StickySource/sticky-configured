package net.stickycode.configured;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.stickycode.reflector.Fields;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationXmlGenerationTest {

  String value;

  @Mock
  private ConfigurationRepository configuration;

  @InjectMocks
  private ConfigurationXmlWriter writer = new ConfigurationXmlWriter();

  @Test
  public void empty() {
    when(configuration.iterator()).thenReturn(Collections.<Configuration> emptyList().iterator());
    assertThat(writer.toXml()).contains("<sticky ").contains("generatedAt=\"20");
  }

  @Test
  public void justBean() {
    when(configuration.iterator()).thenReturn(iterator());
    assertThat(writer.toXml())
        .contains("<sticky generatedAt=\"20")
        .contains("<bean ")
        .contains(" type=\"" + this.getClass().getName() + "\"")
        .contains(" name=\"" + Introspector.decapitalize(this.getClass().getSimpleName()) + "\"");
    ;
  }

  @Test
  public void beanWithOneField() {
    when(configuration.iterator()).thenReturn(iterator(field()));
    assertThat(writer.toXml())
        .contains("<sticky generatedAt=\"20")
        .contains("<bean ")
        .contains(" type=\"" + this.getClass().getName() + "\"")
        .contains(" name=\"" + Introspector.decapitalize(this.getClass().getSimpleName()) + "\"");
  }

  private ConfigurationAttribute field() {
    return new ConfiguredField(this, Fields.find(getClass(), "value"));
  }

  private Iterator<Configuration> iterator(ConfigurationAttribute... attributes) {
    List<Configuration> list = new ArrayList<Configuration>();
    ConfiguredConfiguration configuration = new ConfiguredConfiguration(this);
    for (ConfigurationAttribute a : configuration) {
      configuration.addAttribute(a);
    }
    list.add(configuration);
    return list.iterator();
  }

}
