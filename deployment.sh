#!/bin/bash
echo " \n ********** BEGIN DEPLOYMENT********** \n"

application=kyosk-config-service

docker_username=gmasharia
docker_image_tag=$1

namespace=kyosk

## Deploy to Minikube.
## First We Create A Namespace.

echo "\n ********** NAMESPACE STATE CHECK ********** \n"

elem=$(kubectl get ns $namespace -o=jsonpath='{.status.phase}')
      
if [ $elem == "Active" ] ; then
           
       echo "\n ********** NAMESPACE $namespace EXISTS ********** \n"
else
       echo "\n ********** CREATING NAMESPACE ********** \n"
       echo "kubectl create namespace kyosk"
       
       kubectl create namespace $namespace
            
fi

echo "\n ********** COMPLETED NAMESPACE STATE CHECK ********** \n"

## Sleep for a few to make sure the pod is deployed
sleep 10s

## Now We deploy our service.

echo "\n ********** DOCKER PULL IMAGE ********** \n"


echo "\n  docker pull $docker_username/$application:$docker_image_tag \n"

docker pull $docker_username/$application:$docker_image_tag

echo " \n ********** DOCKER PULL COMPLETE ********** \n"


echo "\n ********** CHECK DEPLOYMENT SERVICE ********** \n"


elem2=$(kubectl get deployment -n kyosk $application | tail -n +2 | awk '{print $1}')

if [ $elem2 == $application ] ; then
           
        echo "\n ********** DEPLOYMENT $application EXISTS REQUIRES UPDATE ********** \n"

        echo " \n kubectl  -n $namespace set image deployment/kyosk-config-service $application=$docker_username/kyosk-config-service:$docker_image_tag \n"

        kubectl  -n $namespace set image deployment/$application $application=$docker_username/$application:$docker_image_tag 

else
	
		echo "\n ********** CREATING DEPLOYMENT ********** \n"
	    
	    echo "\n kubectl -n kyosk create deployment $application --image=$docker_username/$application:$docker_image_tag \n"

	    kubectl -n kyosk create deployment $application --image=$docker_username/$application:$docker_image_tag
    
fi

echo "\n ********** CHECK DEPLOYMENT SERVICE COMPLETE ********** \n"


## Sleep for 5s to wait for pod to start running.
sleep 10s

## Expose the service using via loadBalancer IP.
## On Other clusters, the IP is generated as an external IP.
## On Minikube, the External IP will be marked as "Pending" and you should run "minikube service -n kyosk  kyosk-config-service" to expose the service externally.

echo " \n ********** EXPOSING SERVICE VIA LOADBALANCER ********** \n"


elem3=$(kubectl get services $application -n $namespace| tail -n +2 | awk '{print $1}')


if [ $elem3 == $application ] ; then
           
        echo "\n ********** LOADBALANCER $application EXISTS DOES NOT REQUIRE CREATION ********** \n"

else
	
		echo "\n ********** EXPOSE SERVICE VIA LOADBALANCER  ********** \n"
	    
	    echo "\n kubectl -n kyosk expose deployment $application --type=LoadBalancer --port=80 --target-port=8080 \n"

	    kubectl -n kyosk expose deployment $application --type=LoadBalancer --port=80 --target-port=8080
    
fi

echo " \n ********** EXPOSING SERVICE VIA LOADBALANCER COMPLETE ********** \n"


echo "\n  ********** DEPLOYMENT COMPLETE ********** \n"
