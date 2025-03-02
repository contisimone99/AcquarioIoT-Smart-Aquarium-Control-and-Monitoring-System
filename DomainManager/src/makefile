DomainManager.jar: ApiRestHttpHandler.class DeleteHttpHandler.class KeycloakHandler.class StartHttpHandler.class StopHttpHandler.class JDBCDriver.class InstallHttpHandler.class RestHttpServer.class
	
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: ../org.eclipse.paho.client.mqttv3.jar ../json-20210307.jar ../sqlite-jdbc-3.20.1.jar" >> manifest.txt
	@echo "Main-Class: RestHttpServer" >> manifest.txt
	@echo "" >> manifest.txt

	jar -cmf manifest.txt DomainManager.jar ApiRestHttpHandler.class DeleteHttpHandler.class KeycloakHandler.class InstallHttpHandler.class RestHttpServer.class StartHttpHandler.class StopHttpHandler.class JDBCDriver.class 

ApiRestHttpHandler.class:	ApiRestHttpHandler.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar ApiRestHttpHandler.java
	
DeleteHttpHandler.class:	DeleteHttpHandler.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar DeleteHttpHandler.java
	
InstallHttpHandler.class:	InstallHttpHandler.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar InstallHttpHandler.java

KeycloakHandler.class:	KeycloakHandler.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar KeycloakHandler.java

RestHttpServer.class:	RestHttpServer.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar RestHttpServer.java
   
StartHttpHandler.class:	StartHttpHandler.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar StartHttpHandler.java
	
StopHttpHandler.class:	StopHttpHandler.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar StopHttpHandler.java

JDBCDriver.class:	JDBCDriver.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../sqlite-jdbc-3.20.1.jar:../json-20210307.jar JDBCDriver.java
	
