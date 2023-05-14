# Instruction for Dockerfile to create a new image on top of the base image (debian)

FROM debian:11
RUN apt-get update && \
    apt-get install -y openssh-server && \
    mkdir /var/run/sshd && \
    echo 'root:password' | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    # SSH login fix. Otherwise user is kicked off after login
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    # SSH port
    echo "Port 22" >> /etc/ssh/sshd_config && \
    # allow SSH access to non-root user
    useradd -ms /bin/bash pipeline && \
    echo 'pipeline:password' | chpasswd

# copy your public key to the authorized keys file of the sshuser
#COPY id_rsa.pub /home/sshuser/.ssh/authorized_keys

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
