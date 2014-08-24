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
package org.onebusaway.vdv452;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsWriter;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.vdv452.model.DayType;
import org.onebusaway.vdv452.model.Journey;
import org.onebusaway.vdv452.model.Line;
import org.onebusaway.vdv452.model.StopPoint;
import org.onebusaway.vdv452.model.TransportCompany;

public class Vdv452ToGtfsConverter {

  private File _inputPath;

  private File _outputPath;

  private TimeZone _tz = TimeZone.getTimeZone("Europe/Berlin");

  public void setInputPath(File inputPath) {
    _inputPath = inputPath;
  }

  public void setOutputPath(File outputPath) {
    _outputPath = outputPath;
  }

  public void setTimeZone(TimeZone tz) {
    _tz = tz;
  }
  
  public void run() throws IOException {
    Vdv452Reader reader = new Vdv452Reader();
    reader.setInputLocation(_inputPath);
    reader.run();

    Vdv452Dao in = reader.getDao();
    GtfsMutableRelationalDao out = new GtfsRelationalDaoImpl();
    convert(in, out);

    GtfsWriter writer = new GtfsWriter();
    writer.setOutputLocation(_outputPath);
    writer.run(out);
  }

  private void convert(Vdv452Dao in, GtfsMutableRelationalDao out) {
    Vdv452ToGtfsFactory factory = new Vdv452ToGtfsFactory(in, out, _tz);
    for (TransportCompany company : in.getAllTransportCompanies()) {
      factory.getAgencyForTransportCompany(company);
    }
    for (StopPoint stop : in.getAllStopPoints()) {
      factory.getStopForStopPoint(stop);
    }
    for (DayType dayType : in.getAllDayTypes()) {
      factory.createCalendarEntriesForDayType(dayType);
    }
    for (Line line : in.getAllLines()) {
      factory.getRouteForLine(line);
    }
    for (Journey journey : in.getAllJourneys()) {
      factory.getTripForJourney(journey);
    }
  }
}
