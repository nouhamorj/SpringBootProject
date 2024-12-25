pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = "nouhamorj/springbootproject-master"
        DOCKER_TAG = "${BUILD_NUMBER}"
        GIT_CREDENTIALS_ID = 'git-credentials'  // ID des credentials Jenkins pour Git
    }

    options {
        timeout(time: 1, unit: 'HOURS')
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'git-credentials',
                                                passwordVariable: 'GIT_PASSWORD',
                                                usernameVariable: 'GIT_USERNAME')]) {
                    script {
                        // Configure Git avec le PAT
                        sh """
                            git config --global credential.helper store
                            echo "https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com" > ~/.git-credentials
                        """
                        checkout scm
                    }
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    docker.image('maven:3.8.4-openjdk-17').inside('-v $HOME/.m2:/root/.m2') {
                        sh '''
                            mvn clean package -DskipTests
                            mvn test
                        '''
                    }
                }
            }
            post {
                success {
                    junit '**/target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    // Construction de l'image avec tag BUILD_NUMBER et latest
                    sh """
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    """

                    // Login à DockerHub
                    withCredentials([usernamePassword(credentialsId: 'dockerhub',
                                                    passwordVariable: 'DOCKER_PASSWORD',
                                                    usernameVariable: 'DOCKER_USERNAME')]) {
                        sh """
                            echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin
                            docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push ${DOCKER_IMAGE}:latest
                        """
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh '''
                        docker-compose down || true
                        docker-compose up -d
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sh '''
                        sleep 30
                        curl -f http://localhost:8080/actuator/health || exit 1
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                sh '''
                    docker logout || true
                    rm -f ~/.git || true
                '''
            }
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