pipeline {
    agent {
        kubernetes {
            showRawYaml 'false'
            retries 2
            label 'busybox'
        }
    }
    stages {
        stage('Main') {
            steps {
                sh 'hostname'
                sh 'sleep 60'
            }
        }
    }
}
