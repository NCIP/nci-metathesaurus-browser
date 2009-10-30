#!/bin/csh -f

#----------------------------------------------------------------------------------------
cd ..
set cp=.
foreach jar (../lib/*.jar)
  set cp="$cp":$jar
end
set cp="$cp":../build/web/WEB-INF/classes
setenv CLASSPATH $cp

#----------------------------------------------------------------------------------------
set javac=$JAVA_HOME/bin/javac

#----------------------------------------------------------------------------------------
rm -fr classes
mkdir -p classes
$javac -d classes src/java/gov/nih/nci/evs/browser/test/lexevs/*.java src/java/gov/nih/nci/evs/browser/test/utils/*.java
