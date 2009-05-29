#!/bin/sh

echo $JAVA_HOME

echo compiling...
gcc -I../src/ -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -shared -o libaspectwerkz.so ../src/aspectwerkz.cc


