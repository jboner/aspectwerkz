#!/bin/sh

echo $JAVA_HOME

echo compiling...
g++ -dynamiclib -I../src/ -I/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers -o libaspectwerkz.so -framework JavaVM ../src/aspectwerkz.cc


