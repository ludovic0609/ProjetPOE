#!/bin/bash

#Nombre d'arguments

NUMBER_ARGS=1
#docker database a copier
DB=$1
#Nom du docker jenkins
NAME_DOCKER_JENKINS=jenkins
DIRECTORY_REPOS_BACK="/var/jenkins_home/repos_back"
DIRECTORY_REPOS_BACK_ORACLE_SQL="/var/jenkins_home/repos_back/oracle/."
DIRECTORY_REPOS_BACK_POSTGRES_SQL="/var/jenkins_home/repos_back/postgres/."
JAVA_JAR_NAME=movieapi.jar
APPLICATION_PROPERTIES_NAME=application.properties

DIRECTORY_DOCKER_COMPOSE_ORACLE="docker_compose_oracle/"
DIRECTORY_DOCKER_COMPOSE_POSTGRES="docker_compose_postgres/"

#si le nombre d'arguments est différent de 1
if [ "$#" -ne "$NUMBER_ARGS" ]; then
	echo "wrong number of arguments"
	exit 1
fi

#si le nombre d'arguments est différent de 1
if (("$1"!="oracle")) || (("$1"!="postgres")); then
	echo "wrong argument, use oracle or postgres"
	exit 1
fi

if [ "$1" == "oracle" ]; then
    # oracle db
    # on copie le java jar et application.properties avec les bonnes informations
    docker cp $NAME_DOCKER_JENKINS:$DIRECTORY_REPOS_BACK/$JAVA_JAR_NAME $DIRECTORY_DOCKER_COMPOSE_ORACLE/api/
    docker cp $NAME_DOCKER_JENKINS:$DIRECTORY_REPOS_BACK/$APPLICATION_PROPERTIES_NAME $DIRECTORY_DOCKER_COMPOSE_ORACLE/api/

    docker cp $NAME_DOCKER_JENKINS:$DIRECTORY_REPOS_BACK_ORACLE_SQL $DIRECTORY_DOCKER_COMPOSE_ORACLE/oracle/scripts/sql/

fi

if [ "$1" == "postgres" ]; then
   # postgres db
   # on copie le java jar et application.properties avec les bonnes informations
   docker cp $NAME_DOCKER_JENKINS:$DIRECTORY_REPOS_BACK/$JAVA_JAR_NAME $DIRECTORY_DOCKER_COMPOSE_POSTGRES/api
   docker cp $NAME_DOCKER_JENKINS:$DIRECTORY_REPOS_BACK/$APPLICATION_PROPERTIES_NAME $DIRECTORY_DOCKER_COMPOSE_POSTGRES/api/

   docker cp $NAME_DOCKER_JENKINS:$DIRECTORY_REPOS_BACK_POSTGRES_SQL $DIRECTORY_DOCKER_COMPOSE_POSTGRES/postgres/sql/
fi
