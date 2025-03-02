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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RESTHttpServer {

    static public int port=8080;
	static public Conf conf;
	static public Publisher mqtt_client;

	public static void main(String[] args) throws IOException {
		if(args.length>=1)
		try {
		    conf = new Conf(args);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		port = Integer.parseInt(conf.get("port"));
 		mqtt_client = new Publisher(conf.protocol+"://"+conf.broker);
		mqtt_client.start();
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
		HttpServer server=HttpServer.create(new InetSocketAddress(port),0);
		server.createContext("/apirest", new HtmlPage(conf.confdir));
		server.createContext("/install", new Install(mqtt_client));
		server.createContext("/start", new Start(mqtt_client));
		server.createContext("/stop", new Stop(mqtt_client));
		server.createContext("/delete", new Delete(mqtt_client));
		server.setExecutor(threadPoolExecutor);
		server.start();
	}
}

