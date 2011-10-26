package net.stickycode.configured;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class ConfigurationXmlWriter {
  
  
  static class ConfigurationBeanXml {

    public ConfigurationBeanXml withConfiguation(Configuration c) {
      return this;
    }

  }

  @XmlRootElement(name="sticky")
  static class ConfigurationXml {
    
    private ConfigurationRepository repository;

    public ConfigurationXml withConfiguration(ConfigurationRepository repository) {
      this.repository = repository;
      return this;
    }
    
    @XmlElement(name="bean")
    public List<ConfigurationBeanXml> getBeans() {
      List<ConfigurationBeanXml> beans = new LinkedList<ConfigurationXmlWriter.ConfigurationBeanXml>();
      for (Configuration c : repository) {
        beans.add(new ConfigurationBeanXml().withConfiguation(c));
      }
      return beans;
    }

  }

  @Inject
  private ConfigurationRepository repository;
  
  public String toXml() {
    Marshaller marshaller = createContext();
    StringWriter writer = new StringWriter();
    try {
      marshaller.marshal(new ConfigurationXml().withConfiguration(repository), writer);
    }
    catch (JAXBException e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  private Marshaller createContext() {
    try {
      return JAXBContext.newInstance(ConfigurationXml.class).createMarshaller();
    }
    catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }


}
