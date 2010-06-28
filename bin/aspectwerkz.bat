@REM ----------------------------------------------------------------------------------
@REM Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.
@REM http://aspectwerkz.codehaus.org
@REM ----------------------------------------------------------------------------------
@REM The software in this package is published under the terms of the LGPL style license
@REM a copy of which has been included with this distribution in the license.txt file.
@REM ----------------------------------------------------------------------------------

@REM @ECHO OFF
set ASPECTWERKZ_VERSION=2.2

IF "%1"=="" goto error
IF "%ASPECTWERKZ_HOME%"=="" goto error_no_aw_home
IF "%JAVA_COMMAND%"=="" set JAVA_COMMAND=%JAVA_HOME%\bin\java
IF "%JAVA_HOME%"=="" goto error_no_java_home

set CP=%CLASSPATH%
IF "%CP%"=="" set CP=.
IF "%CP%"=="" set CP=.

@REM Note: you can avoid declaring this since aspectwerkz-x.y.jar comes with a Manifest.mf Class-Path entry
set ASPECTWERKZ_LIBS=%ASPECTWERKZ_HOME%\lib\dom4j-1.4.jar;%ASPECTWERKZ_HOME%\lib\concurrent-1.3.1.jar;%ASPECTWERKZ_HOME%\lib\trove-1.0.2.jar;%ASPECTWERKZ_HOME%\lib\jrexx-1.1.1.jar;%ASPECTWERKZ_HOME%\lib\backport175-1.0.RC1.jar

IF "%1"=="-offline" (
    @goto offline
)

@REM -Daspectwerkz.transform.verbose=yes to turn on verbose mode
@REM -Daspectwerkz.transform.dump=package.foo.* to turn on dump in ./_dump of package.foo.* class
@REM -Daspectwerkz.classloader.wait=10 to delay connection (launching VM)
@REM -Djavax.xml.parsers.SAXParserFactory=com.bluecast.xml.JAXPSAXParserFactory for java 1.3 [see FAQ, after ProcessStarter]

@REM -- J2SE 5 auto detection
"%JAVA_COMMAND%" -cp "%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar" org.codehaus.aspectwerkz.util.EnvironmentDetect -java
IF ERRORLEVEL 15 (
    @REM -- Use for Java 1.5 --
    @REM Note: all jars could be in regular classpath but this command line tool needs to support extra -cp arguments.
    @REM FIXME: This is bad practice on 1.5 and can make CflowAspect fails
    @REM Note: For J2SE 5 prior to beta3b60, you must use -javaagent:org.codehaus.aspectwerkz.hook.Agent
    @REM set CLASSPATH=%CLASSPATH%;%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-extensions-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-jdk5-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_LIBS%
    "%JAVA_COMMAND%" -javaagent:%ASPECTWERKZ_HOME%\lib\aspectwerkz-jdk5-%ASPECTWERKZ_VERSION%.jar %*

    @exit /B %ERRORLEVEL%
)

@REM -- JRockit (1.3 / 1.4) auto detection
"%JAVA_COMMAND%" -cp "%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar" org.codehaus.aspectwerkz.util.EnvironmentDetect -jvm
IF ERRORLEVEL 2 (
    @REM -- Use for BEA JRockit --
    "%JAVA_COMMAND%" -Xmanagement:class=org.codehaus.aspectwerkz.extension.jrockit.JRockitPreProcessor -Xbootclasspath/p:"%ASPECTWERKZ_HOME%\lib\aspectwerkz-extensions-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-jdk14-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_LIBS%;%ASPECTWERKZ_HOME%\lib\piccolo-1.03.jar" %*

    @exit /B %ERRORLEVEL%
)

@REM -- Use for Sun HotSpot and IBM JRE --
"%JAVA_COMMAND%" -cp "%JAVA_HOME%\lib\tools.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar" org.codehaus.aspectwerkz.hook.ProcessStarter -Xbootclasspath/p:"\"%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar\"" -cp "\"%CP%\"" -cp "\"%ASPECTWERKZ_HOME%\lib\aspectwerkz-extensions-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-jdk14-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_LIBS%\"" %*

@exit /B %ERRORLEVEL%

:offline
    IF %1=="" goto error
    IF %2=="" goto error
    IF %3=="" goto error
    "%JAVA_COMMAND%" -Daspectwerkz.transform.filter=no -Daspectwerkz.definition.file=%2 -cp "%ASPECTWERKZ_HOME%\lib\ant-1.5.2.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-extensions-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-jdk14-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_LIBS%" "org.codehaus.aspectwerkz.compiler.AspectWerkzC" %3 %4 %5 %6 %7 %8 %9
    @exit /B %ERRORLEVEL%

:error
    IF EXIST "%ASPECTWERKZ_HOME%\bin\usage.txt" (
        type "%ASPECTWERKZ_HOME%\bin\usage.txt"
    ) ELSE (
        echo ASPECTWERKZ_HOME does not point to the aspectwerkz directory
    )
@goto error_exit

:error_no_java_home
	@echo Please specify the JAVA_HOME environment variable.
@goto error_exit

:error_no_aw_home
	@echo Please specify the ASPECTWERKZ_HOME environment variable.
@goto error_exit

:error_exit
@exit /B -1
