echo %off
setlocal enabledelayedexpansion
cd ../build
ant deploy:hot
echo %on
