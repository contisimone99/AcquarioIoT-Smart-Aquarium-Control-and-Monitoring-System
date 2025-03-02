/*
  ArduinoMqttClient - WiFi Advanced Callback

  This example connects to a MQTT broker and subscribes to a single topic,
  it also publishes a message to another topic every 10 seconds.
  When a message is received it prints the message to the serial monitor,
  it uses the callback functionality of the library.

  It also demonstrates how to set the will message, get/set QoS, 
  duplicate and retain values of messages.

  The circuit:
  - Arduino MKR 1000, MKR 1010 or Uno WiFi Rev.2 board

  This example code is in the public domain.
*/

#include <ArduinoMqttClient.h>
#include <WiFiNINA.h> // for MKR1000 change to: #include <WiFi101.h>
#include <avr/wdt.h>

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
//char ssid[] = "PocketCube-ECCB";        // your network SSID (name)
//char pass[] = "1MAD00DH";    // your network password (use for WPA, or use as key for WEP)

char ssid[] = "WebCube4-M3T5";        // your network SSID (name)
char pass[] = "LJ68YQ46";    // your network password (use for WPA, or use as key for WEP)

// To connect with SSL/TLS:
// 1) Change WiFiClient to WiFiSSLClient.
// 2) Change port value from 1883 to 8883.
// 3) Change broker value to a server with a known SSL/TLS root certificate 
//    flashed in the WiFi module.

const char dom_subdom_service[] = "test/aswf/gpio";
const char clientID[] = "aindout";
const char mqttUser[] = "pissir";
const char mqttPassword[] = "pissir2020";

WiFiClient wifiClient;
MqttClient mqttClient(wifiClient);

//const char broker[]    = "193.206.52.98";
//const char broker[]    = "192.168.20.67";
const char broker[]     = "test.mosquitto.org";
int        port        = 1883;
const char willTopic[] = "arduino/will";
const char requestTopic[]   = "to/all";
const char inTopic[]  = "to/";
const char outTopic[]  = "from/";
const char statusTopic[] = "gm/station/status";

const char description0[] ="{\"test/aswf/gpio\":[\n";
const char description1[] = "{\"name\":\"AN0\",\"type\":\"analogin\"},\n{\"name\":\"AN1\",\"type\":\"analogin\"},\n";
const char description2[] = "{\"name\":\"AN2\",\"type\":\"analogin\"},\n{\"name\":\"AN3\",\"type\":\"analogin\"},\n";
const char description3[] = "{\"name\":\"AN4\",\"type\":\"analogin\"},\n{\"name\":\"AN5\",\"type\":\"analogin\"},\n";
const char description4[] = "{\"name\":\"OUT2\",\"type\":\"booleanout\"},\n{\"name\":\"OUT3\",\"type\":\"booleanout\"},\n";
const char description5[] = "{\"name\":\"OUT4\",\"type\":\"booleanout\"},\n{\"name\":\"OUT5\",\"type\":\"booleanout\"}\n";
const char descriptionend[] = "]}";

const long interval = 10000;
unsigned long previousMillis = 0;
// define sensors on the 6 Analog inputs

int sensor[6] = {A0, A1, A2, A3, A4, A5};

uint32_t x=0;

int count = 0;


void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  } 

  // attempt to connect to Wifi network:
  Serial.print("Attempting to connect to WPA SSID: ");
  Serial.println(ssid);
  
  while (WiFi.begin(ssid, pass) != WL_CONNECTED) {
  // failed, retry
    Serial.print(".");
    delay(5000);
  }

  Serial.println("You're connected to the network");
  //Serial.println();

  // You can provide a unique client ID, if not set the library uses Arduin-millis()
  // Each client must have a unique client ID
  // mqttClient.setId("clientId");

  // You can provide a username and password for authentication
  // mqttClient.setUsernamePassword("username", "password");

  // set a will message, used by the broker when the connection dies unexpectantly
  // you must know the size of the message before hand, and it must be set before connecting

  String willPayload = "oh no!";
  bool willRetain = true;
  int willQos = 1;
  
  mqttClient.beginWill(willTopic, willPayload.length(), willRetain, willQos);
  mqttClient.print(willPayload);
  mqttClient.endWill();
  mqttClient.setId(clientID);
  mqttClient.setUsernamePassword(mqttUser,mqttPassword);


  //Serial.print("Attempting to connect to the MQTT broker: ");
  //Serial.println(broker);
  if (!mqttClient.connect(broker, port)) {
      Serial.print("MQTT connection failed! Error code = ");
      Serial.println(mqttClient.connectError());
      delay(10000);
      wdt_enable(WDTO_2S);
      while(1);
  }

  Serial.println("You're connected to the MQTT broker!");
  Serial.println();

  // set the message receive callback
  mqttClient.onMessage(onMqttMessage);

  //Serial.print("Subscribing to topic: ");
  //Serial.println();

  // subscribe to a topic
  // the second paramter set's the QoS of the subscription,
  // the the library supports subscribing at QoS 0, 1, or 2
  int subscribeQos = 1;
  
  String receiveTopic = String(inTopic)+String(dom_subdom_service)+String("/+");
  // Serial.println(receiveTopic);
  mqttClient.subscribe(receiveTopic.c_str(), subscribeQos);
  mqttClient.subscribe(requestTopic, subscribeQos);

  // topics can be unsubscribed using:
  // mqttClient.unsubscribe(inTopic);

  //Serial.print("Waiting for messages on topic: ");
  //Serial.println(inTopic);
  //Serial.println();
  wdt_enable(WDTO_2S);
}

