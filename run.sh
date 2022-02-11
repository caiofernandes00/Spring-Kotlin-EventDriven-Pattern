#!/usr/bin/env bash

# shellcheck disable=SC2164
cd docker-compose
# shellcheck disable=SC2046
sudo docker rm -f $(docker ps -qa)
sudo docker compose -f common.yaml -f kafka_cluster.yaml -f services.yaml up --force-recreate --build
