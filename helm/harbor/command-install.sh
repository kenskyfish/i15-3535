helm install harbor oci://registry-1.docker.io/bitnamicharts/harbor \
--version 24.0.0 \
--set service.type=NodePort \
--namespace harbor --create-namespace
