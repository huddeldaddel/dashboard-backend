# Dashboard (backend)

An intuitive developer dashboard that displays the most important key figures of a sprint. The data is collected from 
Pivotal Tracker and Github, processed and clearly presented. The progress of the sprint, the complexity of the 
individual tickets, the number of open pull requests and the average lifetime of bugs and pull requests can thus be read
at a glance by the members of the development team. 

![Screenshot](/docs/images/Dashboard-full.png)

## Architecture

The dashboard consists of two parts: 

 * a frontend application, which is written in JavaScript (React) and
 * a backend application, which is written in Java (Spring Boot).

![UML Component Diagram](/docs/images/architecture.png)

The [backend application](https://github.com/huddeldaddel/dashboard-backend) implements the integration of the various
APIs (e.g. for Pivotal Tracker and Github). The data obtained from these external services is cached in the backend for
a few minutes so that the dashboard does not encounter the API limits of the providers, even with frequent access. 

The [frontend application](https://github.com/huddeldaddel/dashboard-frontend) retrieves the data from the backend and
displays it on a scaling dashboard view. The representation is roughly similar to that of Geckoboard, so that the
dashboard can be seamlessly integrated into such an environment.

This repository contains the backend code of the dashboard.

## Getting started

### Setup

1) The backend application requires a [MongoDB database](https://mongodb.com). For development purposes it should be 
fine to install a MongoDB with default settings (no auth, default port). This configuration will be detected by Spring
Boot automatically - so that you do not have to configure anything.
2) Make sure you have the typical Java developers setup installed (Java 11, Maven, ...)

### Configuration

The configuration is stored in `/src/main/resources/application.yml`. It defines two profiles (`local` and 
`production`). The production profile is set up to use environment variables for all relevant properties. This is super
useful if you want to deploy the application with [Heroku](https://www.heroku.com) or a similar service. The deployment
on Heroku will be explained in a later step in more detail.

To set these values for local development you can edit / create a settings file for the local profile. Just create
`/src/main/resources/application-local.properties`. In this file you can add values for all required properties - which
should look someting like this:  

`github.owner=<Github team>`  
`github.repositories=<repository name 1>,<repository name 2>`  
`github.token=<Github token>`  
`tracker.project=<Pivotal Tracker project ID>`  
`tracker.token=<Pivotal Tracker token>`

### Running the code

To run the frontend application locally, just type `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`

## Updating the frontend

The backend delivers a static page when opened in the browser. This page contains the JavaScript code of the 
[frontend application](https://github.com/huddeldaddel/dashboard-frontend). If you want to update the version of the
frontend, just follow these steps:

1) Setup the version of the frontend that you would like to use (following the README.md of the frontend project)
2) Run `webpack` in the directory of your local frontend project to build it for production use
3) Open `/dist/bundle.js` and copy it's content
4) Open `/src/main/resources/engineer/thomas_werner/dashboard/controllers` and replace everything between `<script>` 
   and `</script>` with the code you copied in the previous step

## Deployment on Heroku

The dashboard application is preconfigured to be run on Heroku easily. Heroku gives you enough free Dyno Units to run 
the dashboard during normal office hour without any costs. All you need to do is:

1) Fork the repository
2) Create a new app and connect it to this Github repository
3) Turn on auto-deployment if you want to stay up to date
4) Set the environment variables defined in `/src/main/resources/application.yml`
5) Connect a MongoDB service like [mLab MongoDB](https://elements.heroku.com/addons/mongolab). The free tier will do 
   just fine.