# Jakarta EE 10 REST CRUD Example

Build and run
=============

* To build: `mvn package`
* To run: `mvn wildfly-jar:run`
* Access the application: `http://127.0.0.1:8080/hello`
  
This example shows how to build and test a REST Application using Jakarta EE 10

In order to test the application you can use the following sample REST script:

----
curl -X POST http://localhost:8080/jaxrs-demo/rest/customers  -H 'Content-Type: application/json' -d '{"name":"frank","surname":"marchioni"}'

curl http://localhost:8080/jaxrs-demo/rest/customers/all

curl -X DELETE http://localhost:8080/jaxrs-demo/rest/customers?id=3


----

If you are using Postman for your tests, within the `Postman` folder there's a Collection script to test the application from Postman.
