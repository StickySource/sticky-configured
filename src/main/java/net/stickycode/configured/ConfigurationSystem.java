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

import net.stickycode.bootstrap.AbstractStickySystem;
import net.stickycode.stereotype.configured.Configured;
import net.stickycode.stereotype.plugin.StickyExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyExtension
public class ConfigurationSystem
    extends AbstractStickySystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  private Set<ConfigurationListener> listeners;

  public void start() {
    log.info("configuration starting");

    log.debug("resolving");
    for (ConfigurationListener listener : listeners) {
      listener.resolve();
    }

    log.debug("preconfiguring");
    for (ConfigurationListener listener : listeners) {
      listener.preConfigure();
    }

    log.debug("configuring");
    for (ConfigurationListener listener : listeners) {
      listener.configure();
    }

    log.debug("postconfiguring");
    for (ConfigurationListener listener : listeners) {
      listener.postConfigure();
    }

    log.info("configuration complete");
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  public String getLabel() {
    return Configured.class.getSimpleName();
  }

  @Override
  public Package getPackage() {
    return getClass().getPackage();
  }

}
