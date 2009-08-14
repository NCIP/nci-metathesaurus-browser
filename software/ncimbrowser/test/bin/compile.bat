@echo off
rem -------------------------------------------------------------------------------------
setlocal enabledelayedexpansion
set ocp=%CLASSPATH%

cd ..
set cp=.
for %%x in (..\lib\*.jar) do (
  set cp=!cp!;%%x
)
set CLASSPATH=%cp%

set javac=C:\Progra~1\Java\jdk1.5.0_14\bin\javac.exe

rem -------------------------------------------------------------------------------------
@echo on
mkdir .\classes
%javac% -d .\classes src/java/gov/nih/nci/evs/browser/utils/test/*.java

@echo off
rem -------------------------------------------------------------------------------------
set CLASSPATH=%ocp%
@echo on
