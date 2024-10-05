pipeline {
    agent {
        kubernetes {
            cloud 'Local Microk8s'
            label 'agent'
            namespace 'jenkins'
            slaveConnectTimeout 300
            idleMinutes 5
        }
    }

    stages {
        stage('Main') {
            steps {
                sh 'hostname'
            }
        }
    }
}
