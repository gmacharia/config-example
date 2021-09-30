### Ensure you have docker,minikube and kubectl installed before running the below commands
### steps
1. git clone https://github.com/gmacharia/kyosk-config.git
2. Run the pipeline<sh deployment.sh openjdk:11.0.3-jdk <your-docker-username> <your-docker-password> <image tag>>
Expected output

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

kubectl get namespaces

NAME              STATUS   AGE
default           Active   24h
kube-node-lease   Active   24h
kube-public       Active   24h
kube-system       Active   24h
kyosk             Active   7h1m


kubectl -n kyosk get pods

NAME                                    READY   STATUS              RESTARTS       AGE
kyosk-config-service-645b5c8db9-7w78j   1/1     Running             2 (125m ago)   166m
kyosk-config-service-744d98894f-pfvcd   0/1     ContainerCreating   0              2m51s

NAME                                    READY   STATUS    RESTARTS   AGE
kyosk-config-service-744d98894f-pfvcd   1/1     Running   0          6m55s

How to tail logs
kubectl -n kyosk logs -f kyosk-config-service-744d98894f-pfvcd --since=2m

How to get external IP configured
minikube service -n kyosk  kyosk-config-service

|-----------|----------------------|-------------|---------------------------|
| NAMESPACE |         NAME         | TARGET PORT |            URL            |
|-----------|----------------------|-------------|---------------------------|
| kyosk     | kyosk-config-service |          80 | http://192.168.49.2:30298 |
|-----------|----------------------|-------------|---------------------------|


Url to test are as below

Create: http://192.168.49.2:30298/kyosk/api/configs/create
Update: http://192.168.49.2:30298/kyosk/api/configs/update
Delete: http://192.168.49.2:30298/kyosk/api/configs/datacenter-3
Fetch Specific: http://192.168.49.2:30298/kyosk/api/search?metadata.monitoring.enabled=true
Fetch All: http://192.168.49.2:30298/kyosk/api/configs

Payload

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

##Pipeline was configured via git




