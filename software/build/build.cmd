@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
cls
if "%1" == "" (
    ant usage
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