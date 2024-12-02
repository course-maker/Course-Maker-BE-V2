#!/bin/bash

docker compose --profile required down
docker compose --env-file .env --profile required up -d