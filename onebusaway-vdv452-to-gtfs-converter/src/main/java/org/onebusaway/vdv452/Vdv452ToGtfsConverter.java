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

import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsWriter;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.vdv452.model.Journey;
import org.onebusaway.vdv452.model.Line;
import org.onebusaway.vdv452.model.StopPoint;

public class Vdv452ToGtfsConverter {

  private File _inputPath;

  private File _outputPath;

  public void setInputPath(File inputPath) {
    _inputPath = inputPath;
  }

  public void setOutputPath(File outputPath) {
    _outputPath = outputPath;
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
    Vdv452ToGtfsFactory factory = new Vdv452ToGtfsFactory(in, out);
    for (StopPoint stop : in.getAllStopPoints()) {
      factory.getStopForStopPoint(stop);
    }
    for (Line line : in.getAllLines()) {
      factory.getRouteForLine(line);
    }
    for (Journey journey : in.getAllJourneys()) {
      factory.getTripForJourney(journey);
    }
  }  
}
