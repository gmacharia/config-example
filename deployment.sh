#!/bin/bash

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
