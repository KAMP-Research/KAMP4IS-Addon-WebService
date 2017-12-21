######################################################################################################################################
#                                                           KAMP-WS                                                                  #
######################################################################################################################################

The kamp-ws-parent Maven project contains everything you need to build and deploy the kamp-ws-server.
It was implemented in the course of the bachelor's thesis 
    "KAMP for Build Avoidance on Generation of Documentation" 
by Milena Neumann (milena.neumann@student.kit.edu).

     _________________________________________________________________________________________________________________________________                            
    |   
    |   IMPORTANT:
    |   Because Palladio, KAMP4IS and some components they depend on are not Maven projects, 
    |   these dependencies are contained in the repo folder.
    |   Before you can build the kamp-ws-parent project, you need to make sure these dependencies are in your local Maven repository.
    |   In order to do this, you can simply execute setup.bat, which installs all dependencies.
    |_________________________________________________________________________________________________________________________________


The kamp-ws-parent project has the following modules (in build order):

    kamp-ws-server              contains the implementation of the KAMP web service. 
                                After compilation and unit tests passed, a WSDL file is generated from the Java sources 
                                and placed into the target folder, together with a deployable WAR file.

    kamp-ws-client              uses the WSDL file mentioned above to automatically generate client classes for the web service. 
                                These classes are put into a JAR. 
                                Another Maven project can now add this artifact to its dependencies 
                                and then use the API offered by the web service through this client.

    kamp-ws-integration-test    contains integration tests for the web service. 
                                It automatically starts an Apache Tomcat server as a test environment 
                                and then deploys the artifact of kamp-ws-server on it to run these tests.

    kamp-ws-container           puts the WAR file produced by kamp-ws-server into an Apache Tomcat environment. 
                                The PCM of the xServer is added. 

kamp-ws-server requires a Palladio Component Model. The models folder contains two PCMs:
    
    xServer_test                for testing purposes only. 
                                It is used by kamp-ws-server and kamp-ws-integration-test
                                
    xServer                     The PCM of the PTV xServer. 
                                It is used by kamp-ws-container.

     _________________________________________________________________________________________________________________________________                            
    |   
    |   NOTE:
    |   Sometimes, the Maven Tomcat plugin in the kamp-ws-server tests does not release port 8009 fast enough 
    |   and the kamp-ws-integration-test can't start a Tomcat for its tests. 
    |   If your build fails because of this, kill the respective process over the task manager 
    |   and restart the build where it left off with the -rf command.
    |_________________________________________________________________________________________________________________________________
    
In order to start a kamp-ws-container, unpack the zip (which is located in kamp-ws-container\target) and execute the startup.bat in its bin folder.
It will per default provide its WSDL at http://localhost:8080/kamp-ws/services/changeSpecificDependencies?wsdl