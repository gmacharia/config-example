# Kyosk Config Project Setup

*Please note the docker image build was automated by the Github Actions/Workflows*
## Please ensure you have the following installed before hand.
    - docker
    - minikube
    - kubectl installed before running the below commands
    - git
## How to Setup
### Step 1 : Clone the Project
```
git clone https://github.com/gmacharia/kyosk-config.git
```
### Step 2 : Access the project folder
```
cd kyosk-config
```
### Step 3 : Checkout the master branch
```
git checkout master
```
### Step 4 : Run the Deployment Script
```
sh deployment.sh <docker_image_tag>
```
### Step 5 :  Wait for the following output
```
********** BEGIN DEPLOYMENT **********

********** NAMESPACE STATE CHECK **********

Error from server (NotFound): namespaces "kyosk" not found
deployment.sh: line 18: [: ==: unary operator expected

********** CREATING NAMESPACE **********

kubectl create namespace kyosk
namespace/kyosk created

********** COMPLETED NAMESPACE STATE CHECK **********

********** DOCKER PULL IMAGE **********

docker pull gmasharia/kyosk-config-service:v1.0.0-b1e6ec-amd64

v1.0.0-b1e6ec-amd64: Pulling from gmasharia/kyosk-config-service
Digest: sha256:5a364f424d91d77273607ef964cc94dcfdba12a608e2c378801a0129ce6b5e3d
Status: Image is up to date for gmasharia/kyosk-config-service:v1.0.0-b1e6ec-amd64
docker.io/gmasharia/kyosk-config-service:v1.0.0-b1e6ec-amd64

********** LOGIN TO DOCKER COMPLETE **********

********** CHECK DEPLOYMENT SERVICE **********

Error from server (NotFound): deployments.apps "kyosk-config-service" not found
deployment.sh: line 51: [: ==: unary operator expected

********** CREATING DEPLOYMENT **********

kubectl -n kyosk create deployment kyosk-config-service --image=gmasharia/kyosk-config-service:v1.0.0-b1e6ec-amd64

deployment.apps/kyosk-config-service created

********** CHECK DEPLOYMENT SERVICE COMPLETE **********

********** EXPOSING SERVICE VIA LOADBALANCER **********

Error from server (NotFound): services "kyosk-config-service" not found
deployment.sh: line 85: [: ==: unary operator expected

********** EXPOSE SERVICE VIA LOADBALANCER  **********

kubectl -n kyosk expose deployment kyosk-config-service --type=LoadBalancer --port=80 --target-port=8080

service/kyosk-config-service exposed

********** EXPOSING SERVICE VIA LOADBALANCER COMPLETE **********

********** DEPLOYMENT COMPLETE **********


```

### Step 6 : Get Namespaces  
```
kubectl get namespaces

NAME              STATUS   AGE
default           Active   24h
kube-node-lease   Active   24h
kube-public       Active   24h
kube-system       Active   24h
kyosk             Active   7h1m
```
### Step 7 : List Pods 
```
kubectl -n kyosk get pods

NAME                                    READY   STATUS              RESTARTS       AGE
kyosk-config-service-645b5c8db9-7w78j   1/1     Running             2 (125m ago)   166m
kyosk-config-service-744d98894f-pfvcd   0/1     ContainerCreating   0              2m51s

NAME                                    READY   STATUS    RESTARTS   AGE
kyosk-config-service-744d98894f-pfvcd   1/1     Running   0          6m55s
```
### Step 8 : View Logs
```
kubectl -n kyosk logs -f kyosk-config-service-744d98894f-pfvcd --since=2m
```
### Step 9 : Get external IP configured
```
minikube service -n kyosk  kyosk-config-service
|-----------|----------------------|-------------|---------------------------|
| NAMESPACE |         NAME         | TARGET PORT |            URL            |
|-----------|----------------------|-------------|---------------------------|
| kyosk     | kyosk-config-service |          80 | http://192.168.49.2:30298 |
|-----------|----------------------|-------------|---------------------------|
```
### Step 10 : Url to test are as below
```
Create: http://192.168.49.2:30298/kyosk/api/configs/create
Update: http://192.168.49.2:30298/kyosk/api/configs/update
Delete: http://192.168.49.2:30298/kyosk/api/configs/datacenter-3
Fetch Specific: http://192.168.49.2:30298/kyosk/api/search?metadata.monitoring.enabled=true
Fetch All: http://192.168.49.2:30298/kyosk/api/configs
```
### Sample Payload
```
{
    "name": "datacenter-3",
    "metadata": {
        "monitoring": {
            "enabled": "false"
        },
        "limits": {
            "cpu": {
                "enabled": "false",
                "value": "700m"
            }
        }
    }
}
```





