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

@CsvFields(filename = "REC_FRT_HZT.x10")
public class JourneyWaitTime {

  @CsvField(name = "FRT_FID", mapping = EntityFieldMappingFactory.class)
  private Journey journey;

  @CsvField(name = "ORT_NR", mapping = EntityFieldMappingFactory.class)
  private StopPoint stop;

  @CsvField(name = "FRT_HZT_ZEIT")
  private int waitTime;

  public Journey getJourney() {
    return journey;
  }

  public void setJourney(Journey journey) {
    this.journey = journey;
  }

  public StopPoint getStop() {
    return stop;
  }

  public void setStop(StopPoint stop) {
    this.stop = stop;
  }

  public int getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(int waitTime) {
    this.waitTime = waitTime;
  }
}
