import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpPrincipal;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URLDecoder;
import org.eclipse.paho.client.mqttv3.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Install implements HttpHandler {
  static private Publisher mqtt_client;

  Install(Publisher client) {
	mqtt_client = client;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
      URI requestURI = exchange.getRequestURI();
      String response;
      //printRequestInfo(exchange);
      List<String> strlist = new ArrayList<String>();

      String requestMethod = exchange.getRequestMethod();

      if(requestMethod.compareTo("POST")==0||requestMethod.compareTo("post")==0) {
	ArrayList<String> hosts = new ArrayList<String>();
	String topic;
	JsonBody body = new JsonBody(exchange.getRequestBody());
	String domain = body.getDomain();
	body.getHosts(hosts);
	for(int i=0; i<hosts.size(); i++) 
	   if(!hosts.get(i).equals("cloud")) {
		 topic = "conf/"+domain+"/"+hosts.get(i)+"/appmanager/install";
		 try {
		   mqtt_client.publish(topic,body.body);
		 }
	    	 catch (MqttException e) {
            		e.printStackTrace();
		 }
		   
	   }
        response = "{\"result\":\"done\"}";
        exchange.getResponseHeaders().put("content-type",strlist);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
	os.close();
     }
     else {
        System.out.println("Operation not supported!");
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.close();
     }
  }
}
