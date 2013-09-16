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
import org.onebusaway.vdv452.serialization.LineIdFieldMappingFactory;

@CsvFields(filename = "REC_LID.x10")
public class Line extends IdentityBean<LineId> {

  private static final long serialVersionUID = 1L;

  @CsvField(name = "LI_NR", mapping = LineIdFieldMappingFactory.class)
  private LineId id;

  @CsvField(name = "LI_KUERZEL")
  private String shortName;

  @CsvField(name = "LIDNAME", optional = true)
  private String longName;

  @Override
  public LineId getId() {
    return id;
  }

  @Override
  public void setId(LineId id) {
    this.id = id;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }
}
