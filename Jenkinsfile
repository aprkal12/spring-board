pipeline {
  agent { label 'ec2-agent' }

  environment {
    REGISTRY_CREDENTIALS = credentials('dockerhub-cred-id') // 도커허브 ID/PW
    DOCKER_IMAGE = "ehdehd0175/board-app:latest"
    REPO = "ehdehd0175/board-app"
    EC2_HOST = "ubuntu@3.34.130.77"
    DB_URL = "jdbc:mariadb://61.255.58.189:13306/board"
    DB_USERNAME = "admin"
    SSH_PORT = "222"
  }

  stages {
    stage('Checkout') {
      steps {
        git credentialsId: 'github-token', url: 'https://github.com/aprkal12/spring-board', branch: 'main'
      }
    }

    stage('Build') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew clean build'
      }
    }

    stage('Docker Build & Push') {
      steps {
        script {
          sh """
            docker build -t $DOCKER_IMAGE .
            echo $REGISTRY_CREDENTIALS_PSW | docker login -u $REGISTRY_CREDENTIALS_USR --password-stdin
            docker push $DOCKER_IMAGE
          """
        }
      }
    }

    stage('Deploy to EC2') {
      steps {
        sshagent(credentials: ['ec2-ssh']) {
          withCredentials([string(credentialsId: 'db-password', variable: 'DB_PASSWORD')]) {
            sh """
              ssh -p $SSH_PORT -o StrictHostKeyChecking=no $EC2_HOST '
                cd ~/spring &&
                echo "MARIADB_URL=$DB_URL" > .env &&
                echo "MARIADB_USERNAME=$DB_USERNAME" >> .env &&
                echo "MARIADB_PASSWORD=$DB_PASSWORD" >> .env &&
                docker compose pull &&
                docker compose down &&
                docker compose up -d
              '
            """
          }
        }
      }
    }
  }
}
