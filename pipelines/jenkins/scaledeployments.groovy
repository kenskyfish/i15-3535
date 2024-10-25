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
                            replicas = '0'
                            deployment = 'true'
                            statefulset = 'false'
                            monitoring = 'false'
                            if ((env.NAMESPACE.indexOf('wiki') > -1) || (env.NAMESPACE.indexOf('harbor') > -1) || (env.NAMESPACE.indexOf('monitoring') > -1)) {
                                statefulset = 'true'
                            }
                            if ((env.NAMESPACE.indexOf('postgresql') > -1) ) {
                                deployment = 'false'
                                statefulset = 'true'
                            }
                            if (env.NAMESPACE.indexOf('monitoring') > -1) {
                                monitoring = 'true'
                            }
                            if (env.UP == 'true') {
                                replicas = '1'
                            }
                            if (deployment == 'true') {
                                sh "kubectl scale deployments -n $NAMESPACE --replicas=$replicas --all"
                            }
                            if (statefulset == 'true') {
                                sh "kubectl scale statefulsets -n $NAMESPACE --replicas=$replicas --all"
                            }
                            if (monitoring == 'true') {
                                if (replicas == '1') {
                                    sh 'kubectl -n monitoring patch daemonset stack-prometheus-node-exporter --type json -p=\'[{\"op\": \"remove\", \"path\": \"/spec/template/spec/nodeSelector/non-existing\"}]\''
                                } else {
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
