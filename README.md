# Phone Book App
A simple App to keep track of your Phone Contacts

## Project Base Structure
This Maven project contains three base models that we will enumerate and describe next.
### `backend`
A Jersey Application that will handle the Backend. It will use MySQL as the persistence layer. This backend will be deployed as a Servlet in a Jetty Web Server.

### `frontend`
A React.js Application that will handle the Frontend. It will use MySQL as the persistence layer. It will be deployed in a NGINX Web Server.

### `system-tests`
A module containing system level tests whose purpose is to test each one of the other modules end-to-end. For these tests to run, the docker environment must be running. At the moment this module only contains API level tests using RestAssured to the backend API.

## Docker Environment
To run the project we use Docker Compose, which contains the following containers:
* backend: uses Jetty.
* frontend: uses NGINX.
* mysql

## Starting the Project
There is a `Makefile` present with several handy targets. To start the project you should use `make install start`, which will build the project by also running the unit tests and will spin up the containers. Once the project is running, you can also use `make system-tests` to run the System Tests.

The Phone Book app will be running on [http://localhost:8080](http://localhost:8080).

`Note:` You will need [Maven](https://maven.apache.org/) and [Docker](https://www.docker.com/).
