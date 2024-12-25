pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = "nouhamorj/springbootproject-master"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    options {
        skipDefaultCheckout(true)
        timeout(time: 1, unit: 'HOURS')
        disableConcurrentBuilds()
    }

    stages {
        stage('Git Config') {
            steps {
                sh '''
                    git config --global http.postBuffer 524288000
                    git config --global http.maxRequestBuffer 100M
                    git config --global core.compression 9
                    git config --global http.lowSpeedLimit 1000
                    git config --global http.lowSpeedTime 300
                '''
            }
        }

        stage('Checkout') {
            steps {
                script {
                    retry(3) {
                        timeout(time: 20, unit: 'MINUTES') {
                            sh 'rm -rf ./*'
                            sh 'rm -rf .git'
                            checkout([
                                $class: 'GitSCM',
                                branches: [[name: '*/master']],
                                extensions: [
                                    [$class: 'CloneOption',
                                        depth: 1,
                                        honorRefspec: true,
                                        noTags: true,
                                        reference: '',
                                        shallow: true,
                                        timeout: 30],
                                    [$class: 'CleanBeforeCheckout'],
                                    [$class: 'PruneStaleBranch'],
                                    [$class: 'SubmoduleOption',
                                        disableSubmodules: true,
                                        recursiveSubmodules: false,
                                        trackingSubmodules: false]
                                ],
                                userRemoteConfigs: [[
                                    credentialsId: 'git',
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
                        sh 'mvn clean package'
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
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

        stage('Deploy') {
            steps {
                sh '''
                    docker-compose down || true
                    docker-compose up -d
                '''
            }
        }
    }

    post {
        always {
            sh '''
                docker logout || true
                docker system prune -f || true
            '''
            cleanWs()
        }
    }
}