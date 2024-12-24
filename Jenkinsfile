pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
    }
    stages {
        stage('Checkout') {
            steps {
                node {  // Ajout du node block ici
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
        }
        stage('Build Docker Image') {
            steps {
                node {  // Ajout du node block ici
                    script {
                        sh 'docker build -t nouhamorj/springboot-project:latest .'
                    }
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                node {  // Ajout du node block ici
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: 'dockerhub-credentials',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh '''
                                echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                                docker push nouhamorj/springboot-project:latest
                            '''
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            node {  // Ajout du node block ici
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