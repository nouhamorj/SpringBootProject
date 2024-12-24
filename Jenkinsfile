pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    userRemoteConfigs: [[
                        url: 'git@github.com:nouhamorj/SpringBootProject.git',
                        credentialsId: 'github-ssh-key'
                    ]]
                ])
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def dockerImage = docker.build("nouhamorj/springboot-project:latest")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_PASSWORD'
                    )]) {
                        sh """
                            echo \${DOCKERHUB_PASSWORD} | docker login -u \${DOCKERHUB_USERNAME} --password-stdin
                            docker push nouhamorj/springboot-project:latest
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                sh 'docker logout || true'
            }
        }
        success {
            echo 'Pipeline terminé avec succès.'
        }
        failure {
            echo 'Le pipeline a échoué. Veuillez vérifier les logs pour plus de détails.'
        }
    }
}