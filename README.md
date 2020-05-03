# TeamApps Demo Lessons

Learn how you can build complex Web-Applications within minutes using [TeamApps](https://github.com/teamapps-org/teamapps).

The Lessons are organised in Packages. Each `Demo` class can be run through its main class.
All Lessons are included in the `DemoLessonsApp`

## Requirements

* Knowledge of Java
* Interest in learning new concepts
* Maven
* JDK 11+
* IntelliJ IDEA or some other Java IDE

## First steps using IntelliJ IDEA

To open this project from Github directly in IntelliJ IDEA use the following steps: 

* Start IntelliJ IDEA
* File > New > Project from Version Control...
* Paste git URL of this Project (On Github click the green Button above "Clone or download", then copy the url)
* Click Clone in IntelliJ dialogue

Project will be cloned to the specified directory and opened. 

### Import Maven Project

First we need to install the necessary dependencies using Maven. This can take some time if this is the first time you use TeamApps.

* IntelliJ will prompt you to Import the Maven project or automatically import it.
* In your IntelliJ IDEA Workspace, open the Maven Toolbox on the right.
* Doubleclick on demo-lessons > Lifecycle > install

If all works right, you should see BUILD SUCCESS in the Console window

### Mark directories for IDEA

To enable Auto completion and Validation, IDEA has to know what kind of content is in some Folders.

* Right click `src/main/model` > Mark Directory as > Sources Root
* Right click `target/generated-sources/model-api` > Mark as Generated Sources Root

### Running the DemoLessonsApp

Now we can start the DemoLessonsApp from IntelliJ IDEA

* In the Project structure (left side oft the window) open `teamapps-demolessons/src/main/java/org.teamapps.demolessons`
* Here you see the packages for the individual lessons and at the bottom you see the `DemoLessonsApp` Class
* Right Click > `Debug 'DemoLessonsApp.main()'`
* IntelliJ will compile and start the project.
* In the console you should then see the following output in red:

~~~log
[main] INFO org.eclipse.jetty.server.AbstractConnector - Started ServerConnector@491666ad{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
[main] INFO org.eclipse.jetty.server.Server - Started @4176ms
~~~

This means that the DemoLessonsApp is running and the Server is listening on Port 8081.
You can access the Application using your Browser on: https://localhost:8081

### Running a single Lesson 

* Open the `...Demo` Class in its package. E.g. `teamapps-demolessons/src/main/java/org.teamapps.demolessons/p1_intro/l01_panel/PanelDemo`
* Right click on the Class and click on `Debug 'PanelDemo.main()'`

This will start another Server on Port 8080. You can access a single DemoLesson using your Browser on: https://localhost:8080

You can only start one Demo at once, you need to stop the running Demo before you start another App on Port 8080. Otherwise you will get the following exception:

~~~log
Exception in thread "main" java.io.IOException: Failed to bind to 0.0.0.0/0.0.0.0:8080
[...]
Caused by: java.net.BindException: Address already in use
~~~

## License

The [TeamApps Framework](https://github.com/teamapps-org/teamapps) is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
