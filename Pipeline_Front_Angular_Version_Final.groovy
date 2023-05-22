pipeline {
  agent any
  
  parameters {
    string(name: 'FRONT_GIT_URL', defaultValue: 'https://github.com/matthcol/movie_angular.git', description: 'Github URL of front Angular')
    string(name: 'FRONT_GIT_BRANCH', defaultValue: 'devf1', description: 'Branch to GitHub front Angular')
    string(name: 'BACKEND_BASE_URL', description: 'Base URL of the backend to be connected with the front')
    string(name: 'REPOSITORY', defaultValue: '/var/jenkins_home/repos_front', description: 'Where to deposit the web site ZIP')
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: "${params.FRONT_GIT_BRANCH}", url:"${params.FRONT_GIT_URL}"
      }
    }
    stage('Install dependencies') {
      steps {
        sh 'npm ci'
      }
    }
    stage('Build') {
      steps {
        // Change backend in prod environment file
        sh 'sed -i "s#BACKEND_BASE_URL#$BACKEND_BASE_URL#" src/environments/environment.prod.ts'
        
        // Compiler l'application Angular
        sh 'ng build --prod'
      }
    }
    stage('Copy to Repository') {
      steps {
        script {
          sh "mkdir -p ${params.REPOSITORY}"
          sh "cp -r dist/movie-app/* ${params.REPOSITORY}"
        }
      }
    }
  }
}