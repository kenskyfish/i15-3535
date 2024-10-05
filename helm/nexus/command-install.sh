helm install nexus sonatype/nexus-repository-manager \
--version 64.2.0 \
--set service.type=NodePort \
--namespace nexus --create-namespace
