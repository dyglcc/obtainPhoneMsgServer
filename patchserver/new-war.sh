#!/usr/bin/env bash
# no use now 2020.4.11
if [ -z $1 ];then
	echo '1.0.1'
	exit -1
fi

./gradlew war

if [ ! -d ../war/$1 ];then
    mkdir ../war/$1
fi
cp patchserver-facade/build/libs/hotfix-apis.war ../war/$1/
cp patchserver-manager/build/libs/hotfix-console.war ../war/$1/

cp patchserver-facade/war-war/hotfix-apis.properties ../war/$1/
cp patchserver-manager/war-war/hotfix-console.properties ../war/$1/

cp README.md ../war/$1/

cp import.sql ../war/$1/