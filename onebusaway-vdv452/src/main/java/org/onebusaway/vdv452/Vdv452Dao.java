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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onebusaway.collections.MappingLibrary;
import org.onebusaway.collections.tuple.Pair;
import org.onebusaway.collections.tuple.Tuples;
import org.onebusaway.vdv452.model.DayType;
import org.onebusaway.vdv452.model.Journey;
import org.onebusaway.vdv452.model.Line;
import org.onebusaway.vdv452.model.LineId;
import org.onebusaway.vdv452.model.Period;
import org.onebusaway.vdv452.model.RouteSequence;
import org.onebusaway.vdv452.model.Stop;
import org.onebusaway.vdv452.model.StopId;
import org.onebusaway.vdv452.model.StopPoint;
import org.onebusaway.vdv452.model.TimingGroup;
import org.onebusaway.vdv452.model.TransportCompany;
import org.onebusaway.vdv452.model.TravelTime;
import org.onebusaway.vdv452.model.VehicleType;
import org.onebusaway.vdv452.model.VersionedId;
import org.onebusaway.vdv452.model.WaitTime;

public class Vdv452Dao {
  
  private Map<VersionedId, TransportCompany> _transportCompaniesById = new HashMap<VersionedId, TransportCompany>();
  
  private Map<VersionedId, DayType> _dayTypesById = new HashMap<VersionedId, DayType>();
  
  private List<Period> _periods = new ArrayList<Period>();
  
  private Map<DayType, List<Period>> _periodsByDayType = null;

  private Map<VersionedId, TimingGroup> _timingGroupsById = new HashMap<VersionedId, TimingGroup>();

  private Map<VersionedId, VehicleType> _vehicleTypesById = new HashMap<VersionedId, VehicleType>();

  private Map<StopId, StopPoint> _stopPointsById = new HashMap<StopId, StopPoint>();

  private Map<StopId, Stop> _stopsById = new HashMap<StopId, Stop>();

  private Map<LineId, Line> _linesById = new HashMap<LineId, Line>();

  private Map<VersionedId, Journey> _journeysById = new HashMap<VersionedId, Journey>();

  private List<RouteSequence> _routeSequences = new ArrayList<RouteSequence>();

  private Map<Line, List<RouteSequence>> _routeSequencesByLine = null;

  private List<TravelTime> _travelTimes = new ArrayList<TravelTime>();

  private Map<TimingGroup, Map<Pair<StopPoint>, TravelTime>> _travelTimesByTimingGroup = null;

  private List<WaitTime> _waitTimes = new ArrayList<WaitTime>();

  private Map<TimingGroup, List<WaitTime>> _waitTimesByTimingGroup = null;

  public void putEntity(Object bean) {
    if (bean instanceof TransportCompany) {
      TransportCompany company = (TransportCompany) bean;
      _transportCompaniesById.put(company.getId(), company);
    } else if (bean instanceof DayType) {
      DayType dayType = (DayType) bean;
      _dayTypesById.put(dayType.getId(), dayType);
    } else if (bean instanceof Period) {
      _periods.add((Period) bean);
    } else if (bean instanceof TimingGroup) {
      TimingGroup group = (TimingGroup) bean;
      _timingGroupsById.put(group.getId(), group);
    } else if (bean instanceof VehicleType) {
      VehicleType vehicleType = (VehicleType) bean;
      _vehicleTypesById.put(vehicleType.getId(), vehicleType);
    } else if (bean instanceof StopPoint) {
      StopPoint stopPoint = (StopPoint) bean;
      _stopPointsById.put(stopPoint.getId(), stopPoint);
    } else if (bean instanceof Stop) {
      Stop stop = (Stop) bean;
      _stopsById.put(stop.getId(), stop);
    } else if (bean instanceof Line) {
      Line line = (Line) bean;
      _linesById.put(line.getId(), line);
    } else if (bean instanceof Journey) {
      Journey journey = (Journey) bean;
      _journeysById.put(journey.getId(), journey);
    } else if (bean instanceof RouteSequence) {
      _routeSequences.add((RouteSequence) bean);
    } else if (bean instanceof TravelTime) {
      _travelTimes.add((TravelTime) bean);
    } else if (bean instanceof WaitTime) {
      _waitTimes.add((WaitTime) bean);
    }
  }
  
