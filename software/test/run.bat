@echo off
set OCP=%CLASSPATH%
setlocal enabledelayedexpansion

cd ..\ncimbrowser
set COMMONS=.
set COMMONS=%COMMONS%;conf
set COMMONS=%COMMONS%;build\web\WEB-INF\classes
for %%x in (lib\*.*) do (
  set COMMONS=!COMMONS!;%%x
)
set CLASSPATH=%COMMONS%

set java=java
set class=gov.nih.nci.evs.browser.utils.SearchUtils
rem set config_file=-DLG_CONFIG_FILE=t:\2.3.0\resources\config\config.props
set args=-propertyFile C:/apps/evs/ncim-webapp/conf/NCImBrowserProperties.xml
set args=%args% -performanceTesting

@echo on
%java% %config_file% %class% %args%
@echo off

set CLASSPATH=%OCP%
@echo on
