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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyRepository
@StickyFramework
public class InlineConfigurationRepository
    implements ConfigurationRepository {

  private Logger log = LoggerFactory.getLogger(getClass());

  private List<Configuration> configurations = new ArrayList<Configuration>();

  @Override
  public Iterator<Configuration> iterator() {
    return Collections.unmodifiableList(configurations).iterator();
  }

  @Override
  public void register(Configuration configuration) {
    assert configuration != null;
    log.info("registering {}", configuration);
    configurations.add(configuration);
  }

}
