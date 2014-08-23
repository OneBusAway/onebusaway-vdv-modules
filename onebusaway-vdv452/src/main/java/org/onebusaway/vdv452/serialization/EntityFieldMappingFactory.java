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
package org.onebusaway.vdv452.serialization;

import java.util.Map;

import org.onebusaway.csv_entities.CsvEntityContext;
import org.onebusaway.csv_entities.exceptions.CsvEntityException;
import org.onebusaway.csv_entities.schema.AbstractFieldMapping;
import org.onebusaway.csv_entities.schema.BeanWrapper;
import org.onebusaway.csv_entities.schema.EntitySchemaFactory;
import org.onebusaway.csv_entities.schema.FieldMapping;
import org.onebusaway.csv_entities.schema.FieldMappingFactory;
import org.onebusaway.vdv452.Vdv452Dao;
import org.onebusaway.vdv452.Vdv452Reader;
import org.onebusaway.vdv452.model.DayType;
import org.onebusaway.vdv452.model.Journey;
import org.onebusaway.vdv452.model.Line;
import org.onebusaway.vdv452.model.LineId;
import org.onebusaway.vdv452.model.StopId;
import org.onebusaway.vdv452.model.StopPoint;
import org.onebusaway.vdv452.model.TimingGroup;
import org.onebusaway.vdv452.model.VehicleType;
import org.onebusaway.vdv452.model.VersionedId;

public class EntityFieldMappingFactory implements FieldMappingFactory {

  @Override
  public FieldMapping createFieldMapping(EntitySchemaFactory schemaFactory,
      Class<?> entityType, String csvFieldName, String objFieldName,
      Class<?> objFieldType, boolean required) {
    return new Impl(entityType, csvFieldName, objFieldName, objFieldType,
        required);
  }

  private static class Impl extends AbstractFieldMapping {

    private final Class<?> _objFieldType;

    public Impl(Class<?> entityType, String csvFieldName, String objFieldName,
        Class<?> objFieldType, boolean required) {
      super(entityType, csvFieldName, objFieldName, required);
      _objFieldType = objFieldType;
    }

    @Override
    public void translateFromCSVToObject(CsvEntityContext context,
        Map<String, Object> csvValues, BeanWrapper object)
        throws CsvEntityException {
      if (isMissingAndOptional(csvValues)) {
        return;
      }
      Object entity = resolveEntity(context, csvValues);
      object.setPropertyValue(_objFieldName, entity);
    }

    @Override
    public void translateFromObjectToCSV(CsvEntityContext context,
        BeanWrapper object, Map<String, Object> csvValues)
        throws CsvEntityException {
      throw new UnsupportedOperationException();
    }

    private Object resolveEntity(CsvEntityContext context,
        Map<String, Object> csvValues) {
      Vdv452Dao dao = (Vdv452Dao) context.get(Vdv452Reader.DAO_CONTEXT_KEY);
      if (_objFieldType == DayType.class) {
        VersionedId id = IdFactory.resolveVersionedId(csvValues, _csvFieldName);
        return dao.getDayTypeForId(id);
      } else if (_objFieldType == TimingGroup.class) {
        VersionedId id = IdFactory.resolveVersionedId(csvValues, _csvFieldName);
        return dao.getTimingGroupForId(id);
      } else if (_objFieldType == VehicleType.class) {
        VersionedId id = IdFactory.resolveVersionedId(csvValues, _csvFieldName);
        return dao.getVehicleTypeForId(id);
      } else if (_objFieldType == StopPoint.class) {
        StopId stopId = IdFactory.resolveStopId(csvValues, _csvFieldName);
        return dao.getStopPointForId(stopId);
      } else if (_objFieldType == Line.class) {
        LineId lineId = IdFactory.resolveLineId(csvValues, _csvFieldName);
        return dao.getLineForId(lineId);
      } else if (_objFieldType == Journey.class) {
        VersionedId id = IdFactory.resolveVersionedId(csvValues, _csvFieldName);
        return dao.getJourneyForId(id);
      }
      throw new IllegalStateException("unsupported entity type: "
          + _objFieldType);
    }
  }
}
