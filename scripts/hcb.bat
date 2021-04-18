SET fecha=%DATE%
SET fecha=%fecha:/=-%

SET logFile=C:\Apps\hcb\hcb_%fecha%.log
SET jarFile=C:\Apps\hcb\HCB.jar
SET cnfFile=C:\Apps\hcb\sqlserver.properties

java -Xms256M -Xmx1G -jar %jarFile% start %cnfFile% >> %logFile%