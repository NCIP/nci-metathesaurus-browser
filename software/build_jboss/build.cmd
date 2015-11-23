@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
@rem Environment settings here...
set DEBUG=-Denable.install.debug=false
set TAG=-Danthill.build.tag_built=desktop
@rem Test is debug has been set
if "%2" == "debug" (
    set DEBUG=-debug
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
    echo   uninstall    -- Uninstall the web application
    echo   deploy       -- Hot deploy application
    echo   jsp          -- Hot deploy JSP files
    echo   config       -- Generates configuration files for a target environment
    echo   stop         -- Shutdown JBoss
    echo   start        -- Start JBoss
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

if "%1" == "uninstall" (
    ant %TAG% %DEBUG% deploy:local:uninstall
    goto DONE
)

if "%1" == "deploy" (
    ant %TAG% %DEBUG% deploy:hot
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

if "%1" == "config" (
    ant %DEBUG% build:config
    goto DONE
)

if "%1" == "stop" (
    ant %DEBUG% jboss:stop
    goto DONE
)

if "%1" == "start" (
    ant %DEBUG% jboss:start
    goto DONE
)

:DONE
endlocal