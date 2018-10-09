#!/bin/bash

grepFlag='platform-mobile-service'
ID=`ps -ef | grep "$grepFlag" | grep -v "grep" | awk '{print $2}'`
echo $ID
echo "---------------"
for id in $ID
do
kill -9 $id
echo "killed $id"
done
echo "---------------"

screen -wipe
screen -dmS platform-mobile-service java -Xms128m -Xmx256m -jar platform-mobile.jar --spring.profiles.active=dev;
