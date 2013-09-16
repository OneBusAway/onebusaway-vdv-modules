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

@CsvFields(filename = "REC_UMLAUF.x10")
public class Block {

  @CsvField(name = "ANF_ORT", mapping = EntityFieldMappingFactory.class)
  private StopPoint startPoint;

  @CsvField(name = "END_ORT", mapping = EntityFieldMappingFactory.class)
  private StopPoint endPoint;

  @CsvField(name = "FZG_TYP_NR", mapping = EntityFieldMappingFactory.class)
  private VehicleType vehicleType;

  public StopPoint getStartPoint() {
    return startPoint;
  }

  public void setStartPoint(StopPoint startPoint) {
    this.startPoint = startPoint;
  }

  public StopPoint getEndPoint() {
    return endPoint;
  }

  public void setEndPoint(StopPoint endPoint) {
    this.endPoint = endPoint;
  }

  public VehicleType getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }
}
