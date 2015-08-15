package org.onebusaway.vdv452;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.onebusaway.vdv452.model.Stop;
import org.onebusaway.vdv452.model.StopPoint;

import junit.framework.Assert;

public class Vdv452ReaderTest {
	private static final String TEST_VDV452 = "src/test/resources/vdv452_sasa.zip";
	private static Map<Long, String> exampleStopWkt;

	@BeforeClass
	public static void setupExampleStops() {
		exampleStopWkt = new HashMap<>(2);
		exampleStopWkt.put(5026L, "POINT (11.351923888888889 46.497778333333336)");
		exampleStopWkt.put(5027L, "POINT (11.351668888888888 46.49784805555556)");
	}

	@Test
	public void readSasaGtfsStopLocations() throws IOException {
		final Vdv452Reader reader = new Vdv452Reader();
	    reader.setInputLocation(new File(TEST_VDV452));
	    reader.run();

	    int checked = 0;
	    final Vdv452Dao dao = reader.getDao();
	    final Collection<StopPoint> stops = dao.getAllStopPoints();
	    for (final StopPoint sp : stops) {
			final Stop s = dao.getStopForId(sp.getId());
			final long stopId = s.getId().getId();
			if (exampleStopWkt.containsKey(stopId)) {
				final String stopWkt = "POINT (" + s.getLng() + " " + + s.getLat() + ")";

				checked++;
				Assert.assertEquals("Unexpected example stop location", exampleStopWkt.get(stopId), stopWkt);
			}
		}

	    Assert.assertEquals("Expected example stop not found", exampleStopWkt.size(), checked);
	}

}
