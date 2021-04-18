
# Honne Cloud Backup #

## Description ##
Performs backups from Directories, SQL Server, MySQL, Oracle, PostgreSQL and MongoDb and upload to AWS S3, Azure Blob or Google Cloud Storage

## Requirements ##
* [Java 8](https://www.java.com/es/download/help/java8.html)
* [AWS S3](https://aws.amazon.com/es/s3/)
* [Azure Blob](https://azure.microsoft.com/es-es/services/storage/blobs/)
* [Google Cloud Storage](https://cloud.google.com/storage?hl=es-419)

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
1.- Install java 8
2.- Build and copy the package
3.- Execute "java -jar HCB.jar configure" to create a configuration file
4.- Execute "java -jar HCB.jar start configuration_file" to perform backup

## How to use ##
~~~
java -jar HCB.jar
java -jar HCB.jar start
java -jar HCB.jar start hcb_mysql_s3.properties
java -jar HCB.jar configure
java -jar HCB.jar configure 8888
~~~

## Backup types ##
### MySQL Dump ###
For check mysqldump, execute.
~~~
mysqldump --help
~~~

### MySQL Pump ###
For check mysqlpump, execute.
~~~
mysqlpump --help
~~~

### Percona XtraBackup ###
For check innobackupex, execute.
~~~
innobackupex --help
~~~

### Oracle Data Pump ###
For check expdp, execute.
~~~
expdp help=y
~~~

### PostgreSQL PgDump ###
For check pg_dump, execute.
~~~
pg_dump --help
~~~

### MongoDump ###
For check mongodump, execute.
~~~
mongodump --help
~~~

## Notes ##
The default properties file is hcb.properties and the default port is 10080.

For MySQL backups must be accesible mysqldump, mysqlpump or innobackupex tools.

For Oracle backups must be accesible expdp (Oracle Data Pump).

For PostgreSQL backups must be accesible pg_dump tool.

For MongoDb backups must be accesible mongodump tool.

P.D. Let's go play !!!

