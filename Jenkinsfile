pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Clonando repositorio..."
            }
        }

        stage('Build and Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_CREDENTIALS',
                    usernameVariable: 'NEXUS_USERNAME',
                    passwordVariable: 'NEXUS_PASSWORD'
                )]) {
                    configFileProvider([configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                        sh """
                            echo "Usando settings: \$MAVEN_SETTINGS"
                            mvn -s \$MAVEN_SETTINGS clean package deploy
                        """
                    }
                }
            }
        }

        // Puedes añadir más stages si lo necesitas:
        // stage('SonarQube Analysis') { ... }
        // stage('Build Docker Image') { ... }
        // stage('Push Docker Image to Nexus') { ... }
        // stage('Trigger Argo CD Sync') { ... }
    }

    post {
        always {
            echo 'Pipeline finalizado.'
        }
        success {
            echo '¡Build y deploy exitoso!'
        }
        failure {
            echo 'Error en el build o deploy.'
        }
    }
}
