package com.example.localiseddatetime.dao;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.example.localiseddatetime.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CSVFileLocationDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CSVFileLocationDAO.class);

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final static String RECORD_DELIMITER = ",";

	private final static String NEWLINE_DELIMITER = "\n";

	public List<Location> loadLocations(String filePath) throws IOException, IllegalArgumentException {
	    try (
                BufferedReader br = new BufferedReader(new FileReader(filePath))
	    ) {
	    	AtomicInteger noOfEntries = new AtomicInteger(0);
	    	
	    	List<Location> inputList = br.lines()
	    			 .skip(1)
	    			 .map(l -> {
	    				noOfEntries.incrementAndGet();
						LOG.debug("parse line {}", l);
						String[] v = l.split(RECORD_DELIMITER);
						try {

							ZonedDateTime utcZonedDateTime = ZonedDateTime.of(LocalDateTime.parse(v[0], formatter), ZoneId.of("UTC"));
							String latitude = String.valueOf(v[1]);
							String longitude = String.valueOf(v[2]);

							return new Location(utcZonedDateTime, latitude, longitude);
						}
						catch (ArrayIndexOutOfBoundsException e) {
							LOG.debug("error: {} expected 3 columns, only found {}", e.getMessage(), v.length);
						}
						catch (DateTimeParseException e) {
							LOG.debug("error: {} expected parsable datetime in format '2013-07-10 02:52:49', found {}", e.getMessage(), v[0]);
						}
						catch (IllegalArgumentException e) {
							LOG.debug("error: {}", e.getMessage());
						}
						return null;	    				 
	    			 })
	    			 .filter(Objects::nonNull)
	    			 .collect(Collectors.toList());
	    	
	    	if (noOfEntries.get() != inputList.size()) {
				throw new IllegalArgumentException("attempt to load Location data with invalid Locations - check logs for details");
	    	}
	    	
			return inputList;     
	    }
	}

	public void saveLocations(List<Location> locations, String filePath) throws IOException, IllegalArgumentException {
		try (
				FileWriter fileWriter = new FileWriter(filePath);
				PrintWriter printWriter = new PrintWriter(fileWriter)
		) {
			locations.forEach(location -> printWriter.print(toCsv(location)));
		}
	}

	private static String toCsv(Location location) {
		return location.getUtcZonedDateTime().format(formatter) + RECORD_DELIMITER +
				location.getLatitude() + RECORD_DELIMITER +
				location.getLongitude() + RECORD_DELIMITER +
				location.getLocalZoneId() + RECORD_DELIMITER +
				location.getLocalZonedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + NEWLINE_DELIMITER;
	}
}
