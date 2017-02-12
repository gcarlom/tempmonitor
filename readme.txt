#To build the project
C:\Users\gc\workspace\tempmonitor\tempmonitor>setenv.bat

C:\Users\gc\workspace\tempmonitor\tempmonitor>gradle clean build


# To run the project in gradle:

3## Set JAVA_HOME to JDK 7 
C:\Users\gc\workspace\tempmonitor\tempmonitor>setenv.bat
C:\Users\gc\workspace\tempmonitor\tempmonitor>gradle clean run

# to run the generated distribution
## Set JAVA_HOME to JDK 7
C:\Users\gc\workspace\tempmonitor\tempmonitor>setenv.bat

## build (generate distribution as well in C:\Users\gc\workspace\tempmonitor\tempmonitor\build\distributions )
C:\Users\gc\workspace\tempmonitor\tempmonitor>gradle clean build


## unzip the  C:\Users\gc\workspace\tempmonitor\tempmonitor\build\distributions\tempmonitor.zip in a new dir <MY-DIR>

## open dos command window in <MY-DIR>\tempmonitor\bin
 >unzip ....

## set JAVA_HOME to JDK7, then run the bat file
>set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_40

> <MY-DIR>\tempmonitor\bin\tempmonitor.bat

== To run on develop env (my PC:)

C:\Users\gc\workspace\tempmonitor\tempmonitor> gradle clean eclipse
 (set eclipse files. then in  eclipse project clean, refresh)
 
C:\Users\gc\workspace\tempmonitor\tempmonitor>gradle clean jettyRun

then open in browser: http://localhost:8080/tempmonitor/

> gradle dependencies 
  useful to see libs dependencies!
