# SSL Application

## Requirements

- [Docker](https://www.docker.com/)

## Test on Docker

### Build docker image

```
setup-docker.sh
```

### Start docker 
```
mkdir -p ~/pdprof/share
docker run --rm -p 10080:10080 -v ~/pdprof:/pdprof ssl
```

Now you can access http://localhost:10080/ssl


## Test on OpenShift

After you setup CRC described at [icp4a-helloworld](https://github.com/pdprof/icp4a-helloworld)

You can use following script. 
```
setup-openshift.sh
```

Now you can access to http://db-connections-route-default.apps-crc.testing/top

Other test steps are same with docker.
