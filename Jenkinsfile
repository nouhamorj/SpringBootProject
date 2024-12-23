pipeline {
    agent any
    environment {
        // Identifiants pour Docker Hub et GitHub
        DOCKERHUB_CREDENTIALS = credentials('nouhamorj') // Docker Hub
        GITHUB_CREDENTIALS = credentials('github-jenkins') // GitHub
    }
    stages {
        stage('Checkout') {
            steps {
                echo "Démarrage de l'étape de Checkout"
                git branch: 'master',
                    url: 'git@github.com:nouhamorj/SpringBootProject.git',
                    credentialsId: 'github-jenkins' // ID des credentials GitHub
                echo "Fin de l'étape de Checkout"
            }
        }
        stage('Build Docker Image') {
            steps {
                echo "Démarrage de la construction de l'image Docker"
                script {
                    dockerImage = docker.build("nouhamorj/springboot-project") // Nom de l'image Docker
                }
                echo "Image Docker construite avec succès"
            }
        }
        stage('Push Docker Image') {
            steps {
                echo "Démarrage de l'envoi de l'image Docker à Docker Hub"
                script {
                    docker.withRegistry('', "${DOCKERHUB_CREDENTIALS}") {
                        dockerImage.push() // Pousse l'image sur Docker Hub
                    }
                }
                echo "Image Docker envoyée avec succès"
            }
        }
    }
    post {
        success {
            echo 'Pipeline terminé avec succès.'
        }
        failure {
            echo 'Le pipeline a échoué. Veuillez vérifier les logs pour plus de détails.'
        }
    }
}
