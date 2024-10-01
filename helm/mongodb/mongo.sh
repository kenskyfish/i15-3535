helm install mongodb oci://registry-1.docker.io/bitnamicharts/mongodb --namespace mongodb --create-namespace

# --set auth.rootPassword=apl30ekg
# helm uninstall mongodb --namespace mongodb
# kubectl delete namespace mongodb