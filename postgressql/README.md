# Postgresql  WildFly or WildFly bootable jar 
docker container PostgresSql
=====================================

Run container
```
docker run -d --rm=true --name postgresdb -e POSTGRES_USER=prai -e POSTGRES_PASSWORD=pwd -e POSTGRES_DB=myDB -p 5432:5432 postgres
```
# download connector
```
wget https://repo1.maven.org/maven2/org/postgresql/postgresql/42.2.9/postgresql-42.2.9.jar
```
## Creating the Datasource using the CLI
* module add --name=org.postgres --resources=<pathToJar>/postgresql-42.2.9.jar --dependencies=javax.api,javax.transaction.api

##  install the JDBC driver using the above defined module
* /subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)

## install the data source by using the data-source shortcut command,
* data-source add --jndi-name=java:jboss/datasources/PostGreDS --name=PostgrePool --connection-url=jdbc:postgresql://localhost:5432/postgres --driver-name=postgres --user-name=prai --password=pwd

## test databaseconnection 
* /subsystem=datasources/data-source=PostgrePool:test-connection-in-pool 
{ "outcome" => "success", "result" => [true] }

## source to add datasource within wildfly
* https://www.mastertheboss.com/jbossas/jboss-datasource/how-to-configure-a-datasource-with-jboss-7/
## build application using maven
* mvn clean package 



## Add a new task:
```
curl -X POST http://localhost:8080/<Context-root>/tasks/title/foo
```
## Get all the tasks:
```
curl [http://$(oc get route wf-postgresql-app --template='{{ .spec.host }}')](http://localhost:8080/<Context-root>/)
```