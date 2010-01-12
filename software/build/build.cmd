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
    echo   qa           -- Builds, upgrades JBoss on QA
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
if "%1" == "dev" (
    ant -Dproperties.file=C:\SVN-Projects\ncim-properties\properties\dev-upgrade.properties -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
if "%1" == "qa" (
    ant -Dproperties.file=C:\SVN-Projects\ncim-properties\properties\qa-upgrade.properties -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)

:DONE
endlocal