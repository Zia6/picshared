#FROM ubuntu:latest
#LABEL authors="Z2023"
#
#ENTRYPOINT ["top", "-b"]


version: '3.9'

services:

  mysql:
    image: mysql:8
    container_name: picshare-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: picshared
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:latest
    container_name: picshare-redis
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3-management
    container_name: picshare-rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest

  picshare:
    build:
      context: ./picshare
      dockerfile: Dockerfile
    container_name: picshare-app
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
      - rabbitmq
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/picshared?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_REDIS_HOST: redis
      SPRING_RABBITMQ_HOST: rabbitmq

volumes:
  mysql_data:
