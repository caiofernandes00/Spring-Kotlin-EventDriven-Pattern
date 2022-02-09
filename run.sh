#!/usr/bin/env bash

# shellcheck disable=SC2046
PROJECT_HOME=$(pwd)
cd docker-compose
sudo docker rm -f $(docker ps -qa)
sudo docker compose -f common.yaml -f kafka_cluster.yaml -f services.yaml up --force-recreate --build
