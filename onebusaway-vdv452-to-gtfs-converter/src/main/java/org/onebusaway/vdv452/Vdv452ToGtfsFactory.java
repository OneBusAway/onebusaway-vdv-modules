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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.onebusaway.collections.MappingLibrary;
import org.onebusaway.collections.tuple.Pair;
import org.onebusaway.collections.tuple.Tuples;
import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.Stop;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.model.calendar.ServiceDate;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.gtfs_transformer.updates.CalendarSimplicationLibrary;
import org.onebusaway.gtfs_transformer.updates.CalendarSimplicationLibrary.ServiceCalendarSummary;
import org.onebusaway.vdv452.model.*;

public class Vdv452ToGtfsFactory {

  private final CalendarSimplicationLibrary _calendarLibrary = new CalendarSimplicationLibrary();

  private final Vdv452Dao _in;

  private final GtfsMutableRelationalDao _out;

  private final TimeZone _tz;

  /**
   * The set of service ids for calendar entries that have already been
   * processed.
   */
  private final Set<AgencyAndId> processedCalendars = new HashSet<AgencyAndId>();

  public Vdv452ToGtfsFactory(Vdv452Dao in, GtfsMutableRelationalDao out,
      TimeZone tz) {
    _in = in;
    _out = out;
    _tz = tz;
  }

  public Trip getTripForJourney(Journey journey) {

    VersionedId journeyId = journey.getId();
    AgencyAndId id = new AgencyAndId("1", Long.toString(journeyId.getId()));
    Trip trip = _out.getTripForId(id);
    if (trip == null) {
      trip = new Trip();
      trip.setId(id);
      trip.setRoute(getRouteForLine(journey.getLine()));
      trip.setServiceId(createCalendarEntriesForDayType(journey.getDayType()));
      getStopTimesForJourney(journey, trip);
      _out.saveEntity(trip);
    }
    return trip;
  }

  public Route getRouteForLine(Line line) {
    LineId lineId = line.getId();
    Agency agency = getAgencyForLine(line);
    AgencyAndId id = new AgencyAndId(agency.getId(),
        Long.toString(lineId.getLineId()));
    Route route = _out.getRouteForId(id);
    if (route == null) {
      route = new Route();
      route.setId(id);
      route.setAgency(agency);
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

  private Agency getAgencyForLine(Line line) {
    // Right now, I'm not actually clear on how a Line is linked to a
    // TransportCompany. So for now, if there is more than one, we bail.
    Collection<TransportCompany> companies = _in.getAllTransportCompanies();
    if (companies.size() != 1) {
      throw new IllegalStateException(
          "If you are reading this, it means you have a VDV 452 feed without "
              + "exactly one entry in ZUL_VERKEHRSBETRIEB.x10.  I haven't yet "
              + "implemented support for this scenario but would like to.  Reach "
              + "out to bdferris@google.com and let's see if we can get this fixed.");
    }
    TransportCompany company = companies.iterator().next();
    return getAgencyForTransportCompany(company);
  }

  public Agency getAgencyForTransportCompany(TransportCompany company) {
    String agencyId = Long.toString(company.getId().getId());
    Agency agency = _out.getAgencyForId(agencyId);
    if (agency == null) {
      agency = new Agency();
      agency.setId(agencyId);
      agency.setName(company.getName());
      agency.setTimezone(_tz.getID());
      agency.setUrl("https://github.com/OneBusAway/onebusaway-vdv-modules");
      agency.setLang("de");
      _out.saveEntity(agency);
    }
    return agency;
  }

  public AgencyAndId createCalendarEntriesForDayType(DayType dayType) {
    AgencyAndId serviceId = getServiceIdForDayType(dayType);
    if (!processedCalendars.add(serviceId)) {
      return serviceId;
    }
    Set<ServiceDate> serviceDates = new HashSet<ServiceDate>();
    for (Period period : _in.getPeriodsForDayType(dayType)) {
      // Convert the VDV ServiceDate to a GTFS ServiceDate
      serviceDates.add(new ServiceDate(period.getDate().getAsCalendar(_tz)));
    }
    ServiceCalendarSummary summary = _calendarLibrary.getSummaryForServiceDates(serviceDates);
    List<Object> newEntities = new ArrayList<Object>();
    _calendarLibrary.computeSimplifiedCalendar(serviceId, summary, newEntities);
    for (Object entity : newEntities) {
      _out.saveOrUpdateEntity(entity);
    }
    return serviceId;
  }

  private AgencyAndId getServiceIdForDayType(DayType dayType) {
    return new AgencyAndId("1", Long.toString(dayType.getId().getId()));
  }

  public Stop getStopForStopPoint(StopPoint stopPoint) {
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
      if (vdvStop.getId().getType() == EStopType.SKIP) {
        return null;
      }
      gtfsStop.setName(vdvStop.getName());
      gtfsStop.setLat(vdvStop.getLat());
      gtfsStop.setLon(vdvStop.getLng());
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
      Stop stopForStopPoint = getStopForStopPoint(entry.getStop());
      if (stopForStopPoint == null) {
        return;
      }
      stopTime.setStop(stopForStopPoint);
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
      List<RouteSequence> sequence,
      Map<Pair<StopPoint>, TravelTime> travelTimesByStopPair) {
    List<TravelTime> ordered = new ArrayList<TravelTime>(sequence.size() - 1);
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
