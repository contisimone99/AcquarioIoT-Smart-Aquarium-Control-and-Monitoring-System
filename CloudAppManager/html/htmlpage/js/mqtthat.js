

// called when the client connects
function onConnect() {
  // Once a connection has been made, make a subscription and send a message.
  client.subscribe(subscription);
  message = new Paho.MQTT.Message(subscription);
  message.destinationName = "subscription";
  client.send(message);
}

// called when the client loses its connection
function onConnectionLost(responseObject) {
  if (responseObject.errorCode !== 0) {
    console.log("onConnectionLost:"+responseObject.errorMessage);
  }
}

// called when a message arrives
function onMessageArrived(message) {
// console.log(message.destinationName+", "+message.payloadString);
  var topic_name = message.destinationName.split("/");
  if(topic_name.length <1) return;
  var name = topic_name.pop();
  var name_content = message.payloadString.split(":");
  var value = cleanItem(name_content[1]);
  if(name.includes("IN")) field_update(name,value);
  else button_update(name, value);
}

function cleanItem(item) {
  var citem = "";
  for(i=0; i<item.length; i++) if(item[i] != '"' && item[i] != '}' && item[i] != '{' && item[i] != '\n') citem += item[i];
  return citem;
}

function normalizeValue(value, digit) {
  var res = "";
  var n = 0;
  var dot = false;
  for(i=0; i<value.length && n <digit; i++) {
    res += value[i];
    if(dot == true) n++;
    if(value[i] == '.') dot = true;
  }
  return res;
}

function getNamefromTag(name) {
  var names = name.split("-");
  if(names.length >1) return names[1];
  else return "";
}

function button_update(name, value) {
  var id = "I_"+name;
  var image = document.getElementById(id);
  if(image == null) return;
  if (value=="false" || value<1) {
     image.src = "/img/Button-Blank-Green-icon.png"
  } else {
     image.src = "/img/Button-Blank-Red-icon.png"
  }
  return;
}


function field_update(name, value) {
   //document.getElementById(name).innerHtml=value;
   var pname = "#"+name;
   $(pname).text(value);
   return;
}

doClick = function(name) {
  var id = "I_"+name;
  var image = document.getElementById(id);
  if(image.src.includes("Button-Blank-Green-icon.png")) {
      mqtt_message = new Paho.MQTT.Message("{cmd:1}");
      mqtt_message.destinationName = "to/"+mqtt_domain+"/"+mqtt_subdomain+"/gpio/"+name;
      client.send(mqtt_message);
   }
   else {
     mqtt_message = new Paho.MQTT.Message("{cmd:0}");
     mqtt_message.destinationName = "to/"+mqtt_domain+"/"+mqtt_subdomain+"/gpio/"+name;
     client.send(mqtt_message);
   }
   return;
}

