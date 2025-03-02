package codice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class fileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		URI requestURI = exchange.getRequestURI();
		System.out.println(requestURI);
		String response="";
		// printRequestInfo(exchange);
		List<String> strlist = new ArrayList<String>();
		strlist.add("application/zip");
		String requestMethod = exchange.getRequestMethod();
		String[] pathnames;
		if (requestMethod.compareTo("GET") == 0 || requestMethod.compareTo("get") == 0) {
			String request= requestURI.toString().split("/")[3];
			System.out.println(request);
			String path="src/codice/lib/";
			File list = new File(path);
			pathnames = list.list();
			File requestFile=null;
			for(String pathname: pathnames) {
				if(request.equals(pathname))
					requestFile=new File(path+pathname);
			}
			//trasformo file in input stream per inviarlo
			InputStream is =new FileInputStream(requestFile);
			RequestFile rf= new RequestFile(is);
			//inviamo file zip tramite http (funziona anche con file normali)
			exchange.getResponseHeaders().put("content-type", strlist);
			exchange.sendResponseHeaders(200, rf.buffer.length);
			OutputStream os = exchange.getResponseBody();
			os.write(rf.buffer);
			os.close();
		} else {
			System.out.println("Operation not supported!");
			exchange.sendResponseHeaders(200, 0);
			OutputStream os = exchange.getResponseBody();
			os.close();
		}
	}

	}


