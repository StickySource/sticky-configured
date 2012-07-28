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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyRepository
@StickyFramework
public class InlineConfigurationRepository
    implements ConfigurationRepository {

  private Logger log = LoggerFactory.getLogger(getClass());

  private Map<Object, ConfiguredConfiguration> configurations = new HashMap<Object, ConfiguredConfiguration>();

  private ReentrantLock lock = new ReentrantLock();

  @Override
  public Iterator<Configuration> iterator() {
    return new LinkedList<Configuration>(configurations.values()).iterator();
  }

  @Override
  public void register(ConfigurationAttribute attribute) {
    assert attribute != null;
    log.info("registering {}", attribute);
    Configuration configuration = getConfiguration(attribute.getTarget());
    configuration.register(attribute);
  }

  private Configuration getConfiguration(Object target) {
    try {
      lock.lock();
      if (!configurations.containsKey(target))
        configurations.put(target, new ConfiguredConfiguration(target));

      return configurations.get(target);
    }
    finally {
      lock.unlock();
    }
  }

}
