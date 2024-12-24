pipeline {
    agent any

    stages {
        stage('Check Docker Compose') {
            steps {
                script {
                    // Check platform type and handle accordingly
                    if (isUnix()) {
                        echo 'Checking Docker Compose version (Linux)...'
                        def dockerComposeVersion = sh(script: 'docker-compose --version', returnStdout: true).trim()
                        echo "Docker Compose version: ${dockerComposeVersion}"

                        if (!dockerComposeVersion) {
                            echo 'Docker Compose not found, installing...'
                        } else {
                            echo 'Docker Compose is already installed.'
                        }
                    } else {
                        echo 'Checking Docker Compose version (Windows)...'
                        def dockerComposeVersion = bat(script: 'docker-compose --version', returnStdout: true).trim()
                        echo "Docker Compose version: ${dockerComposeVersion}"

                        if (!dockerComposeVersion) {
                            echo 'Docker Compose not found, but it should be installed with Docker Desktop on Windows.'
                        } else {
                            echo 'Docker Compose is already installed.'
                        }
                    }
                }
            }
        }

        stage('Declarative: Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Install Docker Compose') {
            when {
                expression { isUnix() }  // Ensure this only runs on Unix-like systems
            }
            steps {
                script {
                    def dockerComposeInstalled = sh(script: 'which docker-compose', returnStatus: true)
                    if (dockerComposeInstalled != 0) {
                        echo 'Installing Docker Compose...'
                        sh '''
                            sudo curl -L https://github.com/docker/compose/releases/download/v2.18.1/docker-compose-Linux-x86_64 -o /usr/local/bin/docker-compose
                            sudo chmod +x /usr/local/bin/docker-compose
                        '''
                    } else {
                        echo 'Docker Compose is already installed.'
                    }
                }
            }
        }

        stage('Checkout Code') {
            steps {
                echo 'Cloning the repository...'
                git url: 'https://github.com/nouhamorj/SpringBootProject.git', branch: 'master', credentialsId: 'github-pat-credentials'
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images using Docker Compose...'
                sh 'docker-compose -v'  // Verify docker-compose version
                sh 'docker-compose build'  // Build images
            }
        }

        stage('Run Services') {
            steps {
                echo 'Starting Docker Compose services...'
                sh 'docker-compose up -d'  // Run services in detached mode
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                // Add your test steps here
            }
        }

        stage('Scan Vulnerabilities') {
            steps {
                echo 'Scanning vulnerabilities...'
                // Add your vulnerability scanning steps here
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing Docker images to Docker Hub...'
                // Add your Docker Hub push steps here
            }
        }

        stage('Declarative: Post Actions') {
            steps {
                echo 'Stopping and cleaning up Docker Compose services...'
                sh 'docker-compose down --volumes'  // Clean up after the build
            }
        }
    }

    post {
        failure {
            echo 'Pipeline failed!'
        }
    }
}
