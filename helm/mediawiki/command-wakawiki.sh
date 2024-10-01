helm install wakawiki oci://registry-1.docker.io/bitnamicharts/mediawiki \
--namespace wakawiki --create-namespace \
--set resourcesPreset=medium \
--set service.type=NodePort \
--set mediawikiName=Wakawiki \
--set mediawikiHost=wakawiki \
--set allowEmptyPassword=false \
# --set mediawikiUser=Admin,mediawikiPassword='apl30ekg' \
# --set readinessProbe.enabled=false