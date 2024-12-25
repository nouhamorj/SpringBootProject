pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Identifiant des credentials DockerHub
        GIT_CREDENTIALS = credentials('git') // Identifiant des credentials GitHub
        IMAGE_NAME = "nouhamorj/springbootproject-master-app" // Remplace par le nom de ton image Docker
    }

    stages {
        stage('Cloner le dépôt') {
            steps {
                git credentialsId: "${GIT_CREDENTIALS}", url: 'https://github.com/ton-compte/ton-repo.git', branch: 'main'
            }
        }

        stage('Construire et tester l’application') {
            steps {
                script {
                    sh './mvnw clean package -DskipTests' // Construire le projet Spring Boot
                }
            }
        }

        stage('Construire l’image Docker') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Pousser l’image sur DockerHub') {
            steps {
                script {
                    sh """
                        echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                        docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Déployer avec Docker Compose') {
            steps {
                script {
                    sh """
                        docker-compose down || true
                        docker-compose up -d
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        success {
            echo 'Pipeline exécuté avec succès.'
        }
        failure {
            echo 'Le pipeline a échoué.'
        }
    }
}
