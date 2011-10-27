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

import java.util.Set;

import javax.inject.Inject;

import net.stickycode.stereotype.StickyComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
public class ConfigurationSystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private Set<ConfigurationListener> listeners;

  public void configure() {
    log.info("starting configuration lifecycle");
    for (ConfigurationListener listener : listeners) {
      listener.resolve();
    }
    log.debug("resolved");

    for (ConfigurationListener listener : listeners) {
      listener.preConfigure();
    }
    log.debug("preconfigured");

    for (ConfigurationListener listener : listeners) {
      listener.configure();
    }
    log.debug("configured");

    for (ConfigurationListener listener : listeners) {
      listener.postConfigure();
    }
    log.debug("postconfigured");

    log.info("configured {}", this);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
