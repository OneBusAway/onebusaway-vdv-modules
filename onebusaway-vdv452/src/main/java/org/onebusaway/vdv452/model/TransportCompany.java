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

@CsvFields(filename = "ZUL_VERKEHRSBETRIEB.x10")
public class TransportCompany extends IdentityBean<VersionedId> {

  private static final long serialVersionUID = 1L;

  @CsvField(name = "UNTERNEHMEN", mapping = VersionedIdFieldMappingFactory.class)
  private VersionedId id;

  @CsvField(name = "ABK_UNTERNEHMEN")
  private String name;

  @CsvField(name = "BETRIEBSGEBIET_BEZ", optional = true)
  private String operatingArea;

  @Override
  public VersionedId getId() {
    return id;
  }

  @Override
  public void setId(VersionedId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOperatingArea() {
    return operatingArea;
  }

  public void setOperatingArea(String operatingArea) {
    this.operatingArea = operatingArea;
  }
  
  @Override
  public String toString() {
    return id.toString();
  }
}
