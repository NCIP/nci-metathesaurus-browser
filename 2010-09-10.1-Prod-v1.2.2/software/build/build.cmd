@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
set JAVA_HOME=C:\Apps\Java\jdk1.5.0_14
set DEVPROPFILE=C:\NCI-Projects\ncim-properties\properties\dev-upgrade.properties
set CIPROPFILE=C:\NCI-Projects\ncim-properties\properties\ci-upgrade.properties
set QAPROPFILE=C:\NCI-Projects\ncim-properties\properties\qa-upgrade.properties
set DATAQAPROPFILE=C:\NCI-Projects\ncim-properties\properties\data-qa-upgrade.properties
cls
if "%1" == "" (
    echo.
    echo Available targets are:
    echo.
    echo   clean        -- Remove classes directory for clean build
    echo   all          -- Normal build of application
    echo   upgrade      -- Build and upgrade application
    echo   install      -- Builds, installs JBoss locally
    echo   reconfig     -- Reconfigure war file
    echo   dev          -- Builds, upgrades JBoss on DEV
    echo   ci           -- Builds, upgrades JBoss on CI
    echo   qa           -- Builds, upgrades JBoss on QA
    echo   data-qa      -- Builds, upgrades JBoss on Data QA
    echo   deploy       -- Redeploy application
    goto DONE
)
if "%1" == "all" (
    ant -Danthill.build.tag_built=desktop build:all
    goto DONE
)
if "%1" == "upgrade" (
    ant -Danthill.build.tag_built=desktop deploy:local:upgrade
    goto DONE
)
if "%1" == "deploy" (
    ant -Danthill.build.tag_built=desktop deploy:hot
    goto DONE
)
if "%1" == "install" (
    ant -Danthill.build.tag_built=desktop deploy:local:install
    goto DONE
)
if "%1" == "clean" (
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    ant clean
    goto DONE
)
if "%1" == "reconfig" (
    ant -Dinstall.target=install:jboss:ncimbrowser-webapp:re-configure deploy:local:install
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=%DEVPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
if "%1" == "ci" (
    ant -Dproperties.file=%CIPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
if "%1" == "qa" (
    ant -Dproperties.file=%QAPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
if "%1" == "data-qa" (
    ant -Dproperties.file=%DATAQAPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
:DONE
endlocal