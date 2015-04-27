:: Graylog Agent startup script for Windows

@ECHO OFF

SETLOCAL ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION
TITLE Graylog Agent ${project.version}

IF DEFINED JAVA_HOME goto :continue

:: JAVA_HOME needs to be set to find the JVM.
:jvmError
ECHO JAVA_HOME not set! 1>&2
EXIT /B 1

:: Execute the JAR.
:continue
set BIN_DIR=%~dp0

:: Get root directory of the Agent.
FOR %%D in ("%BIN_DIR%..") DO SET AGENT_ROOT=%%~dpfD

SET AGENT_JAR="%AGENT_ROOT%\${project.artifactId}.jar"

"%JAVA_HOME%\bin\java" ${agent.jvm-opts} -jar "%AGENT_JAR%" %*

ENDLOCAL