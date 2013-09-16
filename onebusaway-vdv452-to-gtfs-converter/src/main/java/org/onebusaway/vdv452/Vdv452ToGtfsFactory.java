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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onebusaway.collections.MappingLibrary;
import org.onebusaway.collections.tuple.Pair;
import org.onebusaway.collections.tuple.Tuples;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.Stop;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.vdv452.model.Journey;
import org.onebusaway.vdv452.model.Line;
import org.onebusaway.vdv452.model.LineId;
import org.onebusaway.vdv452.model.RouteSequence;
import org.onebusaway.vdv452.model.StopId;
import org.onebusaway.vdv452.model.StopPoint;
import org.onebusaway.vdv452.model.TravelTime;
import org.onebusaway.vdv452.model.VersionedId;
import org.onebusaway.vdv452.model.WaitTime;

public class Vdv452ToGtfsFactory {

  private final Vdv452Dao _in;

  private final GtfsMutableRelationalDao _out;

  public Vdv452ToGtfsFactory(Vdv452Dao in, GtfsMutableRelationalDao out) {
    _in = in;
    _out = out;
  }

  public Trip getTripForJourney(Journey journey) {
    VersionedId journeyId = journey.getId();
    AgencyAndId id = new AgencyAndId("1", Long.toString(journeyId.getId()));
    Trip trip = _out.getTripForId(id);
    if (trip == null) {
      trip = new Trip();
      trip.setId(id);
      trip.setRoute(getRouteForLine(journey.getLine()));
      trip.setServiceId(new AgencyAndId("1", "1"));
      getStopTimesForJourney(journey, trip);
      _out.saveEntity(trip);
    }
    return trip;
  }

  private Route getRouteForLine(Line line) {
    LineId lineId = line.getId();
    AgencyAndId id = new AgencyAndId("1", Long.toString(lineId.getLineId()));
    Route route = _out.getRouteForId(id);
    if (route == null) {
      route = new Route();
      route.setId(id);
      if (line.getShortName() != null && !line.getShortName().isEmpty()) {
        route.setShortName(line.getShortName());
      }
      if (line.getLongName() != null && !line.getLongName().isEmpty()) {
        route.setLongName(line.getLongName());
      }
      route.setType(3);
      _out.saveEntity(route);
    }
    return route;
  }

  private Stop getStopForStopPoint(StopPoint stopPoint) {
    StopId stopId = stopPoint.getId();
    AgencyAndId id = new AgencyAndId("1", Long.toString(stopId.getId()));
    Stop gtfsStop = _out.getStopForId(id);
    if (gtfsStop == null) {
      gtfsStop = new Stop();
      gtfsStop.setId(id);
      org.onebusaway.vdv452.model.Stop vdvStop = _in.getStopForId(stopId);
      if (vdvStop == null) {
        throw new IllegalStateException("unknown stop: " + stopId);
      }
      gtfsStop.setName(vdvStop.getName());
      gtfsStop.setLat(vdvStop.getLat() / 10000000.0);
      gtfsStop.setLon(vdvStop.getLng() / 10000000.0);
      if (vdvStop.getLat() == 0) {
        double lat = 53.5535290 + Math.random() * 0.001;
        double lng = 10.0061630 + Math.random() * 0.001;
        gtfsStop.setLat(lat);
        gtfsStop.setLon(lng);
      }
      _out.saveEntity(gtfsStop);
    }
    return gtfsStop;
  }

  private void getStopTimesForJourney(Journey journey, Trip trip) {
    List<RouteSequence> sequence = _in.getRouteSequenceForLine(journey.getLine());
    List<TravelTime> travelTimes = orderTravelTimesForRouteSequence(sequence,
        _in.getTravelTimesForTimingGroup(journey.getTimingGroup()));
    List<WaitTime> waitTimes = orderWaitTimesForRouteSequence(sequence,
        _in.getWaitTimesForTimingGroup(journey.getTimingGroup()));
    int currentTime = journey.getDepartureTime();
    for (int i = 0; i < sequence.size(); ++i) {
      RouteSequence entry = sequence.get(i);
      StopTime stopTime = new StopTime();
      stopTime.setTrip(trip);
      stopTime.setStop(getStopForStopPoint(entry.getStop()));
      stopTime.setStopSequence(i);
      stopTime.setArrivalTime(currentTime);
      WaitTime waitTime = waitTimes.get(i);
      if (waitTime != null) {
        currentTime += waitTime.getWaitTime();
      }
      stopTime.setDepartureTime(currentTime);
      if (i + 1 < sequence.size()) {
        currentTime += travelTimes.get(i).getTravelTime();
      }
      _out.saveEntity(stopTime);
    }
  }

  private List<TravelTime> orderTravelTimesForRouteSequence(
      List<RouteSequence> sequence, List<TravelTime> travelTimes) {
    if (sequence.size() != travelTimes.size() + 1) {
      throw new IllegalStateException();
    }
    Map<Pair<StopPoint>, TravelTime> travelTimesByStopPair = new HashMap<Pair<StopPoint>, TravelTime>();
    for (TravelTime travelTime : travelTimes) {
      Pair<StopPoint> pair = Tuples.pair(travelTime.getFromStop(),
          travelTime.getToStop());
      TravelTime existing = travelTimesByStopPair.put(pair, travelTime);
      if (existing != null) {
        throw new IllegalStateException();
      }
    }
    List<TravelTime> ordered = new ArrayList<TravelTime>(travelTimes.size());
    for (int i = 0; i + 1 < sequence.size(); ++i) {
      RouteSequence from = sequence.get(i);
      RouteSequence to = sequence.get(i + 1);
      Pair<StopPoint> pair = Tuples.pair(from.getStop(), to.getStop());
      TravelTime travelTime = travelTimesByStopPair.get(pair);
      if (travelTime == null) {
        throw new IllegalStateException();
      }
      ordered.add(travelTime);
    }
    return ordered;
  }

  private List<WaitTime> orderWaitTimesForRouteSequence(
      List<RouteSequence> sequence, List<WaitTime> waitTimes) {
    Map<StopPoint, WaitTime> waitTimesByStopPoint = MappingLibrary.mapToValue(
        waitTimes, "stop");
    List<WaitTime> ordered = new ArrayList<WaitTime>(waitTimes.size());
    for (int i = 0; i < sequence.size(); ++i) {
      RouteSequence entry = sequence.get(i);
      WaitTime waitTime = waitTimesByStopPoint.get(entry.getStop());
      ordered.add(waitTime);
    }
    return ordered;
  }

}
