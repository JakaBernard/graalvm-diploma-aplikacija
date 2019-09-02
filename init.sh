#!/bin/bash
gu install R native-image
mvn install
docker run --name postgres-diploma -e POSTGRESS_PASSWORD=postgres -e POSTGRES_DB=loyalty -p 5432:5432 -d postgres
# we need to "seed" the database because named queries don't parse otherwise
# sleep 5
# docker cp ./build-db.sql postgres-diploma:/build-db.sql
# docker exec -u postgres postgres-diploma psql loyalty postgres -f /build-db.sql

