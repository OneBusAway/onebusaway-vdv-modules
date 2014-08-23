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

@CsvFields(filename = "SEL_FZT_FELD.x10")
public class TravelTime {

  @CsvField(name = "FGR_NR", mapping = EntityFieldMappingFactory.class)
  private TimingGroup timingGroup;

  @CsvField(name = "ORT_NR", mapping = EntityFieldMappingFactory.class)
  private StopPoint fromStop;

  @CsvField(name = "SEL_ZIEL", mapping = EntityFieldMappingFactory.class)
  private StopPoint toStop;

  @CsvField(name = "SEL_FZT")
  private int travelTime;

  public TimingGroup getTimingGroup() {
    return timingGroup;
  }

  public void setTimingGroup(TimingGroup timingGroup) {
    this.timingGroup = timingGroup;
  }

  public StopPoint getFromStop() {
    return fromStop;
  }

  public void setFromStop(StopPoint fromStop) {
    this.fromStop = fromStop;
  }

  public StopPoint getToStop() {
    return toStop;
  }

  public void setToStop(StopPoint toStop) {
    this.toStop = toStop;
  }

  public int getTravelTime() {
    return travelTime;
  }

  public void setTravelTime(int travelTime) {
    this.travelTime = travelTime;
  }
  
  @Override
  public String toString() {
    return timingGroup + ": " + fromStop + " + " + toStop + " = " + travelTime;
  }
}
