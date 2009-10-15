@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
cls
if "%1" == "" (
    echo.
    echo Available targets are:
    echo.
    echo   clean        -- Remove classes directory for clean build
    echo   all          -- Normal build of application
    echo   upgrade      -- Build and upgrade application
    echo   install      -- Builds, installs JBoss locally
    echo   dev          -- Builds, upgrades JBoss on DEV
    echo   deploy       -- Redeploy application
    goto DONE
)
if "%1" == "all" (
    ant build:all
    goto DONE
)
if "%1" == "upgrade" (
    ant deploy:local:upgrade
    goto DONE
)
if "%1" == "deploy" (
    ant deploy:hot
    goto DONE
)
if "%1" == "install" (
    ant deploy:local:install
    goto DONE
)
if "%1" == "clean" (
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    ant clean
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=C:\SVN-Projects\ncim-properties\properties\dev-upgrade.properties deploy:remote:upgrade
    rem ant -Dproperties.file=C:\@\src\browsers\ncim.properties\dev-upgrade.properties deploy:remote:upgrade
    goto DONE
)

:DONE
endlocal