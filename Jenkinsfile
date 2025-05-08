pipeline {
    agent any // O un agente específico si tienes configurados

    tools {
        // Usa el nombre de la configuración de Maven que hiciste en Global Tool Configuration
        maven 'Maven'
    }

    environment {
        // Referencia a las credenciales de Nexus que añadiste en Jenkins
        NEXUS_CREDENTIALS = credentials('NEXUS_CREDENTIALS')
        // Extrae usuario y contraseña de las credenciales para usarlos en settings.xml
        NEXUS_USERNAME = NEXUS_CREDENTIALS.username
        NEXUS_PASSWORD = NEXUS_CREDENTIALS.password
    }

    stages {
        stage('Checkout') {
            steps {
                // El plugin de Git ya maneja esto si configuraste SCM en la configuración del job
                script {
                    echo "Clonando repositorio..."
                    // checkout scm // descomentar si no usas SCM en la configuración del job
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "Construyendo proyecto Maven..."
                    // Ejecuta el build de Maven, usando el settings.xml gestionado
                    // El -s especifica el archivo settings.xml
                    // El -Dmaven.test.skip=true es opcional si quieres saltar tests
                    // El clean package install deploy construye, instala localmente y despliega a Nexus
                    sh "mvn -s ${JENKINS_HOME}/config/managed-files/nexus-settings.xml clean package deploy"
                }
            }
        }

        // Puedes añadir más stages aquí, como:
        // stage('SonarQube Analysis') { ... }
        // stage('Build Docker Image') { ... }
        // stage('Push Docker Image to Nexus') { ... }
        // stage('Trigger Argo CD Sync') { ... }
    }

    // Opcional: Configurar acciones post-build (notificaciones, limpieza, etc.)
    // post {
    //     always {
    //         echo 'Pipeline finished.'
    //     }
    //     success {
    //         echo 'Build successful!'
    //     }
    //     failure {
    //         echo 'Build failed!'
    //     }
    // }
}