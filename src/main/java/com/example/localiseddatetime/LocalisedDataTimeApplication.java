package com.example.localiseddatetime;

import com.example.localiseddatetime.dao.CSVFileLocationDAO;
import com.example.localiseddatetime.model.Location;
import com.example.localiseddatetime.service.TimeZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class LocalisedDataTimeApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(LocalisedDataTimeApplication.class);

	@Autowired
	private CSVFileLocationDAO locationDAO;

	@Autowired
	private TimeZoneService timeZoneService;

	@Override
	public void run(String... args) {

		if (args.length < 1) {
			output("Usage: java -jar localiseddatetime-0.0.1-SNAPSHOT.jar <location-file.csv>");
			return;
		}

		String rawLocationFilePath = args[0];
		List<Location> rawLocations = null;
		try {
			rawLocations = locationDAO.loadLocations(rawLocationFilePath);
		} catch (IllegalArgumentException |IOException e) {
			output("Error loading locations csv: " + e.getMessage());
			System.exit(1);
		}

		List<Location> processedLocations = rawLocations.stream()
				.map(timeZoneService::getLocationWithLocalTimeZone)
				.collect(Collectors.toList());

		String processedLocationFilePath = rawLocationFilePath.replace(".csv", "") + ".processed.csv";

		try {
			locationDAO.saveLocations(processedLocations, processedLocationFilePath);
		} catch (IllegalArgumentException |IOException e) {
			output("Error saving locations csv: " + e.getMessage());
			System.exit(1);
		}

		output("output written to: " + processedLocationFilePath);
	}
	
	private static void output (String output) {
		LOG.debug(output);
		System.out.println(output);
	}
	
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LocalisedDataTimeApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args); 
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
