#!/bin/bash
screen -wipe
screen -X -S platform-system-provider-service quit;
screen -X -S platform-web-service quit;
screen -dmS platform-system-provider-service java -Xms128m -Xmx256m -jar platform-system-provider.jar --spring.profiles.active=dev;
