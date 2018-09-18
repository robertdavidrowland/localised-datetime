package com.example.localiseddatetime.dao;

import com.example.localiseddatetime.model.Location;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVFileLocationDAOTest {

	private CSVFileLocationDAO dao = new CSVFileLocationDAO();
	
	@Test
	public void getLocationsFromGoodFile() throws IOException, IllegalArgumentException {
		List<Location> locations = dao.loadLocations("src/test/data/location-good-data.csv");
	
		assertEquals(2, locations.size());
		assertEquals(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T02:52:49"), ZoneId.of("UTC")), locations.get(0).getUtcZonedDateTime());
		assertEquals("-44.490947", locations.get(0).getLatitude());
		assertEquals("171.220966", locations.get(0).getLongitude());
	}

	@Test(expected = IOException.class)
	public void getLocationsFromMissingFileShouldThrowIOException() throws IOException, IllegalArgumentException {
		dao.loadLocations("src/test/data/missing-location-file.csv");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getLocationsFromBadFileShouldThrowIllegalArgumentException() throws IOException, IllegalArgumentException {
		dao.loadLocations("src/test/data/location-bad-data.csv");
	}
}
