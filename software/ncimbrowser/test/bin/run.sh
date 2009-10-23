#!/bin/csh -f

#----------------------------------------------------------------------------------------
cd ..
set cp=.
set cp="$cp":../conf
set cp="$cp":classes
foreach jar (../lib/*.jar lib/*.jar)
  set cp="$cp":$jar
end
set cp="$cp":../build/web/WEB-INF/classes
setenv CLASSPATH $cp

#----------------------------------------------------------------------------------------
set java=$JAVA_HOME/bin/java
set class=gov.nih.nci.evs.browser.utils.test.Main
# set args=(-propertyFile /home/evsuser/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml)
set args=(-propertyFile /local/home/jboss45c/evs/ncim-webapp/conf/NCImBrowserProperties.xml)


#----------------------------------------------------------------------------------------
$java $class $args
