@echo off
set OCP=%CLASSPATH%
setlocal enabledelayedexpansion

cd ..\ncimbrowser
set CP=.
set CP=%CP%;conf
set CP=%CP%;build\web\WEB-INF\classes
for %%x in (lib\*.jar) do (
  set CP=!CP!;%%x
)
for %%x in (..\test\lib\*.jar) do (
  set CP=!CP!;%%x
)
set CLASSPATH=%CP%

set java=java
set class=gov.nih.nci.evs.browser.utils.test.MainTest
set args=-propertyFile C:/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml

@echo on
%java% %config_file% %class% %args%
@echo off

set CLASSPATH=%OCP%
@echo on
