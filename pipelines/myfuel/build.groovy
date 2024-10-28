pipeline {
    environment {
        REGISTRY = 'europe-north1-docker.pkg.dev'
        PROJECT = 'olivefoxdev'
        REPO = 'myfueltest/myfuelapi'
        BRANCH = 'main'
        GITHUBURL = 'git@github.com:kpillamyfuelai/myfuelai_api.git'
        PATH = '/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/root/google-cloud-sdk/bin'
    }
    agent {
        kubernetes {
            showRawYaml 'true'
            retries 2
            inheritFrom 'docker'
        }
    }
    stages {
        stage('enhance image for GCP use') {
            steps {
                container('dind') {
                    sh 'apk update'
                    sh 'apk add curl bash python3'
                    sh 'curl -sSL https://sdk.cloud.google.com | bash'
                }
            }
        }
        stage('Authenticate with GCP') {
            steps {
                container('dind') {
                    withCredentials([file(credentialsId: 'gcp-creds-olivefoxdev-admin-acc83a792aa6', variable: 'GCP_CREDS')]) {
                        sh 'gcloud auth login --cred-file=$GCP_CREDS'
                        sh 'gcloud auth configure-docker europe-north1-docker.pkg.dev'
                    }
                }
            }
        }
        stage('Clone branch of repo') {
            steps {
                container('dind') {
                    checkout([$class: 'GitSCM', branches: [[name: "$BRANCH" ]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: '']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'SSHkey-kmarshatmyfuel', url: "$GITHUBURL" ]] ])
                }
            }
        }
        stage('Build docker image') {
            steps {
                script {
                    now = new Date()
                    timestamp = now.format('yyyyMMdd-HHmmss', TimeZone.getTimeZone('UTC'))
                }
                container('dind') {
                    dir('myfuel') {
                        sh 'docker build -t $REGISTRY/$PROJECT/$REPO .'
                        sh 'docker push $REGISTRY/$PROJECT/$REPO'
                        sh 'docker tag $REGISTRY/$PROJECT/$REPO:latest $REGISTRY/$PROJECT/$REPO:$timestamp'
                        sh 'docker push $REGISTRY/$PROJECT/$REPO:$timestamp'
                    }
                }
            }
        }
    }
}
