@echo off
:: generate class files
javac *.java

:: run the .jar file
java -jar "Cross-Reference Index.jar"
PAUSE