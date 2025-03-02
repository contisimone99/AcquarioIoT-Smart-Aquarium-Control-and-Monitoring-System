package codice;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class libHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		URI requestURI = exchange.getRequestURI();
		//System.out.println(requestURI);
		String response;
		// printRequestInfo(exchange);
		List<String> strlist = new ArrayList<String>();
		String requestMethod = exchange.getRequestMethod();
		String[] pathnames; 
		if (requestMethod.compareTo("GET") == 0 || requestMethod.compareTo("get") == 0) {
			File list = new File("src/codice/lib/");
			pathnames = list.list();
			Gson gson = new GsonBuilder().create();
			JsonResponse listFiles= new JsonResponse(pathnames);
			response = gson.toJson(listFiles,JsonResponse.class);
			exchange.getResponseHeaders().put("content-type", strlist);
			exchange.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} else {
			System.out.println("Operation not supported!");
			exchange.sendResponseHeaders(200, 0);
			OutputStream os = exchange.getResponseBody();
			os.close();
		}
	}

}
