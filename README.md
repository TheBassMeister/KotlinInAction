# KotlinInAction
Hello and welcome visitor to the burgercloud project,

this repo is just a personal exercise to hone my skills in both Spring Boot and Kotlin. 
The code is very much inspired by the Craig Wall's book Spring in Action. Although I did
change a thing or two.

Technologies used in this project: Angular for the frontend, Kotlin as the back end language,
Gradle as the build tool, MongoDB as the backend. The back end and middle layer are all bound together
by Spring Boot. This application is reactive (Webflux).

If you want to try out the code you will need a mongo DB service running. The application.yml expects the MongoDB
server to be running at port 27017 of your localhost. Of course you can adjust it if needed.
After the MongoDB service is running you should be able to run the BurgerCloudApplication's main method to 
start up this application.

After the startup you should be able to access the REST Api under http://localhost:8080/api
To use the UI you will need to execute the ng serve --open command in the UI folder. The UI should
automatically open in your browser.

Please note that this is just an incomplete test application with a lot of issues.
For example the customer and order endpoint should not be exposed. The reason that they
still are is that the customer part in the UI is not implemented. I might do that later.
Optimally the development config should not need to recreate the data all the time.
These and other things might be addressed in the future.









