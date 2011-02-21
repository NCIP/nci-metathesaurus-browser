#!/bin/csh -f

#----------------------------------------------------------------------------------------
set cp=.
set mainDir=../..
set cp="$cp":$mainDir/conf
set cp="$cp":classes
foreach jar ($mainDir/lib/*.jar lib/*.jar)
  set cp="$cp":$jar
end
set cp="$cp":$mainDir/build/web/WEB-INF/classes
setenv CLASSPATH $cp

#----------------------------------------------------------------------------------------
set java=$JAVA_HOME/bin/java
set class=gov.nih.nci.evs.browser.test.KevinTest
# set args=(-propertyFile /home/evsuser/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml)
# set args=(-propertyFile /local/home/jboss45c/evs/ncim-webapp/conf/NCImBrowserProperties.xml)
set args=(-propertyFile ~/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml)

#----------------------------------------------------------------------------------------
$java $class $args
