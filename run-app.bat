@echo off
setlocal enabledelayedexpansion

REM Find Java installation
for /f "delims=" %%A in ('where java') do (
    set "JAVA_PATH=%%A"
    for /d %%D in ("%%~dpA..") do set "JAVA_HOME=%%D"
)

echo JAVA_HOME set to: !JAVA_HOME!

REM Build the project
cd /d "C:\Users\User\IntelliJIDEAProjects\skillverse-v2"
call mvnw.cmd clean package -DskipTests -q

REM Run the application
java -jar target/skillverse-v2-0.0.1-SNAPSHOT.jar

