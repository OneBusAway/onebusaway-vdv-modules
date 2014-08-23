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

import org.onebusaway.csv_entities.schema.annotations.CsvField;
import org.onebusaway.csv_entities.schema.annotations.CsvFields;
import org.onebusaway.vdv452.serialization.EntityFieldMappingFactory;

/**
 * Describes a single entry in an ordered sequence of {@link StopPoint}
 * associated with a {@link Link}.
 */
@CsvFields(filename = "LID_VERLAUF.x10")
public class RouteSequence implements Comparable<RouteSequence> {

  @CsvField(name = "STR_LI_VAR", mapping = EntityFieldMappingFactory.class)
  private Line line;

  @CsvField(name = "LI_LFD_NR")
  private int sequence;

  @CsvField(name = "ORT_NR", mapping = EntityFieldMappingFactory.class)
  private StopPoint stop;

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public StopPoint getStop() {
    return stop;
  }

  public void setStop(StopPoint stop) {
    this.stop = stop;
  }

  @Override
  public int compareTo(RouteSequence o) {
    return this.sequence - o.sequence;
  }
  
  @Override
  public String toString() {
    return sequence + "=" + stop;
  }
}
