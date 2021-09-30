#### Ensure you have docker,minikube and kubectl installed before running the below commands

## How to Setup

### Step 1 : Clone the Project

```
git clone https://github.com/gmacharia/kyosk-config.git
```

### Step 2: Enter into the project

```
cd kyosk-config
```

### Step 3: Run the Deployment Script

```
sh deployment.sh <docker_image_tag>
```
### Step 4:  Wait for the following output

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

### Step 5 : Get Namespaces  
```
kubectl get namespaces

NAME              STATUS   AGE
default           Active   24h
kube-node-lease   Active   24h
kube-public       Active   24h
kube-system       Active   24h
kyosk             Active   7h1m
```
### Step 6 : List Pods 
```
kubectl -n kyosk get pods

NAME                                    READY   STATUS              RESTARTS       AGE
kyosk-config-service-645b5c8db9-7w78j   1/1     Running             2 (125m ago)   166m
kyosk-config-service-744d98894f-pfvcd   0/1     ContainerCreating   0              2m51s

NAME                                    READY   STATUS    RESTARTS   AGE
kyosk-config-service-744d98894f-pfvcd   1/1     Running   0          6m55s
```

### Step 7 : View Logs
```
kubectl -n kyosk logs -f kyosk-config-service-744d98894f-pfvcd --since=2m

```
### Step 8 : Get external IP configured

```
minikube service -n kyosk  kyosk-config-service
 
|-----------|----------------------|-------------|---------------------------|
| NAMESPACE |         NAME         | TARGET PORT |            URL            |
|-----------|----------------------|-------------|---------------------------|
| kyosk     | kyosk-config-service |          80 | http://192.168.49.2:30298 |
|-----------|----------------------|-------------|---------------------------|

```

### Step 9 : Url to test are as below

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
## Pipeline was configured via git




