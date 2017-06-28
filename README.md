Example Project for chat using Websocket
==============================================================================================
Author: Leandro Lima

Technologies: JavaEE7, AngularJs, Node.JS, Gulp, Bower


Development requirements
-------------------

Java 8 Jdk or newer. 

Wildfly 10 or newer. (Any JavaEE 7 Appserver will do)

Maven 3.3.3 or newer. 

Nodejs 6.9.x  

Npm 3.10.x 

Gulp 3.X or newer

Gulp CLI 1.3 or newer

Bower 1.6.x or newer


Build setup
-------------------------
   

Install Gulp, Gulp CLI and Bower

    $ cd ./bitchat-frontend
    $ npm install -g gulp
    $ npm install -g gulp-cli
    $ npm install -g bower


After install all plugins, execute this

    $ npm install
    $ bower install

Change a backend and websocket URL in index.js

    .constant('BackendUrl', 'http://localhost:8080/bitchat')
    .constant('WebsocketUrl', 'ws://localhost:8080/bitchat')

Execute the frontend project in production mode:

    $ gulp serve:dist

And now build the backend project

    $ cd ../bitchat-backend
    $ mvn clean install

And deploy on Wildfly.

It's works fine ;)


