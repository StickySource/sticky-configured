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

import java.net.URL;

import net.stickycode.exception.PermanentException;


@SuppressWarnings("serial")
public class ThereCanBeOnlyOneApplicationConfigurationException
    extends PermanentException {

  public ThereCanBeOnlyOneApplicationConfigurationException(URL url, URL conflict) {
    super("There can only one configuration but more were found {}, {}", url, conflict);
  }

}
