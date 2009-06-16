#!/bin/csh -f

cd ../ncimbrowser
set CP=.
set CP="$CP":conf
set CP="$CP":build/web/WEB-INF/classes
foreach lib (lib/*.jar)
  set CP="$CP":"$lib"
end
setenv CLASSPATH $CP

set java=java
set class=gov.nih.nci.evs.browser.utils.test.SearchUtilsTest
#set config_file=-DLG_CONFIG_FILE=/local/content/evs/ncimbrowser50/config/lbconfig.props
set args=(-propertyFile /home/evsuser/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml)
set args=($args -performanceTesting)

set cmd = "$java $class $args"
echo $cmd
$cmd
