echo off

del sources.txt 
rmdir /S /Q build

REM *** set to your JAVA_HOME location
REM set JAVA_HOME=c:\Progra~1\Java\jdk1.8.0_151

mkdir build
dir *.java /s /b > sources.txt
%JAVA_HOME%/bin/javac -d ./build @sources.txt
cd build
%JAVA_HOME%/bin/jar cvf evolutionEngine.jar .
cd ..
del sources.txt 

echo ----------------------------------
echo Sources are compiled and packed 
echo to ./build/evolutionEngine.jar
echo ----------------------------------
pause


REM ** cd build 
REM ** %JAVA_HOME%/bin/java -cp tal.jar Main
REM ** cd ..