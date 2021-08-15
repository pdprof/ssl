# MustGather Hands-On SSL case

## Start ssl container on docker or openshift

Follow steps described at parent folder's README 


## Start stand alone java program to check server's port cipher

Access to the docker container as follows
```
docker exec -it <container_name> bash
java -jar /config/apps/pdprof.jar localhost 10443
```

Please check enabled ciphers from output


## Start stand alone java pgoram in a docker

Change directory to the directry which include pdprof.jar and issue following command.

```
docker run --rm -v `pwd`:/work ibmjava java -jar /work/pdprof.jar <host_ip_of_liberty> 10443
```
