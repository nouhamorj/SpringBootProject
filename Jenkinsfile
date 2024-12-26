pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = 'nouhamorj/spring-boot-app'
        DOCKER_TAG = "${BUILD_NUMBER}"
        LATEST_TAG = 'latest'
        MYSQL_DATABASE = 'formation'
        MYSQL_ROOT_PASSWORD = ''
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
                    credentialsId: 'jekins-up',
                    url: 'https://github.com/nouhamorj/SpringBootProject.git'
            }
        }

        stage('Tests') {
            steps {
                script {
                    sh 'docker-compose up -d mysqldb'
                    sh '''
                        timeout=60
                        while ! docker-compose exec -T mysqldb mysqladmin ping -h localhost --silent; do
                            sleep 1
                            timeout=$((timeout - 1))
                            if [ $timeout -le 0 ]; then
                                echo "Timeout waiting for MySQL"
                                exit 1
                            fi
                        done
                    '''
                    sh 'mvn test'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:${LATEST_TAG} ."
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:${LATEST_TAG}"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh """
                        sed -i 's|build:|image: ${DOCKER_IMAGE}:${DOCKER_TAG}|g' docker-compose.yml
                        sed -i '/dockerfile:/d' docker-compose.yml
                        sed -i '/context:/d' docker-compose.yml
                    """
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        always {
            script {
                sh 'docker-compose down || true'
                sh 'docker logout'
                cleanWs()
            }
        }
    }
}