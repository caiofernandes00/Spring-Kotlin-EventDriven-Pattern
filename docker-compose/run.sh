#!/usr/bin/env bash

sudo docker compose -f common.yaml -f kafka_cluster.yaml up --force-recreate --build