#!/bin/bash
ORACLE_USER=movie
ORACLE_PASSWORD_USER=password
sqlplus / as sysdba <<EOF
alter session set "_ORACLE_SCRIPT"=true;
create user $ORACLE_USER identified by $ORACLE_PASSWORD_USER;
grant connect, resource to movie;
alter user $ORACLE_USER quota unlimited on users;
exit;
EOF

