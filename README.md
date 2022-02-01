# EventDriven Application
The goal of this project is to build an event driven application.

## Used technologies
- Kotlin
- Spring Boot
- Kafka
- Kafka Streams
- Spring Cloud
- Elasticsearch

## Execute the application
To start the application, first you need the dependencies.
```bash
cd docker-compose
docker-compose -f common.yaml -f kafka_cluster.yaml up
```
Now you can execute the application.
```bash
mvn spring-boot:run
```
To help see what's going on inside kafka cluster, I recommend using kafkacat.</br>
But first let's create an alias for the command. Inside your shell, insert the following line:
```bash
alias kcat="docker run --tty --network=host confluentinc/cp-kafkacat kafkacat"
```
After that reset your shell and you should be ready to run the command.
```bash
kcat -L -b localhost:9092
```