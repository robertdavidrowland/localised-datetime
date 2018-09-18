package com.example.localiseddatetime.service;

import com.example.localiseddatetime.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZoneId;

@Service
public class GeoNodesTimeZoneService implements TimeZoneService {

    private RestTemplate restTemplate;

    private String geoNodeUrl;

    private String geoNodeUsername;

    @Autowired
    public GeoNodesTimeZoneService(RestTemplate restTemplate,
                                   @Value("${geonode.url}") String geoNodeUrl,
                                   @Value("${geonode.username}") String geoNodeUsername) {

        this.restTemplate = restTemplate;

        this.geoNodeUrl = geoNodeUrl;

        this.geoNodeUsername = geoNodeUsername;
    }

    @Override
    public Location getLocationWithLocalTimeZone(Location location) {

        String url = String.format(geoNodeUrl, location.getLatitude(), location.getLongitude(), geoNodeUsername);

        GeoNodeResponse result = restTemplate.getForObject(url, GeoNodeResponse.class);

        String timeZoneId = result.getTimeZone().getTimeZoneId();

        return new Location(location.getUtcZonedDateTime(), location.getLatitude(), location.getLongitude(), ZoneId.of(timeZoneId), location.getUtcZonedDateTime().withZoneSameInstant(ZoneId.of(timeZoneId)));
    }

    @XmlRootElement(name="geonames")
    static class GeoNodeResponse {
        private TimeZone timeZone;

        @XmlElement(name="timezone")
        TimeZone getTimeZone() {
            return timeZone;
        }

        void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }
    }

    @XmlRootElement(name="timezone")
    static class TimeZone {
        private String timeZoneId;

        @XmlElement(name="timezoneId")
        String getTimeZoneId() {
            return timeZoneId;
        }

        void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
        }
    }
}
