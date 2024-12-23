pipeline {
    agent any
    triggers {
        pollSCM('H/5 * * * *') // Vérification des modifications toutes les 5 minutes
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('nouha') // Identifiants Docker Hub
        IMAGE_NAME = 'nouha/springboot-project' // Nom de l'image Docker
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', // Utilisation de la branche master
                    url: 'git@github.com:nouhamorj/SpringBootProject.git',
                    credentialsId: 'github_ssh'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE_NAME}")
                }
            }
        }

        stage('Scan Docker Image') {
            steps {
                script {
                    sh """
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                    aquasec/trivy:latest image --exit-code 1 --severity HIGH,CRITICAL \
                    ${IMAGE_NAME}
                    """
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKERHUB_CREDENTIALS}") {
                        dockerImage.push()
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Le pipeline s\'est exécuté avec succès. L\'image Docker a été construite et poussée sur Docker Hub.'
        }
        failure {
            echo 'Le pipeline a échoué. Veuillez vérifier les logs pour plus de détails.'
        }
    }
}
