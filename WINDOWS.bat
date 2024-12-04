@echo off

docker compose --profile required down
docker compose --profile required up -d
