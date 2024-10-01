helm install novawiki oci://registry-1.docker.io/bitnamicharts/mediawiki \
--namespace novawiki --create-namespace \
--set resourcesPreset=small \
--set service.type=NodePort \
--set mediawikiName=Novawiki \
--set mediawikiHost=novawiki \
--set allowEmptyPassword=false \
# --set mediawikiUser=Admin,mediawikiPassword='apl30ekg' \
# --set readinessProbe.enabled=false
