pipeline {
    agent any
    environment {
        // L'image Docker de votre application Spring Boot
        DOCKER_IMAGE_APP = 'springbootproject-master-app'
        DOCKER_TAG_APP = 'latest'  // Vous pouvez utiliser un tag spécifique ou lié à un commit Git

        // L'image Docker pour MySQL
        DOCKER_IMAGE_DB = 'mysql:5.7'  // Utiliser la version que vous préférez
        DOCKER_TAG_DB = 'latest'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image for App') {
            steps {
                script {
                    // Construire l'image Docker pour l'application Spring Boot
                    sh 'docker build -t $DOCKER_IMAGE_APP:$DOCKER_TAG_APP .'
                }
            }
        }

        stage('Build Docker Image for MySQL') {
            steps {
                script {
                    // Construire l'image Docker pour MySQL (si vous avez un Dockerfile personnalisé pour MySQL)
                    sh 'docker build -t $DOCKER_IMAGE_DB:$DOCKER_TAG_DB ./path/to/mysql/Dockerfile'
                }
            }
        }

        stage('Run Docker Containers') {
            steps {
                script {
                    // Lancer le container pour l'application Spring Boot
                    sh 'docker run -d -p 8080:8080 --name app-container $DOCKER_IMAGE_APP:$DOCKER_TAG_APP'

                    // Lancer le container MySQL
                    sh 'docker run -d -p 3307:3306 --name db-container $DOCKER_IMAGE_DB:$DOCKER_TAG_DB'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Exécuter les tests dans le container de l'application
                    sh 'docker exec -t app-container ./mvnw test'
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    // Pousser l'image de l'application sur Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                        sh 'docker push $DOCKER_IMAGE_APP:$DOCKER_TAG_APP'

                        // Pousser l'image de la base de données sur Docker Hub
                        sh 'docker push $DOCKER_IMAGE_DB:$DOCKER_TAG_DB'
                    }
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    // Nettoyer les containers et images après exécution
                    sh 'docker rm -f $(docker ps -aq)'
                    sh 'docker rmi -f $DOCKER_IMAGE_APP:$DOCKER_TAG_APP $DOCKER_IMAGE_DB:$DOCKER_TAG_DB'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Pipeline finished successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
