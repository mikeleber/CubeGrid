# CubeGrid
A distributed computing scenario

## Setup

## Execute examples

### Starting the hz cluster (ClusterName=dev)
```
hz start
hz start
```
### Starting the management console
```
hz mc start
# connect the mc to the cluster 'dev'
# http://localhost:8080
```

### Spring Boot Hazelcast Client

```
-Dserver.port=8090
-Dserver.port=8095
```
or as program argument
```
--server.port=8095
```

## 3rd Party Libs
### Togglz (java feature flag library)
https://www.togglz.org/ (since version 2.3.0)

https://www.togglz.org/apidocs/2.6.1.final/org/togglz/hazelcast/hazelcaststaterepository


## Reference
### Hazelcast documentation
https://docs.hazelcast.com/home/

https://docs.hazelcast.com/hazelcast/latest/getting-started/quickstart.html

publish/subscribe
https://docs.hazelcast.com/imdg/4.2/data-structures/topic

### DataTables (jQuery)

https://datatables.net/

### Maven Source Versioning Plugin
https://www.baeldung.com/spring-git-information