# spark on k8s

* minikube start --memory 8192 --cpus 5
* helm install incubator/sparkoperator --generate-name --skip-crds --set serviceAccounts.spark.name=spark
* kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark
* kubectl apply -f operator.yml
* minikube dashboard


# argo
* kubectl create clusterrolebinding spark-admin --clusterrole=cluster-admin --serviceaccount=default:default --namespace=default
* helm repo add argo https://argoproj.github.io/argo-helm
* helm update
* helm install argo-wf argo/argo -f argo.yml
* kubectl port-forward deployment/argo-wf-server 2746:2746
* kubectl apply -f persistent_storage.yml
* mkdir /tmp/spark
* cp iris.csv /tmp/spark
* docker build . -t xgboost
* argo submit --watch argo_wf.yml