void loop() {
  int res;
  bool firsttime = true;
  if(firsttime) {
    pinMode(2,OUTPUT);
    pinMode(3,OUTPUT);
    pinMode(4,OUTPUT);
    pinMode(5,OUTPUT);
    pinMode(6,OUTPUT);
    pinMode(7,OUTPUT);
    pinMode(8,OUTPUT);
    firsttime = false;
  }
  wdt_reset();
  // call poll() regularly to allow the library to receive MQTT messages and
  // send MQTT keep alives which avoids being disconnected by the broker
  mqttClient.poll();

  // avoid having delays in loop, we'll use the strategy from BlinkWithoutDelay
  // see: File -> Examples -> 02.Digital -> BlinkWithoutDelay for more info
  unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= interval) {
    // save the last time a message was sent
    previousMillis = currentMillis;

    String payload;
    String source = String(outTopic)+String(dom_subdom_service)+String("/AN");
    source += count;
    // Serial.println(source);
    
    x = analogRead(sensor[count]);
    payload = "{\"event\":\"";
    payload += x;
    payload += "\"}";
    
    // Serial.println(source);
    // Serial.println(payload);

    // send message, the Print interface can be used to set the message contents
    // in this case we know the size ahead of time, so the message payload can be streamed

    bool retained = false;
    int qos = 1;
    bool dup = false;

    mqttClient.beginMessage(source.c_str(), payload.length(), retained, qos, dup);
    mqttClient.print(payload);
    if(mqttClient.endMessage()==0){
        Serial.println("I'm disconnected");
        while (1); 
    };
    count++;
    if(count>5) count = 0;
  }
}

void onMqttMessage(int messageSize) {
  // we received a message, print out the topic and contents
  int rel;
  int val;
  int res;
  bool retained = false;
  int qos = 1;
  bool dup = false;
  String msg;
  String topic = mqttClient.messageTopic();
  // Serial.println(topic);
  while (mqttClient.available()) {
    msg += ((char)mqttClient.read());
  }
  if(topic=="to/all") {
   // Serial.println(mqttClient.messageTopic());

    if(msg=="{\"request\":\"description.json\"}"||msg=="{request:description.json}") {
      //  Serial.println("sending Description");
       msg = description0;
       msg += description1;
       msg += description2;
       msg += description3;
       msg += description4;
       msg += description5;
       msg += descriptionend;
       String descriptionTopic = String(outTopic)+String(dom_subdom_service)+String("/description");
       mqttClient.beginMessage(descriptionTopic.c_str(), msg.length(), retained, qos, dup);
       mqttClient.print(msg);
   
       res = mqttClient.endMessage();
      // Serial.println(descriptionTopic);
    }
  }else {
      //Serial.println(topic);
      rel = topic.substring(topic.indexOf("OUT")+3).toInt();

     // Serial.println(msg);
      sscanf(msg.c_str(),"{\"cmd\":%d}",&val);
      if(val <0 || val >1) sscanf(msg.c_str(),"{cmd:%d}",&val);
      if(val>0 && rel<6 && rel>1) digitalWrite(rel,1);
      else if(rel<6 && rel >1) digitalWrite(rel,0);
      msg = "{\"event\":\"";
      msg += val;
      msg += "\"}";
      topic = String(outTopic)+String(dom_subdom_service)+String("/OUT");
      topic += rel;
      //Serial.println(topic);
      mqttClient.beginMessage(topic.c_str(), msg.length(), retained, qos, dup);
      mqttClient.print(msg);
      res = mqttClient.endMessage();
  }
}
