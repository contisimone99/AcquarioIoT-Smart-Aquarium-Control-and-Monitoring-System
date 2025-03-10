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

public class Start implements HttpHandler {
  static private Publisher mqtt_client;

  Start(Publisher client) {
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
	JsonBody body = new JsonBody(exchange.getRequestBody());
	String domain = body.getDomain();
	String topic = "conf/"+domain+"/all/appmanager/start";
        try {
            mqtt_client.publish(topic,body.body);
        }
        catch (MqttException e) {
            e.printStackTrace();
        }
       exchange.getRequestBody().readAllBytes();
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
