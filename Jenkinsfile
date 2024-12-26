pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = 'springbootproject-master-app'
        DOCKER_TAG = "${BUILD_NUMBER}"
        LATEST_TAG = 'latest'
        // Configuration MySQL
        MYSQL_DATABASE = 'formation'
        MYSQL_ROOT_PASSWORD = ''
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'jekins-up',
                    url: 'https://github.com/nouhamorj/SpringBootProject.git',
                    branch: 'master'
            }
        }

        stage('Tests') {
            steps {
                script {
                    // Démarrer MySQL pour les tests
                    sh 'docker-compose up -d mysqldb'
                    // Attendre que MySQL soit prêt
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
                    // Exécuter les tests
                    sh 'mvn test'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Login Docker Hub
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'

                    // Build avec les deux tags
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:${LATEST_TAG} ."

                    // Push les deux tags
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:${LATEST_TAG}"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Mise à jour du tag dans docker-compose
                    sh """
                        sed -i 's|build:|image: ${DOCKER_IMAGE}:${DOCKER_TAG}|g' docker-compose.yml
                        sed -i '/dockerfile:/d' docker-compose.yml
                        sed -i '/context:/d' docker-compose.yml
                    """

                    // Déploiement avec docker-compose
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        always {
            script {
                // Nettoyage
                sh 'docker-compose down || true'
                sh 'docker logout'
                cleanWs()
            }
        }
    }
}