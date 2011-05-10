/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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
package net.stickycode.configured;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ConfiguredConfiguration
    implements Configuration {

  private final Object target;
  private final List<ConfigurationAttribute> attributes = new ArrayList<ConfigurationAttribute>();

  public ConfiguredConfiguration(Object instance) {
    this.target = instance;
  }

  @Override
  public Iterator<ConfigurationAttribute> iterator() {
    return Collections.unmodifiableList(attributes).iterator();
  }

  @Override
  public void preConfigure() {
  }

  @Override
  public void postConfigure() {
  }

  @Override
  public Class<?> getType() {
    return target.getClass();
  }

  @Override
  public void addAttribute(ConfigurationAttribute attribute) {
    attributes.add(attribute);
  }

  @Override
  public boolean hasTarget(Object target) {
    // only want to configure the instance that was registered
    return this.target == target;
  }

  @Override
  public String toString() {
    return String.format("ConfigurationConfiguration(%s,%s)", getName(), attributes);
  }

  public String getName() {
    return Introspector.decapitalize(getType().getSimpleName());
  }

}
