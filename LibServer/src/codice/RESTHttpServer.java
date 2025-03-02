package codice;
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

        static public int port=10000;

	public static void main(String[] args) throws IOException {
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
		HttpServer server=HttpServer.create(new InetSocketAddress(port),0);
		System.out.println("server listening on port: "+port);
		server.createContext("/lib", new libHandler());
		server.createContext("/lib/files/",new fileHandler());
		//server.createContext("/",new RootHandler());
		server.setExecutor(threadPoolExecutor);
		server.start();
	}
}

