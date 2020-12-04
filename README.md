# spring-boot-solace

## Description

Simple spring boot application to connect to a Solace PubSub+ message broker.

## Getting Started

### Installing

```
 docker pull solace/solace-pubsub-standard
 docker run -d -p 8080:8080 -p 55555:55555 --shm-size=1g --env username_admin_globalaccesslevel=admin --env username_admin_password=admin --name=solace solace/solace-pubsub-standard
```

## Authors

Oliver W. - email: oliverwaefler@gmail.com
