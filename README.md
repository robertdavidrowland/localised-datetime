# Localised datetime from UTC time, latitude and longitude

## Overview

Develop a small application to read a CSV with a UTC datetime, latitude and longitude columns and append the timezone the vehicle is in and the localised datetime. See example of CSV input and output below. We will then run this over several test files with several rows of data. 

Input 
2013-07-10 02:52:49,-44.490947,171.220966
2013-07-10 02:52:49,-33.912167,151.215820

Output 
2013-07-10 02:52:49,-44.490947,171.220966,Pacific/Auckland,2013-07-10T14:52:49
2013-07-10 02:52:49,-33.912167,151.215820,Australia/Sydney,2013-07-10T12:52:49

## Usage

```
> mvn package
> java -jar target/localised-datetime-0.0.1-SNAPSHOT.jar src/test/data/location-good-data.csv
```
 
## Implementation details

This has been set up as Spring Boot project using Java 8 features.  The application is built with Maven.

An external API GeoNames (http://www.geonames.org/) is used to get Timezone from latitude and longitude.  If the username in this example throttles please register for your own username and update an application.properties file alongside the compiled jar. 

## To Do

* Write acceptance test
* Ensure API errors are caught and handled correctly
* Query use of String for latitude/longitude
* Investigate and assess other timezone APIs (i.e. Google Timezone API)
* Investigate and assess possibly of offline implementations
* Check possible edge cases (DST, Coastal Regions, Ocean)
