@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
@rem Environment settings here...
set DEVPROPFILE=C:\NCI-Projects\ncim-properties\properties\dev-upgrade.properties
set CIPROPFILE=C:\NCI-Projects\ncim-properties\properties\ci-upgrade.properties
set QAPROPFILE=C:\NCI-Projects\ncim-properties\properties\qa-upgrade.properties
set DATAQAPROPFILE=C:\NCI-Projects\ncim-properties\properties\data-qa-upgrade.properties
set DEBUG=-Denable.install.debug=false
set TAG=-Danthill.build.tag_built=desktop
@rem Test is debug has been set
if "%2" == "debug" (
    set DEBUG=-Denable.install.debug=true -debug
)
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
    echo   deploy       -- Hot deploy application
    echo   jsp          -- Hot deploy JSP files
    echo   stop         -- Stop war file
    echo   start        -- Start war file
    echo   cissh        -- Test SSH login in CI
    goto DONE
)
if "%1" == "all" (
    ant %TAG% build:all
    goto DONE
)
if "%1" == "upgrade" (
    ant %TAG% %DEBUG% deploy:local:upgrade
    goto DONE
)
if "%1" == "install" (
    ant %TAG% %DEBUG% deploy:local:install
    goto DONE
)
if "%1" == "deploy" (
    ant %TAG% %DEBUG% deploy:hot
    goto DONE
)
if "%1" == "stop" (
    ant %TAG% %DEBUG% deploy:stop
    goto DONE
)
if "%1" == "start" (
    ant %TAG% %DEBUG% deploy:start
    goto DONE
)
if "%1" == "jsp" (
    ant %DEBUG% deploy:hot:jsp
    goto DONE
)
if "%1" == "clean" (
    ant clean
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    goto DONE
)
if "%1" == "reconfig" (
    ant -Dinstall.target=install:jboss:ncimbrowser-webapp:re-configure deploy:local:install
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=%DEVPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if "%1" == "ci" (
    ant -Dproperties.file=%CIPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if "%1" == "qa" (
    ant -Dproperties.file=%QAPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if "%1" == "data-qa" (
    ant -Dproperties.file=%DATAQAPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if "%1" == "cissh" (
    ssh jboss51d@ncias-c512-v.nci.nih.gov -i C:\NCI-Projects\ssh-keys\id_dsa_bda echo "Test worked!"
    goto DONE
)
:DONE
endlocal