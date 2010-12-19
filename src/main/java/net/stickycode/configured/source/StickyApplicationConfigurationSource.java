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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.stickycode.configured.ConfigurationSource;

public class StickyApplicationConfigurationSource
    implements ConfigurationSource {

  private Map<String, String> map = new HashMap<String, String>();

  public StickyApplicationConfigurationSource() {
    super();
    loadApplicationConfiguration();
  }

  @Override
  public boolean hasValue(String key) {
    return map.containsKey(key);
  }

  @Override
  public String getValue(String key) {
    return map.get(key);
  }

  private void loadApplicationConfiguration() {
    Enumeration<URL> urls = findUrls();
    if (!urls.hasMoreElements())
      throw new ApplicationConfigurationNotFoundException(getApplicationConfigurationPath());

    URL url = urls.nextElement();
    if (urls.hasMoreElements())
      throw new ThereCanBeOnlyOneApplicationConfigurationException(url, urls.nextElement());

    Properties p = load(url);
    for (String key : p.stringPropertyNames()) {
      map.put(key, p.getProperty(key));
    }
  }

  protected String getApplicationConfigurationPath() {
    return "META-INF/sticky/application.properties";
  }

  protected Properties load(URL url) {
    try {
      InputStream i = url.openStream();
      Properties p = new Properties();
      p.load(i);
      i.close();
      return p;
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Enumeration<URL> findUrls() {
    try {
      return getClassLoader().getResources(getApplicationConfigurationPath());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected ClassLoader getClassLoader() {
    return getClass().getClassLoader();
  }


}
