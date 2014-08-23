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
import org.onebusaway.vdv452.serialization.VersionedIdFieldMappingFactory;

@CsvFields(filename = "REC_FRT.x10")
public class Journey extends IdentityBean<VersionedId> {

  private static final long serialVersionUID = 1L;

  @CsvField(name = "FRT_FID", mapping = VersionedIdFieldMappingFactory.class)
  private VersionedId id;

  @CsvField(name = "LI_NR", mapping = EntityFieldMappingFactory.class)
  private Line line;

  @CsvField(name = "FRT_START")
  private int departureTime;

  @CsvField(name = "FGR_NR", mapping = EntityFieldMappingFactory.class)
  private TimingGroup timingGroup;
  
  @CsvField(name = "TAGESART_NR", mapping = EntityFieldMappingFactory.class)
  private DayType dayType;

  @Override
  public VersionedId getId() {
    return id;
  }

  @Override
  public void setId(VersionedId id) {
    this.id = id;
  }

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }

  public int getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(int departureTime) {
    this.departureTime = departureTime;
  }

  public TimingGroup getTimingGroup() {
    return timingGroup;
  }

  public void setTimingGroup(TimingGroup timingGroup) {
    this.timingGroup = timingGroup;
  }

  public DayType getDayType() {
    return dayType;
  }

  public void setDayType(DayType dayType) {
    this.dayType = dayType;
  }
}
