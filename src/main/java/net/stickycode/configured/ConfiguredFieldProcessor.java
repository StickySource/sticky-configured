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
package net.stickycode.configured;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.reflector.AnnotatedFieldProcessor;
import net.stickycode.stereotype.Configured;
import net.stickycode.stereotype.ConfiguredStrategy;

public class ConfiguredFieldProcessor
    extends AnnotatedFieldProcessor {

  private Logger log = LoggerFactory.getLogger(getClass());

  private final ConfiguredConfiguration configuration;

  public ConfiguredFieldProcessor(ConfiguredConfiguration configuration) {
    super(Configured.class, ConfiguredStrategy.class);
    this.configuration = configuration;
  }

  @Override
  public void processField(Object target, Field field) {
    if (field.getType().isPrimitive())
      throw new ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossibleException(target, field);

    ConfiguredField configuredField = new ConfiguredField(target, field);
    configuration.addAttribute(configuredField);
  }
}
