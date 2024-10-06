pipeline {
    environment {
        REGISTRY = 'core.harbor.domain'
        PROJECT = 'i15-3535'
        REPO = 'flask-alpha'
    }
    agent {
        kubernetes {
            showRawYaml 'false'
            retries 0
            inheritFrom 'k8s'
        }
    }
    stages {
        stage('Clone main branch of kenskyfish/$REPO') {
            steps {
                sh 'git clone https://github.com/kenskyfish/$REPO.git'
            }
        }
        stage('Delete existing namespace') {
            steps {
                container('k8s') {
                    script {
                        try {
                            withKubeConfig([credentialsId: 'Kubeconfig001']) {
                                sh 'kubectl delete namespace $REPO'
                            }
                        } catch (err) {
                            echo err.getMessage()
                        }
                    }
                }
            }
        }
        stage('Apply deployment YAML') {
            steps {
                container('k8s') {
                    withKubeConfig([credentialsId: 'Kubeconfig001']) {
                        sh 'kubectl apply -f $REPO/kubernetes/nodeport.yaml'
                    }
                }
            }
        }
        stage('Get the Service IP Address') {
            steps {
                container('k8s') {
                    withKubeConfig([credentialsId: 'Kubeconfig001']) {
                        sh 'kubectl get services -n $REPO'
                    }
                }
            }
        }
    }
}

