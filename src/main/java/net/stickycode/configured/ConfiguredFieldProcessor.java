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

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.configuration.ConfigurationTarget;
import net.stickycode.reflector.AnnotatedFieldProcessor;
import net.stickycode.stereotype.configured.Configured;
import net.stickycode.stereotype.configured.ConfiguredStrategy;

public class ConfiguredFieldProcessor
    extends AnnotatedFieldProcessor {

  private final ConfigurationRepository configuration;

  private ConfigurationTarget parent;

  public ConfiguredFieldProcessor(ConfigurationRepository configuration, ConfigurationTarget parent2) {
    super(Configured.class, ConfiguredStrategy.class);
    this.configuration = configuration;
    this.parent = parent2;
  }

  @Override
  public void processField(Object target, Field field) {
    if (field.getType().isPrimitive())
      throw new ConfiguredFieldsMustNotBePrimitiveAsDefaultDerivationIsImpossibleException(target, field);

    configuration.register(new ConfiguredField(parent, target, field, fieldTarget(field)));
  }

  private CoercionTarget fieldTarget(Field field) {
    if (parent.getCoercionTarget() == null)
      return CoercionTargets.find(field);

    return CoercionTargets.find(field, parent.getCoercionTarget());
  }

}
