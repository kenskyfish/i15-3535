export MEDIAWIKI_PASSWORD=$(kubectl get secret --namespace "ikenwiki" ikenwiki-mediawiki -o jsonpath="{.data.mediawiki-password}" | base64 -d)
export MARIADB_ROOT_PASSWORD=$(kubectl get secret --namespace "ikenwiki" ikenwiki-mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)
export MARIADB_PASSWORD=$(kubectl get secret --namespace "ikenwiki" ikenwiki-mariadb -o jsonpath="{.data.mariadb-password}" | base64 -d)
helm upgrade ikenwiki oci://registry-1.docker.io/bitnamicharts/mediawiki \
--namespace ikenwiki --create-namespace \
--set resourcesPreset=small \
--set service.type=NodePort \
--set mediawikiName="Iken Pages" \
--set mediawikiHost=ikenwiki \
--set allowEmptyPassword=false \
--set mediawikiPassword=$MEDIAWIKI_PASSWORD \
--set mariadb.auth.rootPassword=$MARIADB_ROOT_PASSWORD \
--set mariadb.auth.password=$MARIADB_PASSWORD \
# --set mediawikiUser=Admin,mediawikiPassword='apl30ekg' \
# --set readinessProbe.enabled=false
