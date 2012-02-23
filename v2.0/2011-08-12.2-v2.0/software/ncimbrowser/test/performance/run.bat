@echo off
rem -------------------------------------------------------------------------------------
setlocal enabledelayedexpansion
set ocp=%CLASSPATH%

set cp=.
set mainDir=..\..
set cp=%cp%;%mainDir%\conf
set cp=%cp%;classes
for %%x in (%mainDir%\lib\*.jar lib\*.jar) do (
  set cp=!cp!;%%x
)
set cp=%cp%;%mainDir%\build\web\WEB-INF\classes
set CLASSPATH=%cp%

set java=%JAVA_HOME%\bin\java
set class=gov.nih.nci.evs.browser.test.Test
set args=-propertyFile C:/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml

rem -------------------------------------------------------------------------------------
@echo on
"%java%" %class% %args%

@echo off
rem -------------------------------------------------------------------------------------
set CLASSPATH=%ocp%
@echo on
