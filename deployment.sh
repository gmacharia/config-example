#!/bin/bash
echo " ********** BEGIN ********** "

application=kyosk-config-service

docker_username=$1
docker_password=$2
docker_image_tag=$3

## Deploy to Minikube.
## First We Create A Namespace.
echo " ********** creating namespace ********** "
kubectl create namespace kyosk

## Sleep for a few to make sure the pod is deployed
sleep 10s

## Now We deploy our service.

echo " ********** login to docker ********** "

docker login -u $docker_username -p $docker_password

echo " ********** docker pull image ********** "

docker pull $docker_username/$application:$docker_image_tag

echo " ********** deploying service ********** "

kubectl -n kyosk create deployment $application --image=$docker_username/$application:$docker_image_tag

## Sleep for 5m to wait for pod to start running.
sleep 20s

## Expose the service using via loadBalancer IP.
## On Other clusters, the IP is generated as an external IP.
## On Minikube, the External IP will be marked as "Pending" and you should run "minikube service -n staging  kyosk-config-service" to expose the service externally.
echo " ********** exposing service via loadbalancer ********** "
kubectl -n kyosk expose deployment $application --type=LoadBalancer --port=80 --target-port=8080

echo " ********** COMPLETE ********** "


