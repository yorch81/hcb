SET mydate=%DATE%
SET mydate=%mydate:/=-%

SET logFile=C:\Apps\hcb\hcb_%mydate%.log
SET jarFile=C:\Apps\hcb\HCB.jar
SET cnfFile=C:\Apps\hcb\sqlserver.properties

java -Xms256M -Xmx1G -jar %jarFile% start %cnfFile% >> %logFile%