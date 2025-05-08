// Define la pipeline
pipeline {
    // Configura el agente donde se ejecutará el pipeline.
    // 'any' significa que se ejecutará en cualquier agente disponible.
    // Puedes usar 'agent { label 'tu-etiqueta-agente' }' si usas etiquetas.
    agent any

    // Configura las herramientas necesarias (Maven en este caso).
    // El nombre 'Maven' debe coincidir con el 'Name' configurado en
    // Manage Jenkins -> Tools -> Maven Installations.
    tools {
        maven 'Maven'
    }

    // No necesitas la sección 'environment' para las credenciales aquí,
    // ya que 'withCredentials' las inyecta temporalmente.

    // Define las etapas del pipeline
    stages {
        stage('Checkout') {
            steps {
                // Paso para clonar el repositorio.
                // Si configuraste el repositorio en la configuración del job de Jenkins
                // (en la sección 'Pipeline' o 'Source Code Management'), Jenkins ya
                // se encarga del checkout automáticamente antes de ejecutar el primer stage.
                echo "Clonando repositorio (si no lo hace Jenkins automáticamente)..."
                // descomenta la siguiente línea si necesitas hacer el checkout manualmente:
                // checkout scm
            }
        }

        stage('Build and Deploy to Nexus') {
            steps {
                // --- Inicia Bloque de Verificación de Java ---
                // Añadimos pasos para verificar la versión de Java disponible en el agente
                echo "========================================="
                echo "Verificando versión de Java en el agente..."
                // Ejecuta el comando 'java -version' en el shell del agente
                sh "java -version"
                echo "========================================="
                // --- Fin Bloque de Verificación de Java ---

                // Usa 'withCredentials' para obtener las credenciales de Nexus de forma segura.
                // 'credentialsId' debe coincidir con el ID de la credencial 'Username with password' en Jenkins.
                // 'usernameVariable' y 'passwordVariable' definen los nombres de las variables de entorno
                // que estarán disponibles dentro de este bloque.
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_CREDENTIALS',
                    usernameVariable: 'NEXUS_USERNAME', // Variable de entorno para el usuario
                    passwordVariable: 'NEXUS_PASSWORD'  // Variable de entorno para la contraseña
                )]) {
                    // Usa 'configFileProvider' para obtener el archivo settings.xml gestionado.
                    // 'fileId' debe coincidir con el ID del archivo 'Maven settings file' en Manage Jenkins -> System -> Managed files.
                    // 'variable' define el nombre de la variable de entorno que contendrá la ruta al archivo temporal.
                    configFileProvider([configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS' // Variable de entorno para la ruta del settings.xml
                    )]) {
                        // Ejecuta el comando Maven dentro del shell del agente.
                        // Usa '-s $MAVEN_SETTINGS' para especificar el archivo settings.xml temporal.
                        // Las variables de entorno NEXUS_USERNAME y NEXUS_PASSWORD, junto con MAVEN_SETTINGS,
                        // estarán disponibles en este shell y serán usadas por Maven (a través del settings.xml).
                        sh """
                            echo "Usando settings file temporal: \$MAVEN_SETTINGS"
                            // Ejecuta el build y el deploy
                            mvn -s \$MAVEN_SETTINGS clean package deploy
                            // Si necesitas saltar tests, usa:
                            // mvn -s \$MAVEN_SETTINGS clean package deploy -Dmaven.test.skip=true
                        """
                    }
                }
            }
        }

        // Puedes añadir más etapas aquí si lo necesitas (ej: análisis SonarQube, build Docker, push Docker, etc.)
        // stage('SonarQube Analysis') { ... }
        // stage('Build Docker Image') { ... }
        // stage('Push Docker Image') { ... }
        // stage('Trigger Deployment') { ... }
    }

    // Define acciones a ejecutar después de que todas las etapas terminan
    post {
        // Se ejecuta siempre al finalizar el pipeline (éxito o fallo)
        always {
            echo 'Pipeline finalizado.'
            // Puedes añadir pasos de limpieza aquí si es necesario
        }
        // Se ejecuta solo si el pipeline fue exitoso
        success {
            echo '¡Build y deploy exitoso!'
        }
        // Se ejecuta solo si el pipeline falló
        failure {
            echo 'Error en el build o deploy.'
            // Puedes añadir notificaciones de fallo aquí
        }
        // Se ejecuta solo si el pipeline fue abortado
        aborted {
            echo 'Pipeline abortado.'
        }
    }
}