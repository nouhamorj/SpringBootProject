pipeline {
    agent any
    triggers {
        pollSCM ('H/5 * * * *')
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('nouha')  // Identifiants Docker Hub
        IMAGE_NAME = 'springbootproject'  // Nom de l'image Docker pour le serveur
        MYSQL_IMAGE = 'mysql:8.0.33'  // Image MySQL à utiliser
        MYSQL_DB = 'formation'  // Nom de la base de données
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'git@github.com:nouhamorj/SpringBootProject.git',
                    credentialsId: 'github_ssh''
            }
        }
        
        stage('Build Server Image') {
            steps {
                script {
                    // Construction de l'image Docker pour l'application Spring Boot
                    dockerImage = docker.build("springbootproject")
                }
            }
        }

        stage('Build MySQL Image') {
            steps {
                script {
                    // Construction de l'image Docker pour MySQL (optionnel, si vous n'utilisez pas l'image officielle)
                    dockerImageMySQL = docker.build("mysql:8.0.33")
                }
            }
        }

        stage('Scan Server Image') {
            steps {
                script {
                    // Scan de l'image Docker de l'application avec Trivy (optionnel)
                    sh """
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                        aquasec/trivy:latest image --exit-code 0 --severity LOW,MEDIUM,HIGH,CRITICAL \
                        ${dockerImage}
                    """
                }
            }
        }

        stage('Scan MySQL Image') {
            steps {
                script {
                    // Scan de l'image MySQL avec Trivy
                    sh """
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                        aquasec/trivy:latest image --exit-code 0 --severity LOW,MEDIUM,HIGH,CRITICAL \
                        ${MYSQL_IMAGE}
                    """
                }
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                script {
                    // Pousser l'image de l'application et de MySQL sur Docker Hub
                    docker.withRegistry('', "${DOCKERHUB_CREDENTIALS}") {
                        dockerImage.push("${IMAGE_NAME}:latest")
                        dockerImageMySQL.push("${MYSQL_IMAGE}:latest")
                    }
                }
            }
        }

        stage('Deploy Docker Compose') {
            steps {
                script {
                    // Déployer le stack Docker Compose
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
            }
        }
    }

    post {
        success {
            echo 'Le pipeline a été exécuté avec succès.'
        }
        failure {
            echo 'Le pipeline a échoué.'
        }
    }
}
