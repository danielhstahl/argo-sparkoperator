# spark on k8s

This runs XGBoost on spark, and saves the trained model

## prereqs
Minikube, helm

## create storage for spark
`mkdir /tmp/spark`
`cp iris.csv /tmp/spark`

## start minikube and set mount
`minikube start --memory 8192 --cpus 5 --mount-string /tmp/spark:/data --mount --kubernetes-version v1.15.3 # older k8s version for working with spark 2.4.5`

## Create registry to push images to
`minikube addons enable registry`
`docker run --rm -it --network=host alpine ash -c "apk add socat && socat TCP-LISTEN:5000,reuseaddr,fork TCP:$(minikube ip):5000"`

## build image include spark jar
`docker build . -t xgboost`
`docker tag xgboost localhost:5000/xgboost`
`docker push localhost:5000/xgboost`

## install spark operator for k8s
`kubectl create namespace spark-operator`
`helm install spark-operator incubator/sparkoperator --namespace spark-operator --set sparkJobNamespace=default,enableWebhook=true,operatorVersion=v1beta2-1.1.2-2.4.5`
`kubectl create serviceaccount spark`
`kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark`
`kubectl apply -f operator.yml`

## start k8s dashboard
`minikube dashboard # use this to monitor pods`


# argo

This schedules/orchestrates the xgboost job

## install helm chart
`kubectl create clusterrolebinding spark-admin --clusterrole=cluster-admin --serviceaccount=default:default --namespace=default`
`helm repo add argo https://argoproj.github.io/argo-helm`
`helm update`
`helm install argo-wf argo/argo -f argo.yml`

## allow dashboard
`kubectl port-forward deployment/argo-wf-server 2746:2746`

## submit job
`argo submit --watch argo_wf.yml`




