helm install prometheus oci://registry-1.docker.io/bitnamicharts/prometheus \
--namespace monitoring --create-namespace \
--set server.service.type=NodePort \
--set alertmanager.service.type=NodePort
helm install kube-prometheus --namespace monitoring --create-namespace oci://registry-1.docker.io/bitnamicharts/kube-prometheus