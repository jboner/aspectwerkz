@echo off
%JAVA_HOME%\bin\java -version

SETLOCAL

set ASPECTWERKZ_VERSION=1.0
set ASPECTWERKZ_LIBS=%ASPECTWERKZ_HOME%\lib\dom4j-1.4.jar;%ASPECTWERKZ_HOME%\lib\concurrent-1.3.1.jar;%ASPECTWERKZ_HOME%\lib\trove-1.0.2.jar;%ASPECTWERKZ_HOME%\lib\commons-jexl-1.0-beta-2.jar;%ASPECTWERKZ_HOME%\lib\piccolo-1.03.jar;%ASPECTWERKZ_HOME%\lib\jrexx-1.1.1.jar


set OPT=
@rem set OPT=-verbose:class

cd %ASPECTWERKZ_HOME%\src\native\test
%JAVA_HOME%\bin\javac -classpath %ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar test\*.java
cd %ASPECTWERKZ_HOME%\src\native

echo ---- Standard application
%JAVA_HOME%\bin\java %OPT% -cp %ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\bcel.jar;%ASPECTWERKZ_HOME%\src\native\test test.Test

echo ---- Use of native module
set PATH=%ASPECTWERKZ_HOME%\src\native\build;%PATH%
%JAVA_HOME%\bin\java -Xdebug -Xrunaspectwerkz %OPT% -Xbootclasspath/a:%JAVA_HOME%\lib\tools.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\bcel.jar -cp %ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_LIBS%;%ASPECTWERKZ_HOME%\src\native\test -Daspectwerkz.definition.file=%ASPECTWERKZ_HOME%\src\native\test\native.xml test.Test

echo ---- Use of native module with -D option
set PATH=%ASPECTWERKZ_HOME%\src\native\build;%PATH%
set OPT=-Daspectwerkz.classloader.clpreprocessor=org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl %OPT%
%JAVA_HOME%\bin\java -Xdebug -Xrunaspectwerkz %OPT% -Xbootclasspath/a:%JAVA_HOME%\lib\tools.jar;%ASPECTWERKZ_HOME%\lib\aspectwerkz-core-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_HOME%\lib\bcel.jar -cp %ASPECTWERKZ_HOME%\lib\aspectwerkz-%ASPECTWERKZ_VERSION%.jar;%ASPECTWERKZ_LIBS%;%ASPECTWERKZ_HOME%\src\native\test -Daspectwerkz.definition.file=%ASPECTWERKZ_HOME%\src\native\test\native.xml test.Test


ENDLOCAL