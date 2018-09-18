package com.example.localiseddatetime.service;

import com.example.localiseddatetime.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class GeoNodesTimeZoneServiceTest {

    @Mock
    private RestTemplate restTemplate = new RestTemplate();

    @InjectMocks
    private TimeZoneService service = new GeoNodesTimeZoneService(restTemplate, "url", "user");

    @Test
    public void firstLocalTimeZoneTest() {

        mockResponse("Pacific/Auckland");

        Location rawLocation = new Location(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T02:52:49"), ZoneId.of("UTC")), "-44.490947", "171.220966");

        Location processedLocation = service.getLocationWithLocalTimeZone(rawLocation);

        assertEquals(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T14:52:49"), ZoneId.of("Pacific/Auckland")), processedLocation.getLocalZonedDateTime());
        assertEquals(ZoneId.of("Pacific/Auckland"), processedLocation.getLocalZoneId());
    }

    @Test
    public void secondLocalTimeZoneTest() {

        mockResponse("Australia/Sydney");

        Location rawLocation = new Location(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T02:52:49"), ZoneId.of("UTC")), "-33.912167", "151.215820");

        Location processedLocation = service.getLocationWithLocalTimeZone(rawLocation);

        assertEquals(ZonedDateTime.of(LocalDateTime.parse("2013-07-10T12:52:49"), ZoneId.of("Australia/Sydney")), processedLocation.getLocalZonedDateTime());
        assertEquals(ZoneId.of("Australia/Sydney"), processedLocation.getLocalZoneId());
    }

    private void mockResponse(String s) {
        GeoNodesTimeZoneService.TimeZone timeZone = new GeoNodesTimeZoneService.TimeZone();
        timeZone.setTimeZoneId(s);

        GeoNodesTimeZoneService.GeoNodeResponse geoNodeResponse = new GeoNodesTimeZoneService.GeoNodeResponse();
        geoNodeResponse.setTimeZone(timeZone);

        Mockito.when(restTemplate.getForObject(
                anyString(),
                any(Class.class))
        ).thenReturn(geoNodeResponse);
    }
}
