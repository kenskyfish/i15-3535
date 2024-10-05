pipeline {
    agent {
        label '!bad'
    }

    environment {
        HEAVY_ARTILLERY = 'false'
    }
    
    stages {
        stage('Initialize') {
            steps {
                echo 'Placeholder.'
            }
        }
    }
}
