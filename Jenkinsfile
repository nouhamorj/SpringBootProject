pipeline {
    agent any
    environment {
        // Identifiants pour Docker Hub et GitHub
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        SSH_CREDENTIALS = credentials('github-ssh-key')
        DOCKER_IMAGE_TAG = "nouhamorj/springboot-project:latest"
    }
    stages {
        stage('Checkout') {
            steps {
                echo "Démarrage de l'étape de Checkout"
                script {
                    try {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/master']],
                            userRemoteConfigs: [[
                                url: 'git@github.com:nouhamorj/SpringBootProject.git',
                                credentialsId: 'github-ssh-key'
                            ]]
                        ])
                        echo "Fin de l'étape de Checkout"
                    } catch (Exception e) {
                        echo "Erreur lors du checkout : ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                echo "Démarrage de la construction de l'image Docker"
                script {
                    try {
                        dockerImage = docker.build(DOCKER_IMAGE_TAG)
                        echo "Image Docker construite avec succès"
                    } catch (Exception e) {
                        echo "Erreur lors de la construction de l'image Docker : ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                echo "Démarrage de l'envoi de l'image Docker à Docker Hub"
                script {
                    try {
                        withCredentials([usernamePassword(
                            credentialsId: 'dockerhub-credentials',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh """
                                echo \${DOCKER_PASS} | docker login -u \${DOCKER_USER} --password-stdin
                                docker push ${DOCKER_IMAGE_TAG}
                            """
                        }
                        echo "Image Docker envoyée avec succès"
                    } catch (Exception e) {
                        echo "Erreur lors de l'envoi de l'image Docker à Docker Hub : ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
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
        always {
            sh 'docker logout'
        }
    }
}