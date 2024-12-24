pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = "nouhamorj/springbootproject-master"
        DOCKER_TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            agent {
                docker {
                    image 'maven:3.8.4-openjdk-17'
                    reuseNode true
                }
            }
            steps {
                sh '''
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Run Tests') {
            agent {
                docker {
                    image 'maven:3.8.4-openjdk-17'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Construction de l'image
                    sh """
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    """

                    // Login à DockerHub
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'

                    // Push de l'image
                    sh """
                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Arrêt des conteneurs existants
                    sh 'docker-compose down || true'

                    // Démarrage des nouveaux conteneurs
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
            cleanWs()
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline execution failed!'
        }
    }
}