Temperature Monitor
=================


A Web application which parses text file containing a temperature log and checks whether values were within a give temperature range.


Technology stack:
- Spring MVC
- Gradle to build project and generate the WAR file
- gretty plugin 

Temperature CSV file should have following format:
```
  Point, Timestamp (US format), Temperature (° C), Alarms,
  1,12/14/2016 11:15:24 AM,5.1,,
```
Note: The first line is regarded as Header and is ignored

### Developer Instructions

#### Set up the development environment
- Download/ clone the repository
- Import the project in your IDE
- Make changes
- Deploy on a web application server



#### Running / Testing the application locally

Create Eclipse project files / synchronize Eclipse project
```
gradle clean eclipse
```


Run application on local server (uses gretty, jetty 9) 
```
gradle clean jettyRun
```


Debug application on local server (gretty, jetty 9)
```
gradle clean jettyRunDebug
```


Open your browser at the URL
http://localhost:8080/tempmonitor/



#### Building, Testing and Generating the WAR file

To Generate the WAR file
```
gradle clean war
```

You will find the WAR file under the build/libs directory

