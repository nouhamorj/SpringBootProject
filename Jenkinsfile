pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'my-spring-boot-app'  // Nom de l'image Docker
        DOCKER_TAG = "latest"  // Tag de l'image
        REGISTRY = 'docker.io'  // Docker Hub ou autre registre
        REGISTRY_CREDENTIALS = 'dockerhub-creds'  // Identifiants Docker Hub
        TRIVY_IMAGE = 'aquasec/trivy:latest'  // Image Docker pour Trivy
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupérer le code source du dépôt
                git 'https://votre-repository-url.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Construire l'image Docker à partir du Dockerfile
                    sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
                }
            }
        }

        stage('Scan for Vulnerabilities with Trivy') {
            steps {
                script {
                    // Effectuer un scan de vulnérabilités sur l'image Docker construite avec Trivy
                    sh """
                    docker run --rm ${TRIVY_IMAGE} --quiet --severity HIGH,CRITICAL --exit-code 1 --no-progress ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }

        stage('Push Docker Image') {
            when {
                branch 'main'  // Pousser l'image uniquement si on est sur la branche 'main'
            }
            steps {
                script {
                    // Se connecter au registre Docker
                    withCredentials([usernamePassword(credentialsId: REGISTRY_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin ${REGISTRY}"
                    }

                    // Taguer l'image avant de la pousser
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"

                    // Pousser l'image vers Docker Hub
                    sh "docker push ${REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage('Cleanup') {
            steps {
                // Nettoyer les ressources Docker (images et conteneurs)
                sh 'docker system prune -f'
            }
        }
    }

    post {
        always {
            // Toujours arrêter les services Docker, même en cas d'erreur
            sh 'docker-compose down || true'
        }
    }
}
