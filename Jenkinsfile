// Define variables para configuración
// >>> Asegúrate de que estos valores son correctos para tu entorno <<<
def dockerRegistryUrl = 'nexus.nexaplaform:6001' // <-- HOSTNAME y PUERTO de tu repositorio Docker en Nexus
def imageName = 'backend-docker'         // <-- Nombre que quieres para tu imagen (ej: backend-docker)
def dockerCredentialsId = 'NEXUS_DOCKER_CREDENTIALS' // <-- ID de tus credenciales Username/Password para Nexus Docker en Jenkins
def mavenSettingsFileId = 'nexus-settings' // <-- ID de tu archivo settings.xml en la configuración de Jenkins
def nexusCredentialsId = 'NEXUS_CREDENTIALS' // <-- ID de tus credenciales Username/Password para Nexus Maven en Jenkins

pipeline {
    // Usa una plantilla de Pod con el contenedor 'kaniko' configurado
    // Asegúrate de que tu Cloud de Kubernetes y Plantilla de Pod están configurados en Jenkins UI
    // Reemplaza 'your-agent-label' por la etiqueta que le pusiste a tu plantilla de Pod en Jenkins
    agent { kubernetes { label 'jenkins-jenkins-agent' } }

    tools {
        // Asegúrate de que 'Maven' coincide exactamente con el nombre de tu configuración de herramienta Maven en Jenkins
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Clonando repositorio..."
                // !!! Importante: Agrega aquí tus pasos reales para clonar el código fuente !!!
                // Ejemplo para Git:
                // git url: 'ssh://git@your-scm/your-project.git', branch: 'main'
            }
        }

        stage('Build and Deploy to Nexus') {
            steps {
                echo "Construyendo proyecto Maven y desplegando artefacto a Nexus Maven..."
                withCredentials([usernamePassword(
                    credentialsId: nexusCredentialsId,
                    usernameVariable: 'NEXUS_USERNAME',
                    passwordVariable: 'NEXUS_PASSWORD'
                )]) {
                    configFileProvider([configFile(
                        fileId: mavenSettingsFileId,
                        variable: 'MAVEN_SETTINGS'
                    )]) {
                        sh """
                            echo "Usando settings: \$MAVEN_SETTINGS"
                            # Ejecuta el ciclo de vida de Maven: limpia, empaqueta y despliega a Nexus Maven
                            mvn -s \$MAVEN_SETTINGS clean package deploy
                        """
                    }
                }
            }
        }

        stage('Build and Push Docker Image (Kaniko)') {
            steps {
                echo "Construyendo y subiendo imagen Docker con Kaniko..."
                script {
                    // 1. Acceder a las credenciales de Nexus Docker configuradas en Jenkins
                    withCredentials([usernamePassword(
                        credentialsId: dockerCredentialsId,
                        usernameVariable: 'NEXUS_DOCKER_USERNAME',
                        passwordVariable: 'NEXUS_DOCKER_PASSWORD'
                    )]) {
                        // 2. Crear el directorio .docker y el archivo config.json en el workspace para que Kaniko lo use
                        // El workspace del pipeline se monta automáticamente en /workspace dentro del contenedor kaniko
                        sh "mkdir -p ${WORKSPACE}/.docker"
                        def dockerConfigJson = """
                        {
                            "auths": {
                                "${dockerRegistryUrl}": {
                                    "auth": "${("${NEXUS_DOCKER_USERNAME}:${NEXUS_DOCKER_PASSWORD}".bytes.encodeBase64().toString())}"
                                }
                            }
                        }
                        """
                        // Escribir el contenido JSON al archivo config.json en el workspace
                        writeFile file: "${WORKSPACE}/.docker/config.json", text: dockerConfigJson
                        echo "Archivo .docker/config.json creado en el workspace."

                        // 3. Ejecutar Kaniko en su propio contenedor 'kaniko'
                        // El comando 'sh' se ejecuta dentro del contenedor llamado 'kaniko'
                        container('kaniko') {
                            sh """
                                echo "Ejecutando Kaniko executor desde /kaniko/executor..."
                                # Ejecuta el comando kaniko/executor con los argumentos necesarios:
                                # --context: Ruta al contexto del build (workspace montado en /workspace)
                                # --dockerfile: Ruta al Dockerfile dentro del contexto
                                # --destination: La URL completa de destino de la imagen en el registro Nexus
                                # --dockerconfig: Directorio donde Kaniko debe buscar el config.json que creamos
                                /kaniko/executor \\
                                    --context=/workspace \\
                                    --dockerfile=/workspace/Dockerfile \\
                                    --destination=${dockerRegistryUrl}/${imageName}:${env.BUILD_NUMBER} \\
                                    --dockerconfig=/workspace/.docker
                            """
                        }
                    }
                }
            }
        }

        // Puedes añadir más stages aquí si los necesitas
        // stage('Deploy to Environment') { ... }
    }

    post {
        always {
            echo 'Pipeline finalizado.'
        }
        success {
            echo '✅ ¡Pipeline completado exitosamente con Kaniko!'
        }
        failure {
            echo '❌ ¡Pipeline fallido! Revisa los logs para identificar el error.'
        }
    }
}