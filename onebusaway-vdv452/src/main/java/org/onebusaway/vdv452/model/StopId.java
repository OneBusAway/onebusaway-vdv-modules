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

import java.io.Serializable;

public class StopId implements Serializable {

  private static final long serialVersionUID = 1L;

  private long version;

  private EStopType type;

  private long id;

  public StopId() {

  }

  public StopId(long version, EStopType type, long id) {
    this.version = version;
    this.type = type;
    this.id = id;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public EStopType getType() {
    return type;
  }

  public void setType(EStopType type) {
    this.type = type;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + (int) (version ^ (version >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StopId other = (StopId) obj;
    if (id != other.id)
      return false;
    if (type != other.type)
      return false;
    if (version != other.version)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return version + ":" + type + ":" + id;
  }
}
