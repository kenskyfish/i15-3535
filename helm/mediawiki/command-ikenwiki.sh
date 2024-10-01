helm install ikenwiki oci://registry-1.docker.io/bitnamicharts/mediawiki \
--namespace ikenwiki --create-namespace \
--set resourcesPreset=small \
--set service.type=NodePort \
--set mediawikiName="Iken Pages" \
--set mediawikiHost=ikenwiki \
--set allowEmptyPassword=false \
# --set mediawikiUser=Admin,mediawikiPassword='apl30ekg' \
# --set readinessProbe.enabled=false