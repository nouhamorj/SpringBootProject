pipeline {
    agent any

    parameters {
        string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'Branch à construire')
        string(name: 'DOCKERHUB_REPO', defaultValue: 'nouhamorj', description: 'Nom du dépôt Docker')
        string(name: 'DOCKER_IMAGE_NAME', defaultValue: 'springbootproject', description: 'Nom de l'image Docker')
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/nouhamorj/SpringBootProject', branch: "${params.GIT_BRANCH}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    def dockerImageName = "${params.DOCKERHUB_REPO}/${params.DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}"
                    sh "docker build -t $dockerImageName ."
                    withCredentials([usernamePassword(credentialsId: 'dockerhub',
                                            passwordVariable: 'DOCKER_PASSWORD',
                                            usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        sh "docker push $dockerImageName"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                // À adapter en fonction de votre environnement de déploiement
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}