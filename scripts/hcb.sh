#!/bin/bash

logFile=/home/centos/hcb/hcb_$(date +%d-%m-%Y).log
jarFile=/home/centos/hcb/HCB.jar
cnfFile=/home/centos/hcb/mysql.properties

java -Xms256M -Xmx1G -jar ${jarFile} start ${cnfFile} >> ${logFile}