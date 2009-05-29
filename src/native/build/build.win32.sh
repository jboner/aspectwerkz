#!/bin/sh

echo $JAVA_HOME

echo compiling...
gcc-2 -mno-cygwin -I../src/ -I$JAVA_HOME/include -I$JAVA_HOME/include/win32 -Wl,--add-stdcall-alias -shared -o aspectwerkz.dll ../src/aspectwerkz.cc
#gcc-2 -mno-cygwin -I../src/ -I$JAVA_HOME/include -I$JAVA_HOME/include/win32 -I$JAVA_HOME/jre/bin -Wl,-L$JAVA_HOME/jre/bin,--add-stdcall-alias -shared -o aspectwerkz.dll ../src/aspectwerkz.cc


#gcc-2 -c -mno-cygwin -I../src/ -I$JAVA_HOME/include -I$JAVA_HOME/include/win32 -shared -o ../src/aspectwerkz.o ../src/aspectwerkz.cc
#ld -L$JAVA_HOME/include -L$JAVA_HOME/include/win32 -L$JAVA_HOME/jre/bin -o aspectwerkz.dll ../src/aspectwerkz.o