  public Collection<TransportCompany> getAllTransportCompanies() {
    return _transportCompaniesById.values();
  }
  
  public TransportCompany getTransportCompanyForId(VersionedId id) {
    return _transportCompaniesById.get(id);
  }
  
  public Collection<DayType> getAllDayTypes() {
    return _dayTypesById.values();
  }

  public DayType getDayTypeForId(VersionedId id) {
    return _dayTypesById.get(id);
  }
  
  public List<Period> getPeriodsForDayType(DayType dayType) {
    if (_periodsByDayType == null) {
      _periodsByDayType = MappingLibrary.mapToValueList(_periods, "dayType");
    }
    return list(_periodsByDayType.get(dayType));
  }

  public TimingGroup getTimingGroupForId(VersionedId id) {
    return _timingGroupsById.get(id);
  }

  public Map<Pair<StopPoint>, TravelTime> getTravelTimesForTimingGroup(TimingGroup timingGroup) {
    if (_travelTimesByTimingGroup == null) {
      _travelTimesByTimingGroup = new HashMap<TimingGroup, Map<Pair<StopPoint>,TravelTime>>();
      for (TravelTime travelTime : _travelTimes) {
        Map<Pair<StopPoint>, TravelTime> travelTimesByStopPair = _travelTimesByTimingGroup.get(travelTime.getTimingGroup());
        if (travelTimesByStopPair == null) {
          travelTimesByStopPair = new HashMap<Pair<StopPoint>, TravelTime>();
          _travelTimesByTimingGroup.put(travelTime.getTimingGroup(), travelTimesByStopPair);
        }
        Pair<StopPoint> pair = Tuples.pair(travelTime.getFromStop(),
            travelTime.getToStop());
        TravelTime existing = travelTimesByStopPair.put(pair, travelTime);
        if (existing != null) {
          throw new IllegalStateException();
        }
      }
    }
    return map(_travelTimesByTimingGroup.get(timingGroup));
  }

  public List<WaitTime> getWaitTimesForTimingGroup(TimingGroup timingGroup) {
    if (_waitTimesByTimingGroup == null) {
      _waitTimesByTimingGroup = MappingLibrary.mapToValueList(_waitTimes,
          "timingGroup");
    }
    return list(_waitTimesByTimingGroup.get(timingGroup));
  }

  public VehicleType getVehicleTypeForId(VersionedId id) {
    return _vehicleTypesById.get(id);
  }

  public Collection<StopPoint> getAllStopPoints() {
    return _stopPointsById.values();
  }

  public StopPoint getStopPointForId(StopId id) {
    return _stopPointsById.get(id);
  }

  public Stop getStopForId(StopId id) {
    return _stopsById.get(id);
  }
  
  public Collection<Line> getAllLines() {
    return _linesById.values();
  }

  public Line getLineForId(LineId id) {
    return _linesById.get(id);
  }

  public Collection<Journey> getAllJourneys() {
    return _journeysById.values();
  }

  public Journey getJourneyForId(VersionedId id) {
    return _journeysById.get(id);
  }

  public List<RouteSequence> getRouteSequenceForLine(Line line) {
    if (_routeSequencesByLine == null) {
      _routeSequencesByLine = constructRouteSequencesByLine();
    }
    return list(_routeSequencesByLine.get(line));
  }

  private Map<Line, List<RouteSequence>> constructRouteSequencesByLine() {
    Map<Line, List<RouteSequence>> routeSequencesByLine = MappingLibrary.mapToValueList(
        _routeSequences, "line");
    for (List<RouteSequence> routeSequences : routeSequencesByLine.values()) {
      Collections.sort(routeSequences);
    }
    return routeSequencesByLine;
  }

  private static <T> List<T> list(List<T> values) {
    if (values == null) {
      return Collections.emptyList();
    }
    return values;
  }
  
  private static <X, Y> Map<X, Y> map(Map<X, Y> values) {
    if (values == null) {
      return Collections.emptyMap();
    }
    return values;
  }

}
