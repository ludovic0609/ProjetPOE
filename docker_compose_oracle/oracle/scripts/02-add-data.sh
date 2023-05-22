#!/bin/bash
ORACLE_USER=movie
ORACLE_PASSWORD_USER=password
SCRIPT_SQL=@movie_all.sql

cd /opt/oracle/scripts/setup/sql
sqlplus $ORACLE_USER/$ORACLE_PASSWORD_USER $SCRIPT_SQL > /dev/null
