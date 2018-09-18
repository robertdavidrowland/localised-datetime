package com.example.localiseddatetime.service;

import com.example.localiseddatetime.model.Location;

public interface TimeZoneService {
    Location getLocationWithLocalTimeZone(Location location);
}
