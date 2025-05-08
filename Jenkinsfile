pipeline {
    agent any // O un agente específico si tienes configurados

    tools {
        // Asegúrate de que 'Maven' aquí coincide exactamente con el 'Name' en Global Tool Configuration
        maven 'Maven'
    }

    // Eliminamos las asignaciones directas de credenciales aquí
    // environment {
    //     NEXUS_CREDENTIALS = credentials('nexus-credentials')
    //     NEXUS_USERNAME = NEXUS_CREDENTIALS.username // Incorrecto aquí
    //     NEXUS_PASSWORD = NEXUS_CREDENTIALS.password // Incorrecto aquí
    // }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Clonando repositorio..."
                    // El plugin de Git ya maneja esto si configuraste SCM en la configuración del job
                    // checkout scm // descomentar si no usas SCM en la configuración del job
                }
            }
        }

        stage('Build and Deploy to Nexus') { // Renombrado para mayor claridad
            steps {
                script {
                    echo "Construyendo y desplegando a Nexus..."
                    // Usamos withCredentials para exponer las credenciales como variables de entorno
                    withCredentials([usernamePassword(credentialsId: 'NEXUS_CREDENTIALS', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        // Dentro de este bloque, NEXUS_USERNAME y NEXUS_PASSWORD están disponibles como variables de entorno

                        // Ejecuta el build de Maven, usando el settings.xml gestionado
                        // El -s especifica el archivo settings.xml
                        // El -Dmaven.test.skip=true es opcional si quieres saltar tests
                        // El clean package install deploy construye, instala localmente y despliega a Nexus
                        // Las variables de entorno NEXUS_USERNAME y NEXUS_PASSWORD serán usadas por Maven a través del settings.xml
                        sh "mvn -s ${JENKINS_HOME}/config/managed-files/nexus-settings.xml clean package deploy"
                    }
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
