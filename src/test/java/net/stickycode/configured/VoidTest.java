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

import java.lang.reflect.Method;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class VoidTest {

  @Test
  public void voidit() throws SecurityException, NoSuchMethodException {
    Method m = getClass().getDeclaredMethod("voidit", new Class<?>[0]);
    assertThat(m.getReturnType()).isEqualTo(Void.TYPE);
  }
}
