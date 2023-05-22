pipeline {
    agent any

    parameters {
        string(name: 'URL_GITHUB_SCRIPT', defaultValue: 'https://github.com/matthcol/dbmovie.git', description: 'URL Github Script USER, TABLE + DATA')
        choice(name: 'DATABASE_SCRIPT', choices: ['Oracle', 'Postgres'], description: 'SQL Script')
        string(name: 'REPOSITORY', defaultValue: '/var/jenkins_home/repos_back', description: 'Where to depose the script database')
    }
    stages {
        stage('Git') {
            steps {
                script {
                    if("${params.DATABASE_SCRIPT}"=='Oracle')
                    {
                        git branch: "ora-nodocker", url:"${params.URL_GITHUB_SCRIPT}"

                    }
                    if("${params.DATABASE_SCRIPT}"=='Postgres')
                    {
                        git branch: "docker-pg", url:"${params.URL_GITHUB_SCRIPT}"
                    }
                }


            }
        }
        stage('Create Directory in jenkins_home and copy sql script') {
        steps {
        script {
                if("${params.DATABASE_SCRIPT}"=='Oracle')
                {
                    sh "mkdir -p ${params.REPOSITORY}/oracle"
                    sh "mv sql/* ${params.REPOSITORY}/oracle/"

                }
                if("${params.DATABASE_SCRIPT}"=='Postgres')
                {
                    sh "mkdir -p ${params.REPOSITORY}/postgres"
                    sh "mv sql/* ${params.REPOSITORY}/postgres/"
                }


            }
      }
    }


    }

}