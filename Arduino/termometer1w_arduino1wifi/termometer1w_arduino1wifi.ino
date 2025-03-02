/* Reineu Project Temperature Meters
 Program developed by DISIT - Computer Science Section
 */
 
#include <ArduinoMqttClient.h>
#include <WiFiNINA.h> // for MKR1000 change to: #include <WiFi101.h>
#include <avr/wdt.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#define ONE_WIRE_BUSA 10
#define ONE_WIRE_BUSB 3

#define WDTO_16S 15
#define WDTO_8S 9
#define WDTO_4S 8

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
//char ssid[] = "PocketCube-ECCB";        // your network SSID (name)
//char pass[] = "1MAD00DH";    // your network password (use for WPA, or use as key for WEP)

//char ssid[] = "BeagleBone-165A";        // your network SSID (name)
char ssid[] = "WebCube4-M3T5";
char pass[] = "LJ68YQ46";    // your network password (use for WPA, or use as key for WEP)

// To connect with SSL/TLS:
// 1) Change WiFiClient to WiFiSSLClient.
// 2) Change port value from 1883 to 8883.
// 3) Change broker value to a server with a known SSL/TLS root certificate
//    flashed in the WiFi module.
const char clientID[] = "aindout";
const char mqttUser[] = "pissir";
const char mqttPassword[] = "pissir2020";

WiFiClient wifiClient;
MqttClient mqttClient(wifiClient);

//const char broker[]    = "193.206.52.98";
//const char broker[]    = "192.168.8.1";
const char broker[]     = "test.mosquitto.org";

int        port        = 1883;
const char willTopic[] = "arduino/will";
const char requestTopic[]   = "to/all";
const char inTopic[]  = "to/";
const char outTopic[]  = "from/";

// To change domain and subdomain edit the following assignments
const char domain[] = "test";
const char subdomain[] = "tgroup1";
const char service[] = "gpio";
//
const char statusTopic[] = "gm/station/status";


String descriptionstart = String("{\"")+String(domain)+String("/")+String(subdomain)+String("/")+String(service)+String("\":[\n");
const char description1[] = "{\"name\":\"T";
const char description2[] = "\",\"type\":\"analogin\"}";
const char descriptionend[] = "]}";

const long interval = 10000;
unsigned long previousMillis = 0;

// define sensors on the 6 Analog inputs


uint32_t x=0;

int count = 0;

OneWire oneWireA(ONE_WIRE_BUSA);
OneWire oneWireB(ONE_WIRE_BUSB);

// Pass our oneWire reference to Dallas Temperature.
DallasTemperature sensorsA(&oneWireA);
DallasTemperature sensorsB(&oneWireB);
int deviceCountA = 0;
int deviceCountB = 0;


void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600);
  wdt_enable(WDTO_16S);

  // Start up the library:
  sensorsA.begin();
  sensorsB.begin();
  // Locate the devices on the bus:
  Serial.println("Locating devices...");
  Serial.print("Found ");
  deviceCountA = sensorsA.getDeviceCount();
  for(int index=0; index<deviceCountA; index++) {
    DeviceAddress deviceAddress;
    sensorsA.getAddress(deviceAddress, index);
    sensorsA.setResolution(deviceAddress, 12);
  }
  deviceCountB = sensorsB.getDeviceCount();
  for(int index=0; index<deviceCountB; index++) {
    DeviceAddress deviceAddress;
    sensorsB.getAddress(deviceAddress, index);
    sensorsB.setResolution(deviceAddress, 12);
  }
  
  Serial.print(deviceCountA+deviceCountB);
  Serial.println(" devices");

 // attempt to connect to Wifi network:
  Serial.print("Attempting to connect to WPA SSID: ");
  Serial.println(ssid);
  wdt_reset();
  while (WiFi.begin(ssid, pass) != WL_CONNECTED) {
    //failed, retry
    Serial.print(".");
    delay(5000);
  }
  //Serial.println("You're connected to the network");
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

  wdt_reset();
  Serial.print("Attempting to connect to the MQTT broker: ");
  Serial.println(broker);
  while (!mqttClient.connect(broker, port)) {
      Serial.print("MQTT connection failed! Error code = ");
      Serial.println(mqttClient.connectError());
      delay(10000);
  }
  wdt_reset();
  Serial.println("You're connected to the MQTT broker!");
  //Serial.println();

  // set the message receive callback
  mqttClient.onMessage(onMqttMessage);

  //Serial.print("Subscribing to topic: ");
  //Serial.println();

  // subscribe to a topic
  // the second paramter set's the QoS of the subscription,
  // the the library supports subscribing at QoS 0, 1, or 2
  int subscribeQos = 1;

  String receiveTopic = String(inTopic)+String(domain)+String("/")+String(subdomain)+String("/")+String(service)+String("/+");
  // Serial.println(receiveTopic);
  wdt_reset();
  mqttClient.subscribe(requestTopic, subscribeQos);
  wdt_reset();
}

void loop() {
  int res;

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
    String source;
  
    source = String(outTopic)+String(domain)+String("/")+String(subdomain)+String("/")+String(service)+String("/T");
    source += count;
    float tempC;
    if(count<deviceCountA) {
      sensorsA.requestTemperatures();
      tempC = sensorsA.getTempCByIndex(count);
    }
    else {
      sensorsB.requestTemperatures();
      tempC = sensorsB.getTempCByIndex(count-deviceCountA);
    }
    payload = "{\"event\":\"";
    payload += tempC;
    payload += "\"}";
    wdt_reset();
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
    if(count>=deviceCountA+deviceCountB) count = 0;
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
       msg = descriptionstart;
       for(int i=0; i<deviceCountA+deviceCountB; i++) {
         msg += description1;
         msg += i;
         msg += description2;
         if(i<(deviceCountA+deviceCountB - 1)) msg += ",";
       }
       msg += descriptionend;
       String descriptionTopic = String(outTopic)+String(domain)+String("/")+String(subdomain)+String("/")+String(service)+String("/description");
       mqttClient.beginMessage(descriptionTopic.c_str(), msg.length(), retained, qos, dup);
       mqttClient.print(msg);

       res = mqttClient.endMessage();
      // Serial.println(descriptionTopic);
    }
  }
}

