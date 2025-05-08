pipeline {
    agent any // O un agente específico si tienes configurados

    tools {
        // Asegúrate de que 'Maven' aquí coincide exactamente con el 'Name' en Global Tool Configuration
        // (Manage Jenkins -> Tools -> Maven Installations)
        maven 'Maven'
    }

    // No necesitas declarar las variables de entorno aquí,
    // withCredentials se encarga de inyectarlas temporalmente
    // environment {
    //     NEXUS_CREDENTIALS = credentials('nexus-credentials') // Esto ya no es necesario así
    //     NEXUS_USERNAME = NEXUS_CREDENTIALS.username // Esto no es correcto en Declarative
    //     NEXUS_PASSWORD = NEXUS_CREDENTIALS.password // Esto no es correcto en Declarative
    // }

    stages {
        stage('Checkout') {
            steps {
                // El plugin de Git en la configuración del job ya suele manejar esto.
                // Si no has configurado el SCM en la configuración del job, descomenta la línea checkout scm
                echo "Clonando repositorio..."
                // checkout scm
            }
        }

        stage('Build and Deploy to Nexus') { // Renombrado para mayor claridad
            steps {
                // Usamos withCredentials para exponer las credenciales como variables de entorno
                // El ID 'NEXUS_CREDENTIALS' debe existir en Jenkins Credentials (Username with password)
                withCredentials([usernamePassword(credentialsId: 'NEXUS_CREDENTIALS', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    // Dentro de este bloque, NEXUS_USERNAME y NEXUS_PASSWORD están disponibles como variables de entorno

                    // Ejecuta el build de Maven.
                    // -s ${configFile(fileId: 'nexus-settings').path} le dice a Maven que use el archivo settings.xml gestionado por Jenkins.
                    // El ID 'nexus-settings' debe existir en Jenkins Config File Management.
                    // La llamada a configFile() obtiene la ruta temporal del archivo, y .path accede a esa ruta.
                    // Las variables de entorno NEXUS_USERNAME y NEXUS_PASSWORD (inyectadas por withCredentials) serán leídas por el settings.xml gestionado.
                    sh "mvn -s ${configFile(fileId: 'nexus-settings').path} clean package deploy"

                    // El -Dmaven.test.skip=true es opcional si quieres saltar tests:
                    // sh "mvn -s ${configFile(fileId: 'nexus-settings').path} clean package deploy -Dmaven.test.skip=true"
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