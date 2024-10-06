pipeline {
    agent {
        kubernetes {
            showRawYaml 'false'
            retries 0
            inheritFrom 'k8s'
        }
    }
    stages {
        stage('Scale Pods') {
            steps {
                container('k8s') {
                    withKubeConfig([credentialsId: 'Kubeconfig001']) {
                        script {
                            if (env.UP == 'true') {
                                sh 'kubectl scale deployments -n $NAMESPACE --replicas=1 --all'
                                if ((env.NAMESPACE.indexOf('wiki') > -1) || (env.NAMESPACE.indexOf('monitoring') > -1) || (env.NAMESPACE.indexOf('harbor') > -1)) {
                                    sh 'kubectl scale statefulsets -n $NAMESPACE --replicas=1 --all'
                                }
                                if (env.NAMESPACE.indexOf('monitoring') > -1) {
                                    sh 'kubectl -n monitoring patch daemonset stack-prometheus-node-exporter --type json -p=\'[{\"op\": \"remove\", \"path\": \"/spec/template/spec/nodeSelector/non-existing\"}]\''
                                }
                        } else {
                                sh 'kubectl scale deployments -n $NAMESPACE --replicas=0 --all'
                                if ((env.NAMESPACE.indexOf('wiki') > -1) || (env.NAMESPACE.indexOf('monitoring') > -1) || (env.NAMESPACE.indexOf('harbor') > -1)) {
                                    sh 'kubectl scale statefulsets -n $NAMESPACE --replicas=0 --all'
                                }
                                if (env.NAMESPACE.indexOf('monitoring') > -1) {
                                    sh 'kubectl -n monitoring patch daemonset stack-prometheus-node-exporter -p \'{\"spec\": {\"template\": {\"spec\": {\"nodeSelector\": {\"non-existing\": \"true\"}}}}}\''
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
