Temerature Monitor
=================

A Web application using
- Spring MVC
- Gradle to build project and generate the WAR file
- gretty plugin 

### Developer Instructions

#### Set up the development environment
- Download/ clone the repository
- Import the project in your IDE
- Make changes
- Deploy on a web application server



####Running / Testing the application locally

Create Eclipse projct files / synchronize Eclipse project 
```
gradle clean eclipe
```


Run application on local server (uses gretty, jetty 9) 
```
gradle clean jettyRun
```


Debug application on local server (gretty, jetty)

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

