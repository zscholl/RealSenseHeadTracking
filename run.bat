if not exist "%JAVA_HOME%" (
	echo "Please set the JAVA_HOME environment variable"
) ELSE (
    	"%JAVA_HOME%\bin\javac" -classpath .\libpxcclr.java.jar FaceTracking.java
	"%JAVA_HOME%\bin\java"  -classpath .\libpxcclr.java.jar;. -Djava.library.path=. FaceTracking
)