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
* kubectl -n argo port-forward deployment/argo-wf-server 2746:2746
* argo submit --watch argo_wf.yml




