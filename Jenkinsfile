// Define la pipeline
pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Clonando repositorio (si no lo hace Jenkins automáticamente)..."
                // checkout scm
            }
        }

        stage('Build and Deploy to Nexus') {
            steps {
                echo "========================================="
                echo "Verificando versión de Java en el agente..."
                sh "java -version"
                echo "========================================="

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
                            echo "Usando settings file temporal: \$MAVEN_SETTINGS"
                            // Ejecuta el build (package). NO pongas 'deploy' aquí todavía
                            // La compilación ya ocurrió con 'package'.
                            // Aquí, 'package' asegura que los archivos .class están listos para el análisis.
                            mvn -s \$MAVEN_SETTINGS clean package
                        """
                    }
                }
            }
        }

        // --- Nueva Etapa para Análisis SonarQube ---
        stage('SonarQube Analysis') {
            steps {
                echo "========================================="
                echo "Ejecutando análisis SonarQube..."
                echo "========================================="

                // Usa el paso 'withSonarQubeEnv' proporcionado por el plugin.
                // El nombre 'Mi SonarQube K8s' debe coincidir con el configurado en Manage Jenkins -> Configure System.
                withSonarQubeEnv('Mi SonarQube K8s') {
                    // Dentro de este bloque, las variables de entorno para SonarQube están configuradas.
                    // Necesitas usar el settings.xml nuevamente para que Maven sepa cómo descargar plugins/dependencias de SonarQube si es necesario,
                    // y para la configuración del mirror de Nexus si SonarQube necesita descargar dependencias de tu proyecto.
                    configFileProvider([configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                         sh """
                            # Ejecuta el goal 'sonar:sonar' de Maven.
                            # Maven descargará el SonarQube Scanner for Maven si no lo tiene.
                            # El plugin de Jenkins ya configura las propiedades de conexión a SonarQube.
                            # '-Dsonar.projectKey=tu-project-key-en-sonar' es OBLIGATORIO si no está en tu pom.xml o archivo sonar-project.properties.
                            # El projectKey debe ser único en SonarQube.
                            mvn -s \$MAVEN_SETTINGS sonar:sonar -Dsonar.projectKey=mic_auth
                            # Si necesitas especificar otras propiedades, añádelas con -D, ej: -Dsonar.sources=src/main/java
                         """
                    }
                }
                echo "========================================="
                echo "Análisis SonarQube completado."
                echo "Revisa la interfaz de SonarQube para ver los resultados."
                echo "========================================="
            }
        }
        // --- Fin Nueva Etapa SonarQube ---

        // --- Etapa para Deploy a Nexus (Separada después del análisis) ---
        // Ahora que el análisis pasó (o se ejecutó), puedes desplegar.
        stage('Deploy to Nexus') {
             steps {
                echo "========================================="
                echo "Iniciando Deploy a Nexus..."
                echo "========================================="
                // Necesitas las credenciales y el settings.xml nuevamente para el deploy.
                 withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_CREDENTIALS',
                    usernameVariable: 'NEXUS_USERNAME', // Variable de entorno para el usuario
                    passwordVariable: 'NEXUS_PASSWORD'  // Variable de entorno para la contraseña
                )]) {
                    configFileProvider([configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                         sh """
                            echo "Usando settings file temporal: \$MAVEN_SETTINGS"
                            # Ejecuta solo el goal 'deploy'
                            mvn -s \$MAVEN_SETTINGS deploy
                         """
                    }
                }
                 echo "========================================="
                 echo "Deploy a Nexus completado."
                 echo "========================================="
            }
        }
        // --- Fin Etapa Deploy ---

        // Puedes añadir más etapas si lo necesitas:
        // stage('Build Docker Image') { ... }
        // stage('Push Docker Image') { ... }
        // stage('Trigger Deployment') { ... }
    }

    post {
        always {
            echo 'Pipeline finalizado.'
        }
        success {
            echo '¡Pipeline (build, análisis, deploy) exitoso!'
        }
        failure {
            echo 'Error en alguna etapa del pipeline.'
        }
        aborted {
            echo 'Pipeline abortado.'
        }
    }
}