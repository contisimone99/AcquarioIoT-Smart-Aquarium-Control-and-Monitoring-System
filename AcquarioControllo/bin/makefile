Controllo.jar: PubControllo.class SubControllo.class SubControlloCallback.class Controllo.class Persistenza.class Utils.class Log.class Alimentatori.class SensoreLuce.class SensoreTemperatura.class MainC.class
	
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: org.eclipse.paho.client.mqttv3.jar gson-2.6.2.jar java-json.jar bcprov-jdk13-167.jar bcpkix-jdk13-167.jar" >> manifest.txt
	@echo "Main-Class: controllo/MainC" >> manifest.txt
	@echo "" >> manifest.txt

	jar -cmf manifest.txt Controllo.jar ./de/dcsquare/paho/client/util/Utils.class ./controllo/Controllo.class ./controllo/Persistenza.class ./controllo/PubControllo.class ./controllo/SubControllo.class ./controllo/SubControlloCallback.class ./sensori/SensoreTemperatura.class ./sensori/SensoreLuce.class ./sensori/Alimentatori.class ./log/Log.class ./controllo/MainC.class 

PubControllo.class:   ./controllo/PubControllo.java 
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./controllo/PubControllo.java
	
Persistenza.class:   ./controllo/Persistenza.java 
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./controllo/Persistenza.java
	
Controllo.class:   ./controllo/Controllo.java 
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./controllo/Controllo.java

SubControllo.class:    ./controllo/SubControllo.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./controllo/SubControllo.java

SubControlloCallback.class:    ./controllo/SubControlloCallback.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./controllo/SubControlloCallback.java

Log.class:    ./log/Log.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./log/Log.java
   
SensoreTemperatura.class:    ./sensori/SensoreTemperatura.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./sensori/SensoreTemperatura.java
	
SensoreLuce.class:    ./sensori/SensoreLuce.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./sensori/SensoreLuce.java
	
Alimentatori.class:    ./sensori/Alimentatori.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./sensori/Alimentatori.java

MainC.class:    ./controllo/MainC.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:java-json.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./controllo/MainC.java
	
Utils.class:	./de/dcsquare/paho/client/util/Utils.java
	javac -cp .:org.eclipse.paho.client.mqttv3.jar:gson-2.6.2.jar:java-json.jar:bcprov-jdk13-167.jar:bcpkix-jdk13-167.jar:. ./de/dcsquare/paho/client/util/Utils.java

