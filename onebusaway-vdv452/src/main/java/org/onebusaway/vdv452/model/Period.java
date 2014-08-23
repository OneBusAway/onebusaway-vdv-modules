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
import org.onebusaway.vdv452.serialization.ServiceDateFieldMappingFactory;

@CsvFields(filename = "FIRMENKALENDER.x10")
public class Period {

  @CsvField(name = "BETRIEBSTAG", mapping = ServiceDateFieldMappingFactory.class)
  private ServiceDate date;

  @CsvField(name = "TAGESART_NR", mapping = EntityFieldMappingFactory.class)
  private DayType dayType;

  @CsvField(name = "BETRIEBSTAG_TEXT", optional = true)
  private String desc;

  public ServiceDate getDate() {
    return date;
  }

  public void setDate(ServiceDate date) {
    this.date = date;
  }

  public DayType getDayType() {
    return dayType;
  }

  public void setDayType(DayType dayType) {
    this.dayType = dayType;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
  
  @Override
  public String toString() {
    return date.toString();
  }
}
