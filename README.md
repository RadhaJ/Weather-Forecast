#UI to Dynamic Using Service API

          Weather Forecaster
In this assignment I created UI which uses asynchronous JavaScript requests to send the user-inputted data to the REST API built, receive the structured results and then published them into the page without refreshing the whole page again.

          Languages/Frameworks
Language - Java,Javascript,AJAX,Jquery,HTML FrameWork - MVC (Servlets) Server - Amazon EC2

To run the application just follow the below Url

URL - http://54.214.80.5:8080/Project2

          Creating a local version
Download the source code from github.
Open the project using Eclipse.
Build the project and press Run to run it locally.

          To host the project on a Web-Server
Create aN Amazon EC2 account and log into the portal.
Create an instance using Linux AMI.
Install Tomcat server on the instance (in putty).
Export the project from Eclipse as a .war file.
Push the .war file into the server.
Start tomcat and run the url(public dns) of the instance in the browser.

          How to build code
Create HTML page with date picker in it.
Create Javascript to perform validations like not accepting null data, invalid date.
Create Javascript with AJAX calls to API urls and fetch JSON data and send data to charts made.
Include Highcharts spline charts and send JSON data to charts.
