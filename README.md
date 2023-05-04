J.B. Hunt Hackathon Readme
==========================

Introduction
------------

Welcome to the README file for the J.B. Hunt Hackathon API. This document provides an overview of the backend server, its setup, and usage instructions. Note that this file also contains instructions to run the client side of the J.B. Hunt platform.

Application Purpose
-------------------

*   Users should be able to register for an upcoming Hackathon (Participant)
*   Users should be able to create and manage teams (Team)
*   Users should be able to judge teams (Judge)
*   Users should be able to manage Hackathons (Admin)
*   Users should be able to manage the prizes page(Admin)
*   User should be able to access external resources (Participants)

Getting Started
---------------

To run the application, the following is needed

*   Java 11
*   Maven 3.6.3
*   Add necessary environment variables - values provided by devs upon request
     * username
     * password
     * email-apikey

Installation
------------

1.  Clone the backend repository:

            `git clone https://github.com/alison-nicole/api-jbhackathon-backend-UARK`


2.  Clone the frontend repository:

            `git clone https://github.com/alison-nicole/app-jbhackathon-frontend-UARK`



Server Configuration
--------------------

Set up the run configuration:

1.  In your IDE, open the "Edit configuration" tool
2.  Add a new configuration and select "Spring Boot"
3.  Name the configuration "Application"
4.  For "Run on", select "Local machine" to run the server locally
5.  For "Build and run" select Java 11
6.  For "Active profiles" type "dev"
7.  Go to the "Modify options" and select "Override configurations properties"
8.  In the configuration properties, add the username, password, and email-api key provided by the developers
9.  Apply changes and hit ok

This should take care of the configuration file to run the server.

Server Usage
------------

To run the server locally, just run the Application configuration file that was set up in the Configuration step. Once the server is running locally, it can receive and handle requests from the client side.

Client Usage
------------

To run the Angular client locally, open a terminal and enter the 'ng serve' command. This should run the client and provide access to an URL of the J.B. Hunt Hackathon website. Once the client is running, it can send requests to the server.