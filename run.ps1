cd docker-compose
docker rm -f $(docker ps -qa)
docker compose -f common.yaml -f kafka_cluster.yaml -f services.yaml up --force-recreate --build
