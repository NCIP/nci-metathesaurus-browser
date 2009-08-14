#!/bin/csh -f

#----------------------------------------------------------------------------------------
cd ..
set cp=.
set cp="$cp":../conf
set cp="$cp":classes
foreach jar (../lib/*.jar lib/*.jar)
  set cp="$cp":"$jar"
end
echo $cp
setenv CLASSPATH $cp

#----------------------------------------------------------------------------------------
set java=$JAVA_HOME/bin/java
set class=gov.nih.nci.evs.browser.utils.test.Main
set args=

#----------------------------------------------------------------------------------------
$java $class $args
