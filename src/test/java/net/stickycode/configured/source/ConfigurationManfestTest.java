package net.stickycode.configured.source;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.Collections;

import net.stickycode.configured.ConfigurationManifest;
import net.stickycode.configured.ConfigurationSource;
import net.stickycode.reflector.Fields;

import org.junit.Test;


public class ConfigurationManfestTest {

  ConfigurationSource source;
  
  ConfigurationManifest manifest;
  
  public void before() throws SecurityException, NoSuchFieldException {
    source = mock(ConfigurationSource.class);
    manifest = new ConfigurationManifest();
    Field f = ConfigurationManifest.class.getDeclaredField("sources");
    Fields.set(manifest, f, Collections.singleton(source));

    Field fs = ConfigurationManifest.class.getDeclaredField("systemProperties");
    Fields.set(manifest, fs, mock(SystemPropertiesConfigurationSource.class));
    
    Field fp = ConfigurationManifest.class.getDeclaredField("applicationConfiguration");
    Fields.set(manifest, fp, mock(StickyApplicationConfigurationSource.class));
  }
  
  @Test
  public void noEnv() throws SecurityException, NoSuchFieldException {
    System.clearProperty("env");
    before();
    manifest.lookupValue("a");
    verify(source).hasValue("a");
  }
  
  @Test
  public void env() throws SecurityException, NoSuchFieldException {
    System.setProperty("env", "qa");
    before();
    manifest.lookupValue("a");
    verify(source).hasValue("qa.a");
    verify(source).hasValue("a");
    System.clearProperty("env");
  }
}
