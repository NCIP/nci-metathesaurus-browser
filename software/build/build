#!/bin/bash
# ******************************************
# **** Command file to invoke build.xml ****
# ******************************************
# Environment settings here...
DEBUG=-Denable.install.debug=false
TAG=-Danthill.build.tag_built=$USER

if [ "$2" = "debug" ]; then
   DEBUG="$DEBUG -debug"
fi
clear

if [ -z "$1" ]; then
    echo
    echo "Available targets are:"
    echo
    echo "  clean        -- Remove classes directory for clean build"
    echo "  all          -- Normal build of application"
    echo "  upgrade      -- Build and upgrade application"
    echo "  install      -- Builds, installs JBoss locally"
    echo "  deploy       -- Hot deploy application"
    echo "  jsp          -- Hot deploy JSP files"
    echo
    exit
fi

case $1 in
    all)
        ant $TAG build:all
        ;;

    upgrade)
        ant $TAG $DEBUG deploy:local:upgrade
        ;;

    install)
        ant $TAG $DEBUG deploy:local:install
        ;;

    deploy)
        ant $TAG $DEBUG deploy:hot
        ;;

    jsp)
        ant $DEBUG deploy:hot:jsp
        ;;

    clean)
        ant clean
        ;;

    *)
        echo "Invalid target '$1'."
        ;;
esac
