# Kyosk Config Project Setup

## Please ensure you have the following installed before hand

    * docker
    * minikube
    * kubectl installed before running the below commands
    * git

## How to Setup

### Step 1 : Clone the Project

```
git clone https://github.com/gmacharia/kyosk-config.git
```

### Step 2: Access the project folder

```
cd kyosk-config
```

### Step 3: Checkout the master branch

```
git checkout master
```

### Step 4: Run the Deployment Script

```
sh deployment.sh <docker_image_tag>
```
### Step 5:  Wait for the following output

```
\n ********** BEGIN ********** \n
\n ********** NAMESPACE STATE CHECK ********** \n
\n ********** NAMESPACE kyosk EXISTS ********** \n
\n ********** COMPLETED NAMESPACE STATE CHECK ********** \n
\n ********** LOGIN TO DOCKER ********** \n
WARNING! Using --password via the CLI is insecure. Use --password-stdin.
WARNING! Your password will be stored unencrypted in /home/kobe/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
\n ********** DOCKER PULL IMAGE ********** \n

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





