# MustGather Hands-On SSL case

## Start db-connections container on docker or openshift

Follow steps described at parent folder's README 


## Start stand alone java program to check server's port cipher

Access to the docker container as follows
```
docker exec -it <container_name> bash
java -jar /config/apps/pdprof.jar localhost 10443
```

Please check enabled ciphers from output
