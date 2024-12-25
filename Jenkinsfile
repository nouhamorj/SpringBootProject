pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = "nouhamorj/springbootproject-master"
        DOCKER_TAG = "${BUILD_NUMBER}"
        GIT_CREDENTIALS_ID = 'git'
    }

    options {
        timeout(time: 1, unit: 'HOURS')
        disableConcurrentBuilds()
        gitLabConnection('gitlab')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    retry(3) {
                        timeout(time: 10, unit: 'MINUTES') {
                            checkout([$class: 'GitSCM',
                                branches: [[name: '*/master']],
                                extensions: [
                                    [$class: 'CloneOption',
                                        depth: 1,
                                        noTags: true,
                                        shallow: true,
                                        timeout: 30],
                                    [$class: 'GitLFSPull'],
                                    [$class: 'CleanBeforeCheckout']
                                ],
                                userRemoteConfigs: [[
                                    credentialsId: env.GIT_CREDENTIALS_ID,
                                    url: scm.userRemoteConfigs[0].url
                                ]]
                            ])
                        }
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
                    retry(2) {
                        timeout(time: 10, unit: 'MINUTES') {
                            sh """
                                docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                                docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                            """

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
            }
        }

        stage('Deploy') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        sh '''
                            docker-compose down || true
                            docker-compose up -d
                        '''
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    timeout(time: 2, unit: 'MINUTES') {
                        retry(3) {
                            sh '''
                                sleep 30
                                curl -f http://localhost:8080/actuator/health || exit 1
                            '''
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                sh '''
                    docker logout || true
                    docker system prune -f || true
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