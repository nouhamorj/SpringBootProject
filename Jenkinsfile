 pipeline {
    agent any
    triggers {
        pollSCM ( ’ H/5 * * * * ’)
    }

    environment {
        DOCKER_IMAGE_NAME = "springbootproject"
        DOCKER_TAG = "latest"
    }

    stages {
        stage('Cloner le depot') {
            steps {
                git branch: 'main', url: 'git@github.com:nouhamorj/SpringBootProject.git'
            }
        }
        
        stage('Construire le projet') {
            steps {
                script {
                    sh './mvnw clean install'
                }
            }
        }

        stage('Executer les tests') {
            steps {
                script {
                    sh './mvnw test'
                }
            }
        }

        stage('Creer une image Docker') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .'
                }
            }
        }

        stage('Pousser l\'image Docker') {
            steps {
                script {
                    sh 'docker push ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}'
                }
            }
        }
    }

    post {
        success {
            echo 'Le pipeline s\'est execute avec succes.'
        }
        failure {
            echo 'Le pipeline a echoue.'
        }
    }
}

