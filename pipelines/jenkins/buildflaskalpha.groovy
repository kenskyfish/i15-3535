pipeline {
    environment {
        REGISTRY = 'core.harbor.domain'
        PROJECT = 'i15-3535'
        REPO = 'flask-alpha'
    }
    agent {
        kubernetes {
            showRawYaml 'false'
            retries 2
            inheritFrom 'docker'
        }
    }
    stages {
        stage('Clone main branch of kenskyfish/$REPO') {
            steps {
                container('dind') {
                    sh 'git clone https://github.com/kenskyfish/$REPO.git'
                }
            }
        }
        stage('Build docker image') {
            steps {
                container('dind') {
                    dir("$REPO") {
                        sh 'docker build -t $REGISTRY/$PROJECT/$REPO .'
                    }
                }
            }
        }
        stage('Push image to registry') {
            steps {
                container('dind') {
                    sh 'echo 10.152.183.183 $REGISTRY >> /etc/hosts'
                    sh 'mkdir -p /etc/docker/certs.d/$REGISTRY'
                    sh 'wget --no-check-certificate -O /etc/docker/certs.d/$REGISTRY/ca.crt https://$REGISTRY/api/v2.0/systeminfo/getcert'
                    withCredentials([usernamePassword(credentialsId: 'fc4575d0-ccb8-410e-a3ea-3ccb0d4bff0a', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh 'docker login --username=$USERNAME --password=$PASSWORD $REGISTRY'
                    }
                    sh 'docker push $REGISTRY/$PROJECT/$REPO'
                }
            }
        }
    }
}
