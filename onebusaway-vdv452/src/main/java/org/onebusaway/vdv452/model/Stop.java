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
import org.onebusaway.vdv452.serialization.StopIdFieldMappingFactory;

@CsvFields(filename = "REC_ORT.x10")
public class Stop extends IdentityBean<StopId> {

  private static final long serialVersionUID = 1L;

  @CsvField(name = "ORT_NR", mapping = StopIdFieldMappingFactory.class)
  private StopId id;

  @CsvField(name = "ORT_NAME")
  private String name;

  @CsvField(name = "ORT_POS_BREITE")
  private long lat;

  @CsvField(name = "ORT_POS_LAENGE")
  private long lng;

  @Override
  public StopId getId() {
    return id;
  }

  @Override
  public void setId(StopId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getLat() {
    return lat;
  }

  public void setLat(long lat) {
    this.lat = lat;
  }

  public long getLng() {
    return lng;
  }

  public void setLng(long lng) {
    this.lng = lng;
  }
}
