pipeline {
    agent any
    
    parameters {
        string(name: 'URL', defaultValue: 'matthcol/movieapi2k3.git', description: 'URL Java Project to Build')
        string(name: 'BRANCH', defaultValue: 'main', description: 'Branch to Build')
        choice(name: 'JAVA_VERSION', choices: ['11', '17'], description: 'Java Version')
        choice(name: 'SERVER_DATABASE', choices: ['Oracle Entreprise', 'Postgres','MS SQL'], description: 'Database data')
        string(name: 'DATABASE_POSTGRES', defaultValue: 'dbmovie', description: 'Name of database for postgres')
        string(name: 'SID_ORACLE', defaultValue: 'ORCLCDB' , description: 'SID of Oracle DB')
        string(name: 'DB_USER', defaultValue: 'movie' ,  description: 'User of Database')
        string(name: 'HOSTNAME_DB', defaultValue: 'db' , description: 'Hostname of database')
        string(name: 'DB_PORT', description: 'Port of  database (postgres: 5432 - oracle: 1521)')
        
        
        
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
        jdk "JDK${params.JAVA_VERSION}"
    }

    stages {
        stage('Git') {
            steps {
                // Get some code from a GitHub repository
                git branch: "${params.BRANCH}", url:"git@github.com-repo-0:${params.URL}"
            }
        }
        
        stage('Config') {
            
            steps {
                
                script {
                    
                    if("${params.JAVA_VERSION}"=='11')
                    {
                        sh "sed -i 's@<java.version>.*</java.version>@<java.version>${params.JAVA_VERSION}</java.version>@' pom.xml"
                    }
                    if("${params.JAVA_VERSION}"=='17')
                    {
                        sh "sed -i 's@<java.version>.*</java.version>@<java.version>${params.JAVA_VERSION}</java.version>@' pom.xml"
                    }
                    
                        
                        
                    if("${params.SERVER_DATABASE}"=='Postgres') {
                        sh "cp pom.xml update_pom.xml"
                        
                        sh "sed -i 's@<!--  jdbc runtime driver: add your dependency here   -->@<dependency><groupId>org.postgresql</groupId><artifactId>postgresql</artifactId><scope>runtime</scope></dependency>@' update_pom.xml"
                        
                        sh "cat update_pom.xml > pom.xml"
                        sh "rm update_pom.xml"
                    }
                    if("${params.SERVER_DATABASE}"=='Oracle Entreprise') {
                        sh "cp pom.xml update_pom.xml"
                        
                        sh "sed -i 's@<!--  jdbc runtime driver: add your dependency here   -->@<dependency><groupId>com.oracle.database.jdbc</groupId><artifactId>ojdbc8</artifactId><scope>runtime</scope></dependency>@' update_pom.xml"
                        
                        sh "cat update_pom.xml > pom.xml"
                        sh "rm update_pom.xml"
                    }
                    
                    
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
        
        stage('Create Directory in jenkins_home') {
      steps {
        script {
          sh 'mkdir -p /var/jenkins_home/repos_back'
	        
        }
      }
    }
    stage('Copy Artifact') {
      steps {
        sh 'cp target/*.jar /var/jenkins_home/repos_back'
      }
    }
    stage('Create application properties with parameters') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'DB_PASSWORD', variable: 'PASSWORD')]) {
                    
                    
                        if("${params.SERVER_DATABASE}"=='Postgres') 
                        {
                            sh "echo 'spring.datasource.url=jdbc:postgresql://${params.HOSTNAME_DB}:${params.DB_PORT}/${params.DATABASE_POSTGRES}\nspring.datasource.username=${params.DB_USER}\nspring.datasource.password=$PASSWORD\nspring.jpa.hibernate.ddl-auto=none\nspring.jpa.show-sql=true' > /var/jenkins_home/repos_back/application.properties"
                        }
                        if("${params.SERVER_DATABASE}"=='Oracle Entreprise') 
                        {
                            sh "echo 'spring.datasource.url=jdbc:oracle:thin:@${params.HOSTNAME_DB}:${params.DB_PORT}/${params.SID_ORACLE}\nspring.datasource.username=${params.DB_USER}\nspring.datasource.password=$PASSWORD\nspring.jpa.hibernate.ddl-auto=none\nspring.jpa.show-sql=true' > /var/jenkins_home/repos_back/application.properties"
                        }
                    }
                }
            
            }
        }
        
          
    }
}