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
import org.onebusaway.vdv452.serialization.VersionedIdFieldMappingFactory;

@CsvFields(filename = "MENGE_TAGESART.x10")
public class DayType extends IdentityBean<VersionedId> {

  private static final long serialVersionUID = 1L;

  @CsvField(name = "TAGESART_NR", mapping = VersionedIdFieldMappingFactory.class)
  private VersionedId id;

  @CsvField(name = "TAGESART_TEXT", optional = true)
  private String desc;

  @Override
  public VersionedId getId() {
    return id;
  }

  @Override
  public void setId(VersionedId id) {
    this.id = id;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
