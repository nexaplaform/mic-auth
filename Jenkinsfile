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
                // El plugin de Git ya maneja esto si configuraste SCM en la configuración del job
                echo "Clonando repositorio..."
                // checkout scm // descomentar si no usas SCM en la configuración del job
            }
        }

        stage('Build and Deploy to Nexus') {
                    steps {
                        // Usamos withCredentials para exponer las credenciales como variables de entorno
                        withCredentials([usernamePassword(credentialsId: 'NEXUS_CREDENTIALS', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                            // Dentro de este bloque, NEXUS_USERNAME y NEXUS_PASSWORD están disponibles como variables de entorno

                            // ** MODIFICACIÓN AQUÍ **
                            // Obtenemos la ruta del archivo de configuración gestionado y la guardamos en una variable
                            // Eliminamos el bloque {} que seguía a configFile anteriormente
                            def mavenSettingsFile = configFile(fileId: 'nexus-settings')

                            // Ejecuta el build de Maven, usando la ruta del archivo obtenida
                            // La ruta está disponible en la propiedad '.path' del objeto devuelto por configFile
                            // Las variables de entorno NEXUS_USERNAME y NEXUS_PASSWORD (inyectadas por withCredentials)
                            // serán leídas por el settings.xml gestionado
                            sh "mvn -s ${mavenSettingsFile.path} clean package deploy"
                            // ** FIN MODIFICACIÓN **
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
