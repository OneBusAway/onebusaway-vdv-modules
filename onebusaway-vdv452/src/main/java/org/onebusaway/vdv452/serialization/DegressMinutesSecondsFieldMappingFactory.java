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

import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.onebusaway.csv_entities.CsvEntityContext;
import org.onebusaway.csv_entities.exceptions.InvalidValueEntityException;
import org.onebusaway.csv_entities.schema.AbstractFieldMapping;
import org.onebusaway.csv_entities.schema.BeanWrapper;
import org.onebusaway.csv_entities.schema.EntitySchemaFactory;
import org.onebusaway.csv_entities.schema.FieldMapping;
import org.onebusaway.csv_entities.schema.FieldMappingFactory;
import org.onebusaway.vdv452.model.ServiceDate;

public class DegressMinutesSecondsFieldMappingFactory implements FieldMappingFactory {

  @Override
public FieldMapping createFieldMapping(final EntitySchemaFactory schemaFactory,
      final Class<?> entityType, final String csvFieldName, final String objFieldName,
      final Class<?> objFieldType, final boolean required) {
    return new Impl(entityType, csvFieldName, objFieldName, required);
  }

  private static class Impl extends AbstractFieldMapping implements Converter {

    private static final Pattern FORMAT = Pattern.compile("^(-{0,1}\\d{2,3})(\\d{2})(\\d{2})(\\d{3})$");

    public Impl(final Class<?> entityType, final String csvFieldName, final String objFieldName,
        final boolean required) {
      super(entityType, csvFieldName, objFieldName, required);
    }

    @Override
	public void translateFromCSVToObject(final CsvEntityContext context,
        final Map<String, Object> csvValues, final BeanWrapper object) {

      if (isMissingAndOptional(csvValues)) {
		return;
	}

      final String value = csvValues.get(_csvFieldName).toString();
      final Matcher matcher = FORMAT.matcher(value);
      if (!matcher.matches()) {
        throw new ConversionException("Could not convert " + value + " to decimal degrees");
      }
      final int degrees = Integer.parseInt(matcher.group(1));
      final int minutes = Integer.parseInt(matcher.group(2));
      final double seconds = Integer.parseInt(matcher.group(3))
          + Integer.parseInt(matcher.group(4)) / 1000.0d;
      final double decimalDegrees = degrees + (minutes / 60.0) + (seconds / 3600);
      object.setPropertyValue(_objFieldName, decimalDegrees);
    }

    @Override
	public void translateFromObjectToCSV(final CsvEntityContext context,
        final BeanWrapper object, final Map<String, Object> csvValues) {

      final ServiceDate date = (ServiceDate) object.getPropertyValue(_objFieldName);
      final String value = date.getAsString();
      csvValues.put(_csvFieldName, value);
    }

    @Override
    public Object convert(@SuppressWarnings("rawtypes") final
    Class type, final Object value) {
      if (type == ServiceDate.class) {
        try {
          return ServiceDate.parseString(value.toString());
        } catch (final ParseException ex) {
          throw new InvalidValueEntityException(_entityType, _csvFieldName,
              value.toString());
        }
      }
      throw new ConversionException("Could not convert " + value + " of type "
          + value.getClass() + " to " + type);
    }
  }

}