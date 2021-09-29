#!/bin/bash

baseJDKImage=amd64
applicationName=kyosk-config
applicationVersion=0.0.1

## Docker username and password
dockerUsername="gmasharia"
dockerPassword="7NzUmdDkVaCVX35n"

## Stage 1 - Build the Maven Build.
echo "We are creating the maven build"
## Clean the package.
mvn clean
## Package the jar and run tests.
mvn package

## Stage 2 - Build the docker image.
## Generate Random String - Use this as a commit id.
INPUT='00db253-365c-415c-86f7-503a35fafa58'
SUBSTRING=$(echo $INPUT| cut -c2-6)
## Lets also get the Architecture.
applicationArchitectureTag=""
if [ "$baseJDKImage" == 'openjdk:11.0.3-jdk' ]
then
  applicationArchitectureTag="amd64"
else
  applicationArchitectureTag="ppc64le"
fi

# shellcheck disable=SC2027
echo "docker build --build-arg BASE_IMAGE="$baseJDKImage" --build-arg APPLICATION_NAME="$applicationName-$applicationVersion" -t $dockerUsername/"$applicationName:v$applicationVersion-$SUBSTRING-$applicationArchitectureTag" "
docker build --build-arg BASE_IMAGE="$baseJDKImage" --build-arg APPLICATION_NAME="$applicationName-$applicationVersion" -t $dockerUsername/"$applicationName:v$applicationVersion-$SUBSTRING-$applicationArchitectureTag" .

## Docker login
docker login -u $dockerUsername -p $dockerPassword

## Push Image to the repository.
docker push $dockerUsername/$applicationName:v$applicationVersion-$SUBSTRING-$applicationArchitectureTag


## Deploy to Minikube.
## First We Create A Namespace.
kubectl create namespace kyosk

## Sleep for a few to make sure the pod is deployed
sleep 5m

## Now We deploy our service.
kubectl -n kyosk create deployment $applicationName --image=$dockerUsername/$applicationName:v$applicationVersion-$SUBSTRING-$applicationArchitectureTag

## Sleep for 5m to wait for pod to start running.
sleep 5m

## Expose the service using via loadBalancer IP.
## On Other clusters, the IP is generated as an external IP.
## On Minikube, the External IP will be marked as "Pending" and you should run "minikube service -n staging  kyosk-config-service" to expose the service externally.
kubectl -n kyosk expose deployment $applicationName --type=LoadBalancer --port=80 --target-port=8080
