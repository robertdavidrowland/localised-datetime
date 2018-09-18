package com.example.localiseddatetime.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Location {

    private final ZonedDateTime utcZonedDateTime;

    private final String latitude;

    private final String longitude;

    private final ZoneId localZoneId;

    private final ZonedDateTime localZonedDateTime;

    public Location(ZonedDateTime utcZonedDateTime, String latitude, String longitude) {
        this.utcZonedDateTime = utcZonedDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localZoneId = ZoneId.of("UTC");
        this.localZonedDateTime = utcZonedDateTime;
    }

    public Location(ZonedDateTime utcZonedDateTime, String latitude, String longitude, ZoneId zoneId, ZonedDateTime localZonedDateTime) {
        this.utcZonedDateTime = utcZonedDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localZoneId = zoneId;
        this.localZonedDateTime = localZonedDateTime;
    }

    public ZonedDateTime getUtcZonedDateTime() {
        return utcZonedDateTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public ZoneId getLocalZoneId() {
        return localZoneId;
    }

    public ZonedDateTime getLocalZonedDateTime() {
        return localZonedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(utcZonedDateTime, location.utcZonedDateTime) &&
                Objects.equals(latitude, location.latitude) &&
                Objects.equals(longitude, location.longitude) &&
                Objects.equals(localZoneId, location.localZoneId) &&
                Objects.equals(localZonedDateTime, location.localZonedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utcZonedDateTime, latitude, longitude, localZoneId, localZonedDateTime);
    }

    @Override
    public String toString() {
        return "Location{" +
                "utcZonedDateTime=" + utcZonedDateTime +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", localZoneId=" + localZoneId +
                ", localZonedDateTime=" + localZonedDateTime +
                '}';
    }
}
