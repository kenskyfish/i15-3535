# export JENKINS_AUTH=user:mypassword
echo File to lint: $1
curl -X POST --insecure --user "$JENKINS_AUTH" -F "jenkinsfile=<$1" "https://jenkins/pipeline-model-converter/validate"