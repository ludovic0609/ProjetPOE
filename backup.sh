#!/bin/bash
DateDay=$(date '+%Y-%m-%d')
USER_SYSTEM=system
USER_PASSWORD=password
ORACLE_SID=ORCLCDB
DIRECTORY_PATH_BACKUP="/opt/oracle/admin/ORCLCDB/dpdump"
NAME_DOCKER_COMPOSE_ORACLE=movie_api_projet_oracle_db_1
docker exec -it $NAME_DOCKER_COMPOSE_ORACLE /bin/bash -c "expdp $USER_SYSTEM/$USER_PASSWORD@$ORACLE_SID full=Y  dumpfile=FULL_DATA_BASE_$DateDay.dmp"

mkdir -p ora_backup
cd ora_backup
docker cp $NAME_DOCKER_COMPOSE_ORACLE:$DIRECTORY_PATH_BACKUP/FULL_DATA_BASE_$DateDay.dmp .
