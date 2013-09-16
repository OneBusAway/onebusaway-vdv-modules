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

public class LineId implements Serializable {

  private static final long serialVersionUID = 1L;

  private long version;

  private long lineId;

  private String lineVariant;

  public LineId() {

  }

  public LineId(long version, long lineId, String lineVariant) {
    this.version = version;
    this.lineId = lineId;
    this.lineVariant = lineVariant;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public long getLineId() {
    return lineId;
  }

  public void setLineId(long lineId) {
    this.lineId = lineId;
  }

  public String getLineVariant() {
    return lineVariant;
  }

  public void setLineVariant(String lineVariant) {
    this.lineVariant = lineVariant;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (lineId ^ (lineId >>> 32));
    result = prime * result
        + ((lineVariant == null) ? 0 : lineVariant.hashCode());
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
    LineId other = (LineId) obj;
    if (lineId != other.lineId)
      return false;
    if (lineVariant == null) {
      if (other.lineVariant != null)
        return false;
    } else if (!lineVariant.equals(other.lineVariant))
      return false;
    if (version != other.version)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return version + ":" + lineId + ":" + lineVariant;
  }
}
