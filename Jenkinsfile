// Define variables para configuración
def dockerRegistryUrl = 'nexus.nexaplaform:6001' // <-- HOSTNAME y PUERTO de tu repositorio Docker en Nexus
def imageName = 'img-mic-auth'         // <-- Cambia esto por el nombre que quieres para tu imagen (ej: my-spring-app)
def dockerCredentialsId = 'NEXUS_DOCKER_CREDENTIALS' // <-- ID de tus credenciales de tipo Username/Password para Nexus Docker en Jenkins
def mavenSettingsFileId = 'nexus-settings' // <-- ID de tu archivo settings.xml en la configuración de Jenkins
def nexusCredentialsId = 'NEXUS_CREDENTIALS' // <-- ID de tus credenciales de tipo Username/Password para Nexus Maven en Jenkins

pipeline {
    agent any // O un agente específico que tenga Docker instalado (ej: agent { label 'docker-agent' })

    tools {
        // Asegúrate de que 'Maven' coincide exactamente con el nombre de tu configuración de herramienta Maven en Jenkins
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Clonando repositorio..."
                // !!! Importante: Agrega aquí tus pasos reales para clonar el código fuente !!!
                // Si usas Git, sería algo como:
                // git url: 'ssh://git@your-scm/your-project.git', branch: 'main' // o tu URL y rama
            }
        }

        stage('Build and Deploy to Nexus') {
            steps {
                echo "Construyendo proyecto Maven y desplegando artefacto a Nexus..."
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

        stage('Build Docker Image') {
            steps {
                echo "Construyendo imagen Docker..."
                script {
                    // Construye la imagen Docker usando el Dockerfile que debe estar en la raíz del proyecto.
                    // Se taggea con el nombre completo del repositorio Docker en Nexus y el número de build de Jenkins como tag.
                    // El "." al final indica que el contexto de build es el directorio actual del proyecto.
                    docker.build("${dockerRegistryUrl}/${imageName}:${env.BUILD_NUMBER}", ".")
                }
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                echo "Subiendo imagen Docker a Nexus..."
                script {
                    // Autentica contra el repositorio Docker de Nexus usando las credenciales configuradas en Jenkins.
                    // Luego, sube la imagen previamente construida y taggeada.
                    // Asegúrate de usar "http://" o "https://" aquí según cómo configuraste el puerto en Nexus.
                    docker.withRegistry("http://${dockerRegistryUrl}", dockerCredentialsId) {
                         docker.image("${dockerRegistryUrl}/${imageName}:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }

        // Puedes añadir más stages aquí si los necesitas, por ejemplo:
        // stage('Run Integration Tests') { ... }
        // stage('Security Scan') { ... }
        // stage('Deploy to Environment') { ... } // por ejemplo, a Kubernetes/OpenShift usando la imagen Docker
    }

    post {
        always {
            echo 'Pipeline finalizado.'
        }
        success {
            echo '✅ ¡Pipeline completado exitosamente! Build Maven, deploy a Nexus Maven y build/push de imagen Docker finalizados.'
        }
        failure {
            echo '❌ ¡Pipeline fallido! Revisa los logs para identificar el error.'
             // Opcional: Notificar por correo, Slack, etc.
        }
        // Opcional: Añadir 'unstable' o 'aborted' si los necesitas
    }
}