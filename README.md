# ratpack-hello-world
Ratpack.groovy runs out of the box <br>
but there will be several steps during setup that need to happen to allow spock testing to work in Intellij Idea
  - under settings/preferences -> Build, Execution, Deployment -> Build Tools -> Gradle: 
      - Windows Shortcut ( Ctrl + Alt + s )
      - Mac Shortcut ( Command + , )
    - you will need to change the following: 
      - Build and run using: IntelliJ IDEA
      - Run tests using: IntelliJ IDEA
  - under Project Structure/Project Settings -> Modules
      - Windows Shortcut ( Ctrl + Alt + Shift + s )
      - Mac Shortcut ( Command + ; )
    - you will need to change the following:
      - Inside the center-left column you should see ratpack-hello-world folder, open the dropdown arrow to show the test folder
        - click on the test folder
        - in the center column under the Source tab
          - open the functional folder
          - click on the groovy folder and set as a Test folder
        - do the same for the unit folder 
        - you should be able to run the FunctionalSpec file inside the groovy folder by right clicking and pressing the play button
    
