pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {
        stage('Git') {
            steps {
                // Get some code from a GitHub repository
                git  branch: 'main', url:'git@github.com:matthcol/movieapi2k3.git'
            }
        }
        stage('Config') {
            
            steps {
                
                script {
                    
                       sh "sed -i 's@<java.version>.*</java.version>@<java.version>11</java.version>@' pom.xml"
                    
                }
            }
            
        }
        
        stage('Compile') {
            steps {
                // Run Maven on a Unix agent.
                sh "mvn clean compile"

                // To run Maven on a Windows agent, use  // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        stage('Package') {
            steps {
                sh "mvn -DskipTests -Dmaven.test.skip package"
            }
            post {
                success {
                     archiveArtifacts 'target/*.jar'
                
                }

            }
        }
        stage('Generate SSH Key Pair') {
            steps{
                    script {
                        if (!fileExists('/var/jenkins_home/.ssh/id_rsa_artifact')) {
                            sh 'ssh-keygen -t rsa -b 4096 -f /var/jenkins_home/.ssh/id_rsa_artifact -N ""'
                        }
                    }
                
            }
        }
        stage('Copy SSH KEY') {
            steps{
                script{
                    sh 'sshpass -f /var/jenkins_home/password.txt ssh-copy-id -o StrictHostKeyChecking=no -p 22  -i /var/jenkins_home/.ssh/id_rsa_artifact.pub pipeline@172.17.0.4'
                }
            }
        }
        stage('Create Directory For Artifact') {
            steps {
                script {
                    sh 'ssh -i /var/jenkins_home/.ssh/id_rsa_artifact pipeline@172.17.0.4 mkdir /home/pipeline/artifacts/'
                }
            }
        }
            stage('Copy Artifact') {
            steps {
                script {
                    
                    sh 'scp -o StrictHostKeyChecking=no -i /var/jenkins_home/.ssh/id_rsa_artifact -P 22  target/*.jar pipeline@172.17.0.4:/home/pipeline/artifacts/'
                }
            }
        }
    }
}