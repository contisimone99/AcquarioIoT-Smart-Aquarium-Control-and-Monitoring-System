ArchiverAcquario.jar: ArchiverMain.class Archiver.class ArchiverPub.class ArchiverSub.class ArchiverCallback.class  Utils.class
	
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: org.eclipse.paho.client.mqttv3.jar gson-2.6.2.jar java-json.jar bcpkix-jdk13-167.jar bcprov-jdk13-167.jar" >> manifest.txt
	@echo "Main-Class: codice/ArchiverMain" >> manifest.txt
	@echo "" >> manifest.txt

	jar -cmf manifest.txt ArchiverAcquario.jar ./de/dcsquare/paho/client/util/Utils.class ./codice/ArchiverMain.class ./codice/Archiver.class ./codice/ArchiverPub.class ./codice/ArchiverSub.class ./codice/ArchiverCallback.class 

ArchiverPub.class:   ./codice/ArchiverPub.java 
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcpkix-jdk13-167.jar:bcprov-jdk13-167.jar:. ./codice/ArchiverPub.java

Utils.class:	./de/dcsquare/paho/client/util/Utils.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcpkix-jdk13-167.jar:bcprov-jdk13-167.jar:. ./de/dcsquare/paho/client/util/Utils.java
	
Archiver.class:   ./codice/Archiver.java 
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcpkix-jdk13-167.jar:bcprov-jdk13-167.jar:. ./codice/Archiver.java
	
ArchiverSub.class:    ./codice/ArchiverSub.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcpkix-jdk13-167.jar:bcprov-jdk13-167.jar:. ./codice/ArchiverSub.java

ArchiverCallback.class:    ./codice/ArchiverCallback.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcpkix-jdk13-167.jar:bcprov-jdk13-167.jar:. ./codice/ArchiverCallback.java

ArchiverMain.class:    ./codice/ArchiverMain.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcpkix-jdk13-167.jar:bcprov-jdk13-167.jar:. ./codice/ArchiverMain.java
	


