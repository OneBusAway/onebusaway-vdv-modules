/**
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.vdv452.model;

public enum EStopType {
  STOP, DEPOT;

  public static EStopType parseFieldValue(String value) {
    if (value.equals("1")) {
      return STOP;
    } else if (value.equals("2")) {
      return DEPOT;
    } else {
      throw new IllegalArgumentException("unknown stop type value: " + value);
    }
  }
}
