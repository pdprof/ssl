#!/bin/bash

# prereq : oc login is required to execuete this shell
#          trapit program and shell PATH environment variable to execute it.
#          kubeadmin
curl -O https://www.ibm.com/support/pages/system/files/support/swg/swgtech.nsf/0/d83af3cb5f0490d1852579d600618374/$FILE/trapit.002/trapit
if [ ! -f trapit ]; then
     echo "===================="
     echo "Please download trapit from https://www.ibm.com/support/pages/node/709009 and put it on the same directory."
     echo "====================" 
     exit 1
fi
chmod 755 trapit

$HOME/kubeadmin

oc registry login
docker login `oc registry info`

# pull openliberty docker repository
BUILD_DATE=`date +%Y%m%d`
docker pull openliberty/open-liberty:kernel-java8-openj9-ubi

docker build -t ssl:${BUILD_DATE} .

docker tag ssl:${BUILD_DATE} $(oc registry info)/$(oc project -q)/ssl:${BUILD_DATE}

docker push $(oc registry info)/$(oc project -q)/ssl:${BUILD_DATE}

sed -i s/image-registry.openshift-image-registry.svc:5000/default-route-openshift-image-registry.apps-crc.testing/g kubernetes.yaml
sed -i s/"\[project-name\]"/$(oc project -q)/g kubernetes.yaml
sed -i s/"\[build-date\]"/${BUILD_DATE}/g kubernetes.yaml

oc create secret generic docker-user-secret --from-file=.dockerconfigjson=$HOME/.docker/config.json --type=kubernetes.io/dockerconfigjson
oc apply -f kubernetes.yaml

