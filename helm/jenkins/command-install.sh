helm install jenkins oci://registry-1.docker.io/bitnamicharts/jenkins \
--version 13.4.23 \
--set service.type=NodePort \
--namespace bogus --create-namespace