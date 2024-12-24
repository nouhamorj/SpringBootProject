pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        APP_SERVICE_NAME = 'app'
        DOCKER_IMAGE = 'springbootproject-master-app:latest' // Remplacez par votre image Docker
        DOCKER_REGISTRY_CREDENTIALS = 'docker-hub-credentials' // Nom des identifiants Jenkins pour Docker Hub
        GITHUB_CREDENTIALS = 'github-pat-credentials' // Nom des identifiants Jenkins pour GitHub (avec PAT ou SSH)
    }

    stages {
        stage('Install Docker Compose') {
            steps {
                script {
                    sh 'curl -L "https://github.com/docker/compose/releases/download/v2.18.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose'
                    sh 'chmod +x /usr/local/bin/docker-compose'
                    sh 'docker-compose --version'
                }
            }
        }

        stage('Checkout Code') {
            steps {
                echo 'Cloning the repository...'
                git credentialsId: "$GITHUB_CREDENTIALS", branch: 'master', url: 'https://github.com/nouhamorj/SpringBootProject.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images using Docker Compose...'
                sh 'docker-compose -f $DOCKER_COMPOSE_FILE build'
            }
        }

        stage('Run Services') {
            steps {
                echo 'Starting services for testing...'
                sh 'docker-compose -f $DOCKER_COMPOSE_FILE up -d'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running application tests...'
                sh 'curl -f http://localhost:8080 || exit 1'
            }
        }

        stage('Scan Vulnerabilities') {
            steps {
                echo 'Scanning for vulnerabilities with Trivy...'
                sh '''
                trivy image $DOCKER_IMAGE > trivy-report.txt || true
                echo "Vulnerability report saved to trivy-report.txt."
                '''
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "$DOCKER_REGISTRY_CREDENTIALS") {
                        sh 'docker push $DOCKER_IMAGE'
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Stopping and cleaning up Docker Compose services...'
            sh 'docker-compose -f $DOCKER_COMPOSE_FILE down --volumes || true'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
