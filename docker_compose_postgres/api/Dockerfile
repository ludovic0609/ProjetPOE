FROM debian:11
RUN apt-get update && \
    apt-get install -y openssh-server && \
    apt-get install -y openjdk-17-jre && \
    apt-get install -y nagios-nrpe-server && \
    apt-get install -y nagios-plugins && \
    mkdir /var/run/sshd && \
    echo 'root:password' | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    # SSH login fix. Otherwise user is kicked off after login
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    # SSH port
    echo "Port 22" >> /etc/ssh/sshd_config && \
    # allow SSH access to non-root user
    useradd -ms /bin/bash springboot && \
    echo 'springboot:password' | chpasswd

COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
EXPOSE 8081/tcp
CMD [ "java", "-jar", "movieapi.jar" ]
