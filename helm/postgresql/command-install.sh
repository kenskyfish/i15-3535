helm install harbor oci://registry-1.docker.io/bitnamicharts/postgresql \
--version 16.0.6 \
--set service.type=NodePort \
--namespace postgresql --create-namespace
