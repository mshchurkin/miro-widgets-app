# Widget REST API

Widget management REST API

**Complications made**:

* Pagination using spring-data-commons;
* SQL database: H2 

Filtering with complexity less than O(n) can be done with TreeMap but was not implemented 

## How-to

Build:

    mvn clean install
Run with in memory storage

    mvn spring-boot:run -Drun.profiles=memory
Run with sql storage

    $mvn spring-boot:run -Drun.profiles=db
API ENDPOINT:

    http://localhost:8080/api/widgets
Also swagger was added for preety interface, located here:

    http://localhost:8080/swagger-ui/