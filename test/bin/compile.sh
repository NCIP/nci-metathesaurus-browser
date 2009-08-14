#!/bin/csh -f

#----------------------------------------------------------------------------------------
cd ..
set cp=.
foreach jar (../lib/*.jar)
  set cp="$cp":$jar
end
setenv CLASSPATH $cp

#----------------------------------------------------------------------------------------
set javac=$JAVA_HOME/bin/javac

#----------------------------------------------------------------------------------------
mkdir -p classes
$javac -d classes src/java/gov/nih/nci/evs/browser/utils/test/*.java
