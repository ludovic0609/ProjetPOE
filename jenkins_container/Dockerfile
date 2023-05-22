# Utilisez une image de base Jenkins LTS
FROM jenkins/jenkins:lts
# Mettez à jour le système d'exploitation et installez les dépendances nécessaires
USER root
RUN apt-get update && apt-get install -y curl

# Installez Node.js et npm
RUN curl -sL https://deb.nodesource.com/setup_16.x | bash -
RUN apt-get install -y nodejs zip sshpass openjdk-17-jdk vim
RUN apt-get install -y nagios-nrpe-server nagios-plugins
RUN npm install -g @angular/cli


# Vérifiez les versions installées de Node.js et npm
RUN node -v
RUN npm -v

# Rétablissez l'utilisateur Jenkins pour exécuter Jenkins en toute sécurité
USER jenkins
