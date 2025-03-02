CloudManager.jar: Conf.class Delete.class HtmlPage.class Install.class JsonBody.class Publisher.class Start.class Stop.class RESTHttpServer.class
	
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: ../org.eclipse.paho.client.mqttv3.jar ../json-20210307.jar " >> manifest.txt
	@echo "Main-Class: RESTHttpServer" >> manifest.txt
	@echo "" >> manifest.txt

	jar -cmf manifest.txt CloudManager.jar ./Conf.class ./HtmlPage.class ./Delete.class ./Install.class ./JsonBody.class  ./Publisher.class ./Start.class ./Stop.class ./RESTHttpServer.class 

Conf.class:   ./Conf.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./Conf.java
	
Delete.class:   ./Delete.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./Delete.java
	
HtmlPage.class:   ./HtmlPage.java 
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./HtmlPage.java

Install.class:    ./Install.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./Install.java

JsonBody.class:    ./JsonBody.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./JsonBody.java

Publisher.class:    ./Publisher.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./Publisher.java
   
Start.class:    ./Start.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./Start.java
	
Stop.class:    ./Stop.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./Stop.java
	
RESTHttpServer.class:    ./RESTHttpServer.java
	javac -cp .:../org.eclipse.paho.client.mqttv3.jar:../json-20210307.jar:. ./RESTHttpServer.java


