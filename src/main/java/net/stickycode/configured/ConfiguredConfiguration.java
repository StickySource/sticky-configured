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

import static net.stickycode.exception.Preconditions.notBlank;
import static net.stickycode.exception.Preconditions.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.stickycode.reflector.Reflector;
import net.stickycode.stereotype.PostConfigured;
import net.stickycode.stereotype.PreConfigured;

public class ConfiguredConfiguration
    implements Configuration {

  private final Object target;
  private final String name;

  private final List<ConfigurationAttribute> attributes = new ArrayList<ConfigurationAttribute>();

  public ConfiguredConfiguration(Object instance, String name) {
    this.target = notNull(instance, "The target object of configuration should not be null");
    this.name = notBlank(name, "The name of a ConfiguredConfiguration cannot be blank");
  }

  @Override
  public Iterator<ConfigurationAttribute> iterator() {
    return Collections.unmodifiableList(attributes).iterator();
  }

  @Override
  public void preConfigure() {
    new Reflector()
        .forEachMethod(new InvokingAnnotatedMethodProcessor(PreConfigured.class))
        .process(target);
  }

  @Override
  public void postConfigure() {
    new Reflector()
        .forEachMethod(new InvokingAnnotatedMethodProcessor(PostConfigured.class))
        .process(target);
  }

  @Override
  public Class<?> getType() {
    return target.getClass();
  }

  public void addAttribute(ConfigurationAttribute attribute) {
    attributes.add(attribute);
  }

  @Override
  public String toString() {
    return String.format("%s with attributes %s", getName(), attributes);
  }

  public String getName() {
    return name;
  }

}
