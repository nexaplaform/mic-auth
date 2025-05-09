// Define la pipeline
pipeline {
    // Configura el agente donde se ejecutará el pipeline.
    // 'agent any' significa que se ejecutará en cualquier agente disponible.
    // Puedes usar 'agent { label 'tu-etiqueta-agente' }' si usas etiquetas específicas.
    agent any

    // Configura las herramientas necesarias (Maven en este caso).
    // El nombre 'Maven' debe coincidir exactamente con el 'Name' configurado en
    // Manage Jenkins -> Tools -> Maven Installations.
    tools {
        maven 'Maven'
    }

    // La sección 'environment' no es necesaria para las credenciales inyectadas por 'withCredentials'.

    // Define las etapas del pipeline
    stages {
        stage('Checkout') {
            steps {
                // Paso para clonar el repositorio.
                // Si configuraste el repositorio en la configuración del job de Jenkins
                // (en 'Source Code Management'), Jenkins ya lo hace automáticamente.
                echo "Clonando repositorio (si no lo hace Jenkins automáticamente)..."
                // Descomenta la siguiente línea si necesitas hacer el checkout manualmente en el pipeline:
                // checkout scm
            }
        }

        stage('Build') { // Etapa RENOMBRADA: Solo realiza el build (clean package)
            steps {
                // --- Bloque de Verificación de Java ---
                echo "========================================="
                echo "Verificando versión de Java en el agente..."
                sh "java -version" // Esto te mostrará la versión de Java del agente
                echo "========================================="
                // --- Fin Bloque de Verificación de Java ---

                // Obtiene las credenciales de Nexus de forma segura
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_CREDENTIALS', // ID de la credencial Username/Password en Jenkins
                    usernameVariable: 'NEXUS_USERNAME', // Nombre de la variable de entorno para el usuario
                    passwordVariable: 'NEXUS_PASSWORD'  // Nombre de la variable de entorno para la contraseña
                )]) {
                    // Obtiene la ruta al archivo settings.xml gestionado
                    configFileProvider([configFile(
                        fileId: 'nexus-settings', // ID del archivo Maven settings file en Jenkins Managed Files
                        variable: 'MAVEN_SETTINGS' // Nombre de la variable de entorno para la ruta del settings.xml
                    )]) {
                        // Ejecuta el comando Maven
                        sh """
                            echo "Usando settings file temporal: \$MAVEN_SETTINGS"
                            # Ejecuta solo los goals clean y package para construir el proyecto
                            mvn -s \$MAVEN_SETTINGS -U clean package
                        """
                    }
                }
            }
        }

        // --- Etapa para Análisis SonarQube ---
        stage('SonarQube Analysis') {
            steps {
                echo "========================================="
                echo "Ejecutando análisis SonarQube..."
                echo "========================================="
                // Configura las variables de entorno para SonarQube.
                // 'SonarQube' es el NOMBRE que le diste a la configuración del servidor en Jenkins -> Configure System.
                withSonarQubeEnv('SonarQube') { // Asegúrate de que 'SonarQube' coincide con el nombre
                    // Necesitas el settings.xml aquí para que Maven pueda resolver dependencias (como el SonarQube Scanner)
                    configFileProvider([configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                         sh """
                            echo "Usando settings file temporal: \$MAVEN_SETTINGS"
                            # Ejecuta el goal 'sonar:sonar'.
                            # -U fuerza a Maven a revalidar la metadata y los plugins de repos remotos, útil para problemas de caché/resolución.
                            # -Dsonar.projectKey es OBLIGATORIO si no está en tu pom.xml.
                            # Reemplaza 'mic_auth' con el Project Key que deseas en SonarQube.
                            mvn -s \$MAVEN_SETTINGS -U sonar:sonar -Dsonar.projectKey=mic_auth
                            # Puedes añadir otras propiedades si es necesario con -D, ej: -Dsonar.sources=src/main/java
                         """
                    }
                }
                echo "========================================="
                echo "Análisis SonarQube completado."
                echo "Revisa la interfaz de SonarQube para ver los resultados."
                echo "========================================="
            }
        }
        // --- Fin Etapa SonarQube ---

        // --- Etapa para verificar Quality Gate (Recomendado) ---
        stage('Quality Gate Check') {
            steps {
                echo "========================================="
                echo "Verificando Quality Gate en SonarQube..."
                echo "========================================="
                // Espera a que SonarQube finalice el análisis y reporte el estado del Quality Gate.
                // 'abortPipeline: true' hará que el build de Jenkins falle si el Quality Gate en SonarQube falla.
                // Esto detiene las etapas posteriores como el deploy si el código no cumple los requisitos.
                waitForQualityGate abortPipeline: true
                echo "========================================="
                echo "Verificación de Quality Gate completada."
                echo "========================================="
            }
        }
        // --- Fin Etapa Quality Gate ---


        // --- Etapa para Deploy a Nexus ---
        stage('Deploy to Nexus') {
             steps {
                echo "========================================="
                echo "Iniciando Deploy a Nexus..."
                echo "========================================="
                // Necesitas las credenciales y el settings.xml nuevamente para el deploy.
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
                            # Ejecuta solo el goal 'deploy'
                            # Recuerda que las URLs en la sección distributionManagement de tu pom.xml
                            # deben apuntar a la URL interna correcta de Nexus en K8s.
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
        // Se ejecuta siempre al finalizar el pipeline (éxito, fallo o abortado)
        always {
            echo 'Pipeline finalizado.'
            // Puedes añadir pasos de limpieza aquí si es necesario
        }
        // Se ejecuta solo si el pipeline fue exitoso (todas las etapas pasaron)
        success {
            echo '¡Pipeline (build, análisis, deploy) exitoso!'
        }
        // Se ejecuta solo si alguna etapa del pipeline falló
        failure {
            echo 'Error en alguna etapa del pipeline.'
            // Puedes añadir notificaciones de fallo aquí
        }
        // Se ejecuta solo si el pipeline fue abortado por el usuario
        aborted {
            echo 'Pipeline abortado.'
        }
    }
}