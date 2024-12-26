pipeline {
    agent any

    /*environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE = 'nouhamorj/springbootproject-master-app'
        /*DOCKER_TAG = "${BUILD_NUMBER}"
        LATEST_TAG = 'latest'
        // Configuration MySQL
        MYSQL_DATABASE = 'formation'
        MYSQL_ROOT_PASSWORD = ''
    }*/
     stages {
            stage('Build') {
                steps {
                    script {
                        docker.build(DOCKER_IMAGE)
                    }
                }
            }

            stage('Scan for Vulnerabilities') {
                steps {
                    script {
                        sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${DOCKER_IMAGE}'
                    }
                }
            }

            stage('Push to Docker Hub') {
                steps {
                    script {
                        docker.withRegistry('', DOCKER_CREDENTIALS) {
                            docker.image(DOCKER_IMAGE).push()
                        }
                    }
                }
            }
     }
}