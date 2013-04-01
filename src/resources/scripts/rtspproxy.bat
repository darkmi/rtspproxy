@REM ----------------------------------------------------------------------------
@REM Copyright 2001-2004 The Apache Software Foundation.
@REM 
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM 
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM 
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------
@REM 

@REM ----------------------------------------------------------------------------
@REM Maven2 Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM M2_HOME - location of maven2's installed home dir
@REM RTSPPROXY_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM RTSPPROXY_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM RTSPPROXY_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug RtspProxy itself, use
@REM set RTSPPROXY_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case RTSPPROXY_BATCH_ECHO is 'on'
@echo off
@REM enable echoing my setting RTSPPROXY_BATCH_ECHO to 'on'
@if "%RTSPPROXY_BATCH_ECHO%" == "on"  echo %RTSPPROXY_BATCH_ECHO%

@REM Execute a user defined script before this one
if exist "%HOME%\rtspproxyrc_pre.bat" call "%HOME%\rtspproxyrc_pre.bat"

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

set ERROR_CODE=0

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
set ERROR_CODE=1
goto end

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkMHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
set ERROR_CODE=1
goto end

:chkMHome
if not "%RTSPPROXY_HOME%"=="" goto valMHome

if "%OS%"=="Windows_NT" SET RTSPPROXY_HOME=%~dps0\.
if not "%RTSPPROXY_HOME%"=="" goto valMHome

echo.
echo ERROR: RTSPPROXY_HOME not found in your environment.
echo Please set the RTSPPROXY_HOME variable in your environment to match the
echo location of the RtspProxy installation
echo.
set ERROR_CODE=1
goto end

:valMHome
if exist "%RTSPPROXY_HOME%\rtspproxy.bat" goto init

echo.
echo ERROR: RTSPPROXY_HOME is set to an invalid directory.
echo RTSPPROXY_HOME = %RTSPPROXY_HOME%
echo Please set the RTSPPROXY_HOME variable in your environment to match the
echo location of the RtspProxy installation
echo.
set ERROR_CODE=1
goto end
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set RTSPPROXY_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set RTSPPROXY_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set RTSPPROXY_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set RTSPPROXY_CMD_LINE_ARGS=%RTSPPROXY_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET RTSPPROXY_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@REM Start RTSPPROXY
SetLocal EnableDelayedExpansion
SET RTSPROXY_CLASSPATH=
for %%i in ("%RTSPPROXY_HOME%"\lib\*.jar) do set RTSPROXY_CLASSPATH=!RTSPROXY_CLASSPATH!%%i;
%RTSPPROXY_JAVA_EXE% %RTSPPROXY_OPTS% -classpath "%RTSPROXY_CLASSPATH%"  "-Drtspproxy.home=%RTSPPROXY_HOME%" rtspproxy.Main %RTSPPROXY_CMD_LINE_ARGS%
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set RTSPPROXY_JAVA_EXE=
set RTSPPROXY_CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec
if exist "%HOME%\rtspproxyrc_post.bat" call "%HOME%\rtspproxyrc_post.bat"
@REM pause the batch file if RTSPPROXY_BATCH_PAUSE is set to 'on'
if "%RTSPPROXY_BATCH_PAUSE%" == "on" pause

exit /B %ERROR_CODE%


