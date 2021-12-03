# Honne Cloud Backup #

## Description ##
Performs backups from Directories, SQL Server, MySQL, Oracle, PostgreSQL and MongoDb and upload to AWS S3, Azure Blob or Google Cloud Storage

## Requirements ##
* [Java 8](https://www.java.com/download/help/java8.html)
* [AWS S3](https://aws.amazon.com/s3/)
* [Azure Blob](https://azure.microsoft.com/en-us/services/storage/blobs/)
* [Google Cloud Storage](https://cloud.google.com/storage)

## Build ##
Execute
~~~
mvn package
~~~

## Documentation ##
Execute
~~~
mvn javadoc:javadoc
~~~

## Installation ##
~~~
1.- Install java 8
2.- Build and copy the package
3.- Execute "java -jar HCB.jar configure" to create a configuration file
4.- Execute "java -jar HCB.jar start configuration_file.properties" to perform backup
~~~

## How to use ##
~~~
java -jar HCB.jar
java -jar HCB.jar start
java -jar HCB.jar start hcb_mysql_s3.properties
java -jar HCB.jar configure
java -jar HCB.jar configure 8888
~~~

## Configuration ##
####  Configuration database example #####
![Database configuration](https://raw.githubusercontent.com/yorch81/hcb/e62d184a90e7d64ba1bcf47d289146bae382fcb4/img/configdb.PNG)

#### AWS S3 configuration #####
![aws s3 configuration](https://raw.githubusercontent.com/yorch81/hcb/master/img/s3config.PNG)

#### Create access and secret key ####
https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html#Using_CreateAccessKey

#### Azure Blob configuration #####
![azure blob configuration](https://raw.githubusercontent.com/yorch81/hcb/master/img/azconfig.PNG)

#### Get Azure Blob keys ####
https://docs.microsoft.com/en-us/azure/storage/common/storage-account-keys-manage?tabs=azure-portal

#### Google Cloud Storage configuration #####
![gcs configuration](https://raw.githubusercontent.com/yorch81/hcb/master/img/gcpconfig.PNG)

#### Create Google Cloud Storage credentials ####
https://cloud.google.com/docs/authentication/getting-started

## Write batch script ##
~~~
SET mydate=%DATE%
SET mydate=%mydate:/=-%

SET logFile=C:\Apps\hcb\hcb_%mydate%.log
SET jarFile=C:\Apps\hcb\HCB.jar
SET cnfFile=C:\Apps\hcb\sqlserver.properties

java -Xms256M -Xmx1G -jar %jarFile% start %cnfFile% >> %logFile%
~~~

## Write bash script ##
~~~
#!/bin/bash

logFile=/home/centos/hcb/hcb_$(date +%d-%m-%Y).log
jarFile=/home/centos/hcb/HCB.jar
cnfFile=/home/centos/hcb/mysql.properties

java -Xms256M -Xmx1G -jar ${jarFile} start ${cnfFile} >> ${logFile}
~~~

## Backup types ##
### MySQL Dump ###
For check mysqldump, execute.
~~~
mysqldump --help
~~~
https://dev.mysql.com/doc/refman/5.7/en/mysqldump.html

### MySQL Pump ###
For check mysqlpump, execute.
~~~
mysqlpump --help
~~~
https://dev.mysql.com/doc/refman/5.7/en/mysqlpump.html

### Percona XtraBackup 2.4 ###
For check innobackupex, execute.
~~~
innobackupex --help
~~~
https://www.percona.com/doc/percona-xtrabackup/2.4/index.html

### Oracle Data Pump ###
For check expdp, execute.
~~~
expdp help=y
~~~
https://oracle-base.com/articles/10g/oracle-data-pump-10g

### PostgreSQL PgDump ###
For check pg_dump, execute.
~~~
pg_dump --help
~~~
https://www.postgresql.org/docs/10/app-pgdump.html

### MongoDump ###
For check mongodump, execute.
~~~
mongodump --help
~~~
https://docs.mongodb.com/v3.6/reference/program/mongodump/

### Percona XtraBackup 8-0 ###
For check xtrabackup, execute.
~~~
xtrabackup --help
~~~
https://www.percona.com/doc/percona-xtrabackup/8.0/index.html

## Notes ##
The default properties file is hcb.properties and the default port is 10080.

For MySQL backups must be accesible mysqldump, mysqlpump, innobackupex or xtrabackup tools.

For Oracle backups must be accesible expdp (Oracle Data Pump).

For PostgreSQL backups must be accesible pg_dump tool.

For MongoDb backups must be accesible mongodump tool.

P.D. Let's go play !!!

