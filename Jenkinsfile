// Define la pipeline
pipeline {
    // Configura el agente donde se ejecutará el pipeline.
    agent any

    // Configura las herramientas necesarias (Maven en este caso).
    tools {
        maven 'Maven' // Asegúrate de que este nombre coincide con el configurado en Jenkins -> Tools
    }

    // Define las etapas del pipeline
    stages {
        stage('Checkout') {
            steps {
                echo "Clonando repositorio (si no lo hace Jenkins automáticamente)..."
                // checkout scm
            }
        }

        stage('Build') { // Etapa RENOMBRADA: Solo realiza el build (package)
            steps {
                // --- Bloque de Verificación de Java ---
                echo "========================================="
                echo "Verificando versión de Java en el agente..."
                sh "java -version"
                echo "========================================="
                // --- Fin Bloque de Verificación de Java ---

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
                            // Ejecuta solo los goals clean y package para construir el proyecto
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

                // Usa 'withSonarQubeEnv' para configurar las variables de entorno de SonarQube.
                // 'Mi SonarQube K8s' es el NOMBRE que le diste a la configuración del servidor SonarQube en Jenkins -> Configure System.
                withSonarQubeEnv('SonarQube') {
                    // Necesitas el settings.xml también aquí para que Maven pueda descargar dependencias (incluido el SonarQube Scanner for Maven)
                    // y usar el mirror de Nexus si está configurado para todo.
                    configFileProvider([configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                         sh """
                            // Ejecuta el goal 'sonar:sonar' de Maven.
                            // Jenkins ya configuró las propiedades de conexión a SonarQube (URL, token) en el entorno.
                            // -Dsonar.projectKey es OBLIGATORIO si no lo defines en tu pom.xml o sonar-project.properties.
                            # Reemplaza 'mic_auth' con el Project Key que deseas en SonarQube.
                            mvn -s \$MAVEN_SETTINGS sonar:sonar -Dsonar.projectKey=mic_auth
                            # Puedes añadir otras propiedades si es necesario, ej: -Dsonar.sources=src/main/java
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

        // --- Nueva Etapa para verificar Quality Gate (Opcional pero Recomendado) ---
        stage('Quality Gate Check') {
            steps {
                echo "========================================="
                echo "Verificando Quality Gate en SonarQube..."
                echo "========================================="
                // Espera a que SonarQube finalice el análisis asíncrono y reporte el estado del Quality Gate.
                // Si el Quality Gate falla, esta etapa marcará el build en Jenkins como UNSTABLE o FAILED,
                // deteniendo potencialmente las etapas posteriores (como el deploy).
                waitForQualityGate abortPipeline: true
                echo "========================================="
                echo "Verificación de Quality Gate completada."
                echo "========================================="
            }
        }
        // --- Fin Nueva Etapa Quality Gate ---


        // --- Nueva Etapa para Deploy a Nexus ---
        // Esta etapa ejecuta el deploy después de que el build y el análisis (y Quality Gate si se incluyó) han finalizado.
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
                            // Ejecuta solo el goal 'deploy'
                            # Recuerda que las URLs en la sección distributionManagement de tu pom.xml
                            # deben apuntar a la URL interna de Nexus en K8s.
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

        // Puedes añadir más etapas después del deploy si lo necesitas (ej: build/push imagen Docker de la aplicación, notificaciones de despliegue, etc.)
        // stage('Build App Docker Image') { ... }
        // stage('Push App Docker Image') { ... }
        // stage('Trigger Deployment') { ... }
    }

    // Define acciones a ejecutar después de que todas las etapas terminan
    post {
        always {
            echo 'Pipeline finalizado.'
        }
        // El mensaje de éxito ahora refleja que todo el proceso se completó
        success {
            echo '¡Pipeline (build, análisis, deploy) exitoso!'
        }
        // El mensaje de fallo indica que algo salió mal en alguna etapa
        failure {
            echo 'Error en alguna etapa del pipeline.'
        }
        aborted {
            echo 'Pipeline abortado.'
        }
    }
}