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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.onebusaway.csv_entities.CsvEntityContext;
import org.onebusaway.csv_entities.CsvEntityReader;
import org.onebusaway.csv_entities.CsvInputSource;
import org.onebusaway.csv_entities.EntityHandler;
import org.onebusaway.csv_entities.IndividualCsvEntityReader;
import org.onebusaway.csv_entities.exceptions.CsvEntityIOException;
import org.onebusaway.csv_entities.schema.EntitySchema;
import org.onebusaway.vdv452.model.BaseVersion;
import org.onebusaway.vdv452.model.Block;
import org.onebusaway.vdv452.model.DayType;
import org.onebusaway.vdv452.model.Journey;
import org.onebusaway.vdv452.model.JourneyWaitTime;
import org.onebusaway.vdv452.model.Line;
import org.onebusaway.vdv452.model.Period;
import org.onebusaway.vdv452.model.RouteSequence;
import org.onebusaway.vdv452.model.Stop;
import org.onebusaway.vdv452.model.StopPoint;
import org.onebusaway.vdv452.model.TimingGroup;
import org.onebusaway.vdv452.model.TransportCompany;
import org.onebusaway.vdv452.model.TravelTime;
import org.onebusaway.vdv452.model.VehicleType;
import org.onebusaway.vdv452.model.WaitTime;
import org.onebusaway.vdv452.serialization.IndividaulVdvEntityReader;
import org.onebusaway.vdv452.serialization.VdvTokenizerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vdv452Reader extends CsvEntityReader {

  public static final String DAO_CONTEXT_KEY = Vdv452Reader.class + ".daoKey";

  private static Logger _log = LoggerFactory.getLogger(Vdv452Reader.class);

  private List<Class<?>> _entityClasses = new ArrayList<Class<?>>();

  private Vdv452Dao _dao = new Vdv452Dao();

  public Vdv452Reader() {
    setTokenizerStrategy(new VdvTokenizerStrategy());
    setTrimValues(true);

    _entityClasses.add(BaseVersion.class);
    _entityClasses.add(TransportCompany.class);
    _entityClasses.add(StopPoint.class);
    _entityClasses.add(Stop.class);
    _entityClasses.add(DayType.class);
    _entityClasses.add(Period.class);
    _entityClasses.add(TimingGroup.class);
    _entityClasses.add(TravelTime.class);    
    _entityClasses.add(WaitTime.class);
    _entityClasses.add(Line.class);
    _entityClasses.add(RouteSequence.class);
    _entityClasses.add(VehicleType.class);
    _entityClasses.add(Block.class);
    _entityClasses.add(Journey.class);
    _entityClasses.add(JourneyWaitTime.class);

    addEntityHandler(new EntityHandlerImpl());

    CsvEntityContext ctx = getContext();
    ctx.put(DAO_CONTEXT_KEY, _dao);
  }

  public List<Class<?>> getEntityClasses() {
    return _entityClasses;
  }

  public void setEntityClasses(List<Class<?>> entityClasses) {
    _entityClasses = entityClasses;
  }

  public Vdv452Dao getDao() {
    return _dao;
  }

  public void run() throws IOException {
    run(getInputSource());
  }

  public void run(CsvInputSource source) throws IOException {
    List<Class<?>> classes = getEntityClasses();
    for (Class<?> entityClass : classes) {
      _log.info("reading entities: " + entityClass.getName());
      readEntities(entityClass, source);
    }
  }

  @Override
  public void readEntities(Class<?> entityClass, InputStream is)
      throws IOException, CsvEntityIOException {
    readEntities(entityClass, new InputStreamReader(is, "ISO-8859-1"));
  }

  @Override
  protected IndividualCsvEntityReader createIndividualCsvEntityReader(
      CsvEntityContext context, EntitySchema schema, EntityHandler handler) {
    return new IndividaulVdvEntityReader(context, schema, handler);
  }

  private class EntityHandlerImpl implements EntityHandler {
    @Override
    public void handleEntity(Object bean) {
      _dao.putEntity(bean);
    }
  }
}
