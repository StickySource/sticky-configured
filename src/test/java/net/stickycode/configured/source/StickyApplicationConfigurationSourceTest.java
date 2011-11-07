/**
 * Copyright (c) 2010 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.configured.source;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import org.junit.Test;

public class StickyApplicationConfigurationSourceTest {

  @Test(expected = ThereCanBeOnlyOneApplicationConfigurationException.class)
  public void thereCanBeOnlyOneConfiguration() {
    new StickyApplicationConfigurationSource() {

      protected ClassLoader getClassLoader() {
        return new ClassLoader() {

          @Override
          protected Enumeration<URL> findResources(String name) throws IOException {
            return Collections.enumeration(Arrays.asList(new URL[] { url(), url() }));
          }

          private URL url() throws MalformedURLException {
            return new URL("http://www.stickycode.net/configured.html");
          }
        };
      }
    }.loadApplicationConfiguration();
  }

  @Test
  public void applicationConfigurationNotFoundWarnsOnly() {
    new StickyApplicationConfigurationSource() {

      @Override
      protected String getApplicationConfigurationPath() {
        return "no/such/path.properties";
      }
    }.loadApplicationConfiguration();
  }

  @Test
  public void hasValueLookupsAreCorrect() {
    assertThat(source().hasValue("application.name")).isTrue();
    assertThat(source().hasValue("no.such.property")).isFalse();
  }

  @Test
  public void getValuesFromSourceWorks() {
    assertThat(source().hasValue("application.name")).isTrue();
    assertThat(source().getValue("application.name")).isEqualTo("sticky");
  }

  private StickyApplicationConfigurationSource source() {
    StickyApplicationConfigurationSource stickyApplicationConfigurationSource = new StickyApplicationConfigurationSource();
    stickyApplicationConfigurationSource.loadApplicationConfiguration();
    return stickyApplicationConfigurationSource;
  }

}
