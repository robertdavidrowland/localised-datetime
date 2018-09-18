package com.example.localiseddatetime.service;

import com.example.localiseddatetime.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoNodesTimeZoneServiceIntegrationTest {

    @Autowired
    private TimeZoneService service;

    @Test
    public void firstLocalTimeZoneTest() {
        Location rawLocation = new Location(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T02:52:49"), ZoneId.of("UTC")), "-44.490947", "171.220966");

        Location processedLocation = service.getLocationWithLocalTimeZone(rawLocation);

        assertEquals(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T14:52:49"), ZoneId.of("Pacific/Auckland")), processedLocation.getLocalZonedDateTime());
        assertEquals(ZoneId.of("Pacific/Auckland"), processedLocation.getLocalZoneId());
    }

    @Test
    public void secondLocalTimeZoneTest() {
        Location rawLocation = new Location(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T02:52:49"), ZoneId.of("UTC")), "-33.912167", "151.215820");

        Location processedLocation = service.getLocationWithLocalTimeZone(rawLocation);

        assertEquals(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T12:52:49"), ZoneId.of("Australia/Sydney")), processedLocation.getLocalZonedDateTime());
        assertEquals(ZoneId.of("Australia/Sydney"), processedLocation.getLocalZoneId());
    }
}
